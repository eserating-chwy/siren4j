/*******************************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Erik R Serating
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *********************************************************************************************/
package com.google.code.siren4j.converter;


import com.google.code.siren4j.Siren4J;
import com.google.code.siren4j.annotations.*;
import com.google.code.siren4j.annotations.Siren4JCondition.Type;
import com.google.code.siren4j.annotations.Siren4JInclude.Include;
import com.google.code.siren4j.component.Action;
import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.component.Link;
import com.google.code.siren4j.component.builder.ActionBuilder;
import com.google.code.siren4j.component.builder.EntityBuilder;
import com.google.code.siren4j.component.builder.FieldBuilder;
import com.google.code.siren4j.component.builder.LinkBuilder;
import com.google.code.siren4j.condition.Condition;
import com.google.code.siren4j.condition.ConditionFactory;
import com.google.code.siren4j.error.Siren4JConversionException;
import com.google.code.siren4j.error.Siren4JException;
import com.google.code.siren4j.error.Siren4JRuntimeException;
import com.google.code.siren4j.meta.FieldOption;
import com.google.code.siren4j.meta.FieldType;
import com.google.code.siren4j.resource.CollectionResource;
import com.google.code.siren4j.resource.Resource;
import com.google.code.siren4j.util.ComponentUtils;
import com.google.code.siren4j.util.ReflectionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReflectingConverter implements ResourceConverter {

    private static Logger LOG = LoggerFactory.getLogger(ReflectingConverter.class);

    private static final Pattern schemePattern = Pattern.compile("\\w[\\w\\d\\+\\-\\.]*:");

    private ResourceRegistry registry;

    /**
     * @since 1.1.0
     */
    private boolean errorOnMissingProperty;

    /**
     * @since 1.1.0
     */
    private boolean suppressBaseUriOnFullyQualified;

    /**
     * Protected ctor to prevent direct instantiation.
     *
     * @throws Siren4JException
     */
    protected ReflectingConverter() throws Siren4JException {
        this(null);
    }

    protected ReflectingConverter(ResourceRegistry registry) throws Siren4JException {
        this.registry = registry;
    }

    /**
     * Gets a new  instance of the converter.
     *
     * @return the converter, never <code>null</code>.
     * @throws Siren4JException
     */
    public static ResourceConverter newInstance(ResourceRegistry registry) throws Siren4JException {
        return new ReflectingConverter(registry);
    }

    /**
     * Gets a new  instance of the converter.
     *
     * @return the converter, never <code>null</code>.
     * @throws Siren4JException
     */
    public static ResourceConverter newInstance() throws Siren4JException {
        return new ReflectingConverter(null);
    }

    /**
     * When set to <code>true</code> an exception will be thrown for missing target properties when
     * converting from entity to object. Default is <code>false</code>
     */
    public boolean isErrorOnMissingProperty() {
        return errorOnMissingProperty;
    }

    /**
     * When set to <code>true</code> an exception will be thrown for missing target properties when
     * converting from entity to object.
     * @param errorOnMissingProperty
     */
    public void setErrorOnMissingProperty(boolean errorOnMissingProperty) {
        this.errorOnMissingProperty = errorOnMissingProperty;
    }

    public boolean isSuppressBaseUriOnFullyQualified() {
        return suppressBaseUriOnFullyQualified;
    }

    /**
     * If set to <code>true</code> will suppress base uri links if resource has fully qualified links set. Default
     * is <code>false</code>.
     * @param suppressBaseUriOnFullyQualified
     */
    public void setSuppressBaseUriOnFullyQualified(boolean suppressBaseUriOnFullyQualified) {
        this.suppressBaseUriOnFullyQualified = suppressBaseUriOnFullyQualified;
    }

    /* (non-Javadoc)
             * @see com.google.code.siren4j.converter.ResourceConverter#toEntity(java.lang.Object)
             */
    public Entity toEntity(Object obj) {
        try {
            return toEntity(obj, null, null, null);
        } catch (Siren4JException e) {
            throw new Siren4JConversionException(e);
        }
    }

    public Object toObject(Entity entity) {
        return toObject(entity, null);
    }

    /* (non-Javadoc)
     * @see com.google.code.siren4j.converter.ResourceConverter#toObject(com.google.code.siren4j.component.Entity)
     */
    public Object toObject(Entity entity, Class targetClass) {
        if (registry == null) {
            LOG.warn("No ResourceRegistry set, using default which "
                    + "will scan the entire classpath. It would be better to set your own registry that filters by packages.");
            try {
                registry = ResourceRegistryImpl.newInstance((String[]) null);
            } catch (Siren4JException e) {
                throw new Siren4JRuntimeException(e);
            }
        }
        Resource resource = null;
        if (entity != null) {
            String sirenClass = targetClass != null
                    ? targetClass.getName()
                    : (String) entity.getProperties().get(Siren4J.CLASS_RESERVED_PROPERTY);
            String[] eClass = entity.getComponentClass();
            if (StringUtils.isBlank(sirenClass) && (eClass == null || eClass.length == 0)) {
                throw new Siren4JConversionException(
                        "No entity class defined, won't be able to match to Java class. Can't go on.");
            }

            if (StringUtils.isBlank(sirenClass)) {
                sirenClass = eClass[0];
            }
            if (!registry.containsEntityEntry(sirenClass)) {
                throw new Siren4JConversionException("No matching resource found in the registry. Can't go on.");
            }
            Class<?> clazz = registry.getClassByEntityName(sirenClass);
            List<ReflectedInfo> fieldInfo = ReflectionUtils.getExposedFieldInfo(clazz);
            Object obj = null;
            try {
                obj = clazz.newInstance();
            } catch (Exception e) {
                throw new Siren4JConversionException(e);
            }

            // Set properties
            handleSetProperties(obj, clazz, entity, fieldInfo);
            // Set sub entities
            handleSetSubEntities(obj, clazz, entity, fieldInfo);

            resource = (Resource) obj;
        }
        return resource;
    }

    /**
     * Sets field value for an entity's property field.
     *
     * @param obj assumed not <code>null</code>.
     * @param clazz assumed not <code>null</code>.
     * @param entity assumed not <code>null</code>.
     * @param fieldInfo assumed not <code>null</code>.
     * @throws Siren4JConversionException
     */
    private void handleSetProperties(Object obj, Class<?> clazz, Entity entity, List<ReflectedInfo> fieldInfo)
            throws Siren4JConversionException {
        if (!MapUtils.isEmpty(entity.getProperties())) {
            for (String key : entity.getProperties().keySet()) {
                if (key.startsWith(Siren4J.CLASS_RESERVED_PROPERTY)) {
                    continue;
                }
                ReflectedInfo info = ReflectionUtils.getFieldInfoByEffectiveName(fieldInfo, key);
                if (info == null) {
                    info = ReflectionUtils.getFieldInfoByName(fieldInfo, key);
                }
                if (info != null) {
                    Object val = entity.getProperties().get(key);
                    try {
                        ReflectionUtils.setFieldValue(obj, info, val);
                    } catch (Siren4JException e) {
                        throw new Siren4JConversionException(e);
                    }
                } else if (isErrorOnMissingProperty() && !(obj instanceof Collection && key.equals("size"))) {
                    //Houston we have a problem!!
                    throw new Siren4JConversionException(
                            "Unable to find field: " + key + " for class: " + clazz.getName());
                }
            }
        }
    }

    /**
     * Sets field value for an entity's sub entities field.
     *
     * @param obj assumed not <code>null</code>.
     * @param clazz assumed not <code>null</code>.
     * @param entity assumed not <code>null</code>.
     * @param fieldInfo assumed not <code>null</code>.
     * @throws Siren4JConversionException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void handleSetSubEntities(Object obj, Class<?> clazz, Entity entity, List<ReflectedInfo> fieldInfo)
            throws Siren4JConversionException {
        if (!CollectionUtils.isEmpty(entity.getEntities())) {
            for (Entity ent : entity.getEntities()) {
                //Skip embedded as we can't deal with them.
                if (StringUtils.isNotBlank(ent.getHref())) {
                    continue;
                }
                String[] rel = ent.getRel();
                if (ArrayUtils.isEmpty(rel)) {
                    throw new Siren4JConversionException("No relationship set on sub entity. Can't go on.");
                }
                String fieldKey = rel.length == 1 ? rel[0] : ArrayUtils.toString(rel);
                ReflectedInfo info = ReflectionUtils.getFieldInfoByEffectiveName(fieldInfo, fieldKey);
                if (info != null) {
                    try {
                        Object subObj = toObject(ent);
                        if (subObj != null) {
                            if (subObj.getClass().equals(CollectionResource.class)) {
                                ReflectionUtils.setFieldValue(obj, info, subObj);
                                continue; // If subObj is collection resource then continue
                                // or it will get wrapped into another collection which we don't want.
                            }
                            if (isCollection(obj, info.getField())) {
                                //If we are a collection we need to add each subObj via the add method
                                //and not a setter. So we need to grab the collection from the field value.
                                try {
                                    Collection coll = (Collection) info.getField().get(obj);
                                    if (coll == null) {
                                        //In the highly unlikely event that no collection is set on the
                                        //field value we will create a new collection here.
                                        coll = new CollectionResource();
                                        ReflectionUtils.setFieldValue(obj, info, coll);
                                    }
                                    coll.add(subObj);
                                } catch (Exception e) {
                                    throw new Siren4JConversionException(e);
                                }
                            } else {
                                ReflectionUtils.setFieldValue(obj, info, subObj);
                            }
                        }
                    } catch (Siren4JException e) {
                        throw new Siren4JConversionException(e);
                    }
                } else {
                    throw new Siren4JConversionException(
                            "Unable to find field: " + fieldKey + " for class: " + clazz.getName());
                }
            }
        }
    }

    /**
     * The recursive method that actually does the work of converting from a resource to an entity.
     *
     * @param obj the object to be converted, this could be a <code>Resource</code> or another <code>Object</code>. Only
     * <code>Resource</code> objects are allowed in via public methods, other object types may come from recursing into the
     * original resource. May be <code>null</code>.
     * @param parentField the parent field, ie the field that contained this object, may be <code>null</code>.
     * @param parentObj the object that contains the field that contains this object, may be <code>null</code>.
     * @param parentFieldInfo field info for all exposed parent object fields, may be <code>null</code>.
     * @return the entity created from the resource. May be <code>null</code>.
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    protected Entity toEntity(Object obj, Field parentField, Object parentObj, List<ReflectedInfo> parentFieldInfo)
            throws Siren4JException {
        if (obj == null) {
            return null;
        }

        EntityBuilder builder = EntityBuilder.newInstance();
        Class<?> clazz = obj.getClass();

        boolean embeddedLink = false;
        List<ReflectedInfo> fieldInfo = ReflectionUtils.getExposedFieldInfo(clazz);
        EntityContext context = new EntityContextImpl(obj, fieldInfo, parentField, parentObj, parentFieldInfo);

        String cname = null;
        String uri = "";

        //Propagate baseUri and fullyQualified setting from parent if needed
        propagateBaseUriAndQualifiedSetting(obj, parentObj);

        boolean suppressClass = false;
        Siren4JEntity entityAnno = (Siren4JEntity) clazz.getAnnotation(Siren4JEntity.class);
        if (entityAnno != null && (StringUtils.isNotBlank(entityAnno.name()) && ArrayUtils
                .isNotEmpty(entityAnno.entityClass()))) {
            throw new Siren4JRuntimeException("Must only use one of 'name' or 'entityClass', not both.");
        }
        if (entityAnno != null) {
            cname = StringUtils.defaultIfEmpty(entityAnno.name(), cname);
            uri = StringUtils.defaultIfEmpty(entityAnno.uri(), uri);
            suppressClass = entityAnno.suppressClassProperty();
        }

        if (!suppressClass) {
            builder.addProperty(Siren4J.CLASS_RESERVED_PROPERTY, clazz.getName());
        }

        Siren4JSubEntity parentSubAnno = null;
        if (parentField != null && parentFieldInfo != null) {
            parentSubAnno = getSubEntityAnnotation(parentField, parentFieldInfo);
        }

        if (parentSubAnno != null) {
            uri = StringUtils.defaultIfEmpty(parentSubAnno.uri(), uri);
            //Determine if the entity is an embeddedLink
            embeddedLink = parentSubAnno.embeddedLink();
            if (obj instanceof Resource) {
                Boolean overrideLink = ((Resource) obj).getOverrideEmbeddedLink();
                if (overrideLink != null) {
                    embeddedLink = overrideLink.booleanValue();
                }
            }
        }
        // Handle uri overriding or token replacement
        String resolvedUri = resolveUri(uri, context, true);

        builder.setComponentClass(getEntityClass(obj, cname, entityAnno));
        if (parentSubAnno != null) {
            String fname = parentField != null ? parentField.getName() : cname;
            builder.setRelationship(ComponentUtils.isStringArrayEmpty(parentSubAnno.rel())
                    ? new String[]{fname} : parentSubAnno.rel());
        }
        if (embeddedLink) {
            builder.setHref(resolvedUri);
        } else {
            for (ReflectedInfo info : fieldInfo) {
                Field currentField = info.getField();
                if(currentField != null) {
                    handleField(obj, builder, fieldInfo, currentField);
                } else {
                    handleMethod(obj, builder, info);
                }

            }
            if (obj instanceof Collection) {
                builder.addProperty("size", ((Collection<?>) obj).size());
            }
            if (obj instanceof Resource) {
                Resource res  = (Resource)obj;
                boolean skipBaseUri = isSuppressBaseUriOnFullyQualified() && (res.isFullyQualifiedLinks()
                        && res.getBaseUri() != null);
                if(!skipBaseUri) {
                    handleBaseUriLink(builder, res.getBaseUri());
                }
            }
            handleSelfLink(builder, resolvedUri);
            handleEntityLinks(builder, context);
            handleEntityActions(builder, context);
        }
        return builder.build();
    }

    private void handleMethod(Object obj, EntityBuilder builder, ReflectedInfo fieldInfo) {
        Object value = ReflectionUtils.getMethodValue(fieldInfo.getGetter(), obj);
        handleAddProperty(builder, fieldInfo.getEffectiveName(), value);
    }

    private void handleField(Object obj, EntityBuilder builder, List<ReflectedInfo> fieldInfo, Field currentField) throws Siren4JException {
        Object fieldVal = ReflectionUtils.getFieldValue(currentField, obj);
        if (ReflectionUtils.isSirenProperty(currentField.getType(), fieldVal, currentField)) {
            // Property
            if (!skipProperty(obj, currentField)) {
                String propName = currentField.getName();
                Siren4JProperty propAnno = currentField.getAnnotation(Siren4JProperty.class);

                if (propAnno != null && StringUtils.isNotBlank(propAnno.name())) {
                    // Override field name from annotation
                    propName = propAnno.name();
                }
                handleAddProperty(builder, propName, currentField, obj);
            }
        } else {
            // Sub Entity
            if (!skipProperty(obj, currentField)) {
                handleSubEntity(builder, obj, currentField, fieldInfo);
            }
        }
    }

    private void propagateBaseUriAndQualifiedSetting(Object obj, Object parentObj) {
        if (parentObj != null && parentObj instanceof Resource && obj instanceof Resource) {
            Resource parentResource = (Resource) parentObj;
            Resource resource = (Resource) obj;
            if (StringUtils.isNotBlank(parentResource.getBaseUri())) {
                resource.setBaseUri(parentResource.getBaseUri());
            }
            if (parentResource.isFullyQualifiedLinks() != null) {
                resource.setFullyQualifiedLinks(parentResource.isFullyQualifiedLinks());
            }
        }
    }

    /**
     * Handles adding a property to an entity builder. Called by {@link ReflectingConverter#toEntity(Object, Field, Object, List)}
     * for each property found.
     *
     * @param builder
     * @param propName
     * @param currentField
     * @param obj
     */
    protected void handleAddProperty(EntityBuilder builder, String propName, Field currentField, Object obj) {
        builder.addProperty(propName, ReflectionUtils.getFieldValue(currentField, obj));
    }

    protected void handleAddProperty(EntityBuilder builder, String propName, Object propValue) {
        builder.addProperty(propName, propValue);
    }

    /**
     * Handles sub entities.
     *
     * @param builder assumed not <code>null</code>.
     * @param obj assumed not <code>null</code>.
     * @param currentField assumed not <code>null</code>.
     * @throws Siren4JException
     */
    private void handleSubEntity(EntityBuilder builder, Object obj, Field currentField, List<ReflectedInfo> fieldInfo)
            throws Siren4JException {

        Siren4JSubEntity subAnno = getSubEntityAnnotation(currentField, fieldInfo);
        if (subAnno != null) {
            if (isCollection(obj, currentField)) {
                Collection<?> coll = (Collection<?>) ReflectionUtils.getFieldValue(currentField, obj);
                if (coll != null) {
                    for (Object o : coll) {
                        builder.addSubEntity(toEntity(o, currentField, obj, fieldInfo));
                    }
                }
            } else {
                Object subObj = ReflectionUtils.getFieldValue(currentField, obj);
                if (subObj != null) {
                    builder.addSubEntity(toEntity(subObj, currentField, obj, fieldInfo));
                }
            }
        }

    }

    /**
     * Resolves the raw uri by replacing field tokens with the actual data.
     *
     * @param rawUri assumed not <code>null</code> or .
     * @param context
     * @return uri with tokens resolved.
     * @throws Siren4JException
     */
    private String resolveUri(String rawUri, EntityContext context, boolean handleURIOverride) throws Siren4JException {
        String resolvedUri = rawUri;
        String baseUri = null;
        boolean fullyQualified = false;
        if (context.getCurrentObject() instanceof Resource) {
            Resource resource = (Resource) context.getCurrentObject();
            baseUri = resource.getBaseUri();
            fullyQualified = resource.isFullyQualifiedLinks() == null ? false : resource.isFullyQualifiedLinks();
            String override = resource.getOverrideUri();
            if (handleURIOverride && StringUtils.isNotBlank(override)) {
                resolvedUri = override;
            }
        }
        resolvedUri = handleTokenReplacement(resolvedUri, context);

        if (fullyQualified && StringUtils.isNotBlank(baseUri)
                && !isAbsoluteUri(resolvedUri)) {
            StringBuffer sb = new StringBuffer();
            sb.append(baseUri.endsWith("/") ? baseUri.substring(0, baseUri.length() - 1) : baseUri);
            sb.append(resolvedUri.startsWith("/") ? resolvedUri : "/" + resolvedUri);
            resolvedUri = sb.toString();
        }
        return resolvedUri;
    }

    private boolean isAbsoluteUri(String resolvedUri) {
        Matcher matcher = schemePattern.matcher(resolvedUri);
        return matcher.find() && matcher.start() == 0;
    }

    /**
     * Helper method to do token replacement for strings.
     *
     * @param str
     * @param context
     * @return
     * @throws Siren4JException
     */
    private String handleTokenReplacement(String str, EntityContext context) throws Siren4JException {
        String result = "";
        // First resolve parents
        result = ReflectionUtils.replaceFieldTokens(
                context.getParentObject(), str, context.getParentFieldInfo(), true);
        // Now resolve others
        result = ReflectionUtils.flattenReservedTokens(ReflectionUtils.replaceFieldTokens(
                context.getCurrentObject(), result, context.getCurrentFieldInfo(), false));
        return result;
    }

    /**
     * Helper to retrieve a sub entity annotation from either the field itself or the getter.
     * If an annotation exists on both, then the field wins.
     *
     * @param currentField assumed not <code>null</code>.
     * @param fieldInfo assumed not <code>null</code>.
     * @return the annotation if found else <code>null</code>.
     */
    private Siren4JSubEntity getSubEntityAnnotation(Field currentField, List<ReflectedInfo> fieldInfo) {
        Siren4JSubEntity result = null;
        result = currentField.getAnnotation(Siren4JSubEntity.class);
        if (result == null && fieldInfo != null) {
            ReflectedInfo info = ReflectionUtils.getFieldInfoByName(fieldInfo, currentField.getName());
            if (info != null && info.getGetter() != null) {
                result = info.getGetter().getAnnotation(Siren4JSubEntity.class);
            }
        }
        return result;
    }

    /**
     * Add the self link to the entity.
     *
     * @param builder assumed not <code>null</code>.
     * @param resolvedUri the token resolved uri. Assumed not blank.
     */
    private void handleSelfLink(EntityBuilder builder, String resolvedUri) {
        if (StringUtils.isBlank(resolvedUri)) {
            return;
        }
        Link link = LinkBuilder.newInstance().setRelationship(Link.RELATIONSHIP_SELF).setHref(resolvedUri).build();
        builder.addLink(link);
    }

    /**
     * Add the baseUri link to the entity if the baseUri is set on the entity.
     *
     * @param builder assumed not <code>null</code>.
     * @param baseUri the token resolved uri. Assumed not blank.
     */
    private void handleBaseUriLink(EntityBuilder builder, String baseUri) {
        if (StringUtils.isBlank(baseUri)) {
            return;
        }
        Link link = LinkBuilder.newInstance().setRelationship(Link.RELATIONSHIP_BASEURI).setHref(baseUri).build();
        builder.addLink(link);
    }

    /**
     * Handles getting all entity links both dynamically set and via annotations and merges them together overriding with
     * the proper precedence order which is Dynamic > SubEntity > Entity. Href and uri's are resolved with the correct data
     * bound in.
     *
     * @param builder assumed not <code>null</code>.
     * @param context assumed not <code>null</code>.
     * @throws Exception
     */
    private void handleEntityLinks(EntityBuilder builder, EntityContext context) throws Siren4JException {

        Class<?> clazz = context.getCurrentObject().getClass();
        Map<String, Link> links = new HashMap<String, Link>();
        /* Caution!! Order matters when adding to the links map */

        Siren4JEntity entity = clazz.getAnnotation(Siren4JEntity.class);
        if (entity != null && ArrayUtils.isNotEmpty(entity.links())) {
            for (Siren4JLink l : entity.links()) {
                if (evaluateConditional(l.condition(), context)) {
                    links.put(ArrayUtils.toString(l.rel()), annotationToLink(l, context));
                }
            }
        }
        if (context.getParentField() != null) {
            Siren4JSubEntity subentity = context.getParentField().getAnnotation(Siren4JSubEntity.class);
            if (subentity != null && ArrayUtils.isNotEmpty(subentity.links())) {
                for (Siren4JLink l : subentity.links()) {
                    if (evaluateConditional(l.condition(), context)) {
                        links.put(ArrayUtils.toString(l.rel()), annotationToLink(l, context));
                    }
                }
            }
        }

        Collection<Link> resourceLinks = context.getCurrentObject() instanceof Resource
                ? ((Resource) context.getCurrentObject()).getEntityLinks() : null;
        if (resourceLinks != null) {
            for (Link l : resourceLinks) {
                links.put(ArrayUtils.toString(l.getRel()), l);
            }
        }
        for (Link l : links.values()) {
            l.setHref(resolveUri(l.getHref(), context, false));
            builder.addLink(l);
        }

    }

    /**
     * Handles getting all entity actions both dynamically set and via annotations and merges them together overriding with
     * the proper precedence order which is Dynamic > SubEntity > Entity. Href and uri's are resolved with the correct data
     * bound in.
     *
     * @param builder
     * @param context
     * @throws Exception
     */
    private void handleEntityActions(EntityBuilder builder, EntityContext context) throws Siren4JException {
        Class<?> clazz = context.getCurrentObject().getClass();
        Map<String, Action> actions = new HashMap<String, Action>();
        /* Caution!! Order matters when adding to the actions map */

        Siren4JEntity entity = clazz.getAnnotation(Siren4JEntity.class);
        if (entity != null && ArrayUtils.isNotEmpty(entity.actions())) {
            for (Siren4JAction a : entity.actions()) {
                if (evaluateConditional(a.condition(), context)) {
                    actions.put(a.name(), annotationToAction(a, context));
                }
            }
        }
        if (context.getParentField() != null) {
            Siren4JSubEntity subentity = context.getParentField().getAnnotation(Siren4JSubEntity.class);
            if (subentity != null && ArrayUtils.isNotEmpty(subentity.actions())) {
                for (Siren4JAction a : subentity.actions()) {
                    if (evaluateConditional(a.condition(), context)) {
                        actions.put(a.name(), annotationToAction(a, context));
                    }
                }
            }
        }

        Collection<Action> resourceLinks = context.getCurrentObject() instanceof Resource
                ? ((Resource) context.getCurrentObject()).getEntityActions() : null;
        if (resourceLinks != null) {
            for (Action a : resourceLinks) {
                actions.put(a.getName(), a);
            }
        }
        for (Action a : actions.values()) {
            a.setHref(resolveUri(a.getHref(), context, false));
            builder.addAction(a);
        }
    }

    /**
     * Evaluates a value against its specified conditional.
     *
     * @param condition
     * @param context
     * @return
     */
    private boolean evaluateConditional(Siren4JCondition condition, EntityContext context) {
        boolean result = true;
        if (condition != null && !("null".equals(condition.name()))) {
            result = false;
            Object val = null;
            ConditionFactory factory = ConditionFactory.getInstance();
            Condition cond = factory.getCondition(condition.logic());
            Object obj = condition.name().startsWith("parent.")
                    ? context.getParentObject()
                    : context.getCurrentObject();
            String name = condition.name().startsWith("parent.")
                    ? condition.name().substring(7)
                    : condition.name();
            if (obj == null) {
                throw new Siren4JRuntimeException(
                        "No object found. Conditional probably references a parent but does not have a parent: " + condition
                                .name());
            }
            if (condition.type().equals(Type.METHOD)) {
                try {
                    Method method = ReflectionUtils.findMethod(obj.getClass(), name,
                            null);
                    if (method != null) {
                        method.setAccessible(true);
                        val = method.invoke(obj, new Object[]{});
                        result = cond.evaluate(val);
                    } else {
                        throw new Siren4JException(
                                "Method referenced in condition does not exist: " + condition.name());
                    }
                } catch (Exception e) {
                    throw new Siren4JRuntimeException(e);
                }
            } else {
                try {
                    Field field = ReflectionUtils.findField(obj.getClass(), name);
                    if (field != null) {
                        field.setAccessible(true);
                        val = field.get(obj);
                        result = cond.evaluate(val);
                    } else {
                        throw new Siren4JException("Field referenced in condition does not exist: " + condition.name());
                    }
                } catch (Exception e) {
                    throw new Siren4JRuntimeException(e);
                }
            }
        }
        return result;
    }

    /**
     * Convert a link annotation to an actual link. The href will not be resolved in the instantiated link, this will need
     * to be post processed.
     *
     * @param linkAnno assumed not <code>null</code>.
     * @return new link, never <code>null</code>.
     */
    private Link annotationToLink(Siren4JLink linkAnno, EntityContext context) {
        LinkBuilder builder = LinkBuilder.newInstance().setRelationship(linkAnno.rel()).setHref(linkAnno.href());
        if (StringUtils.isNotBlank(linkAnno.title())) {
            builder.setTitle(linkAnno.title());
        }
        if (ArrayUtils.isNotEmpty(linkAnno.linkClass())) {
            builder.setComponentClass(linkAnno.linkClass());
        }
        return builder.build();
    }

    /**
     * Convert an action annotation to an actual action. The href will not be resolved in the instantiated action, this will
     * need to be post processed.
     *
     * @param actionAnno assumed not <code>null</code>.
     * @return new action, never <code>null</code>.
     */
    private Action annotationToAction(Siren4JAction actionAnno, EntityContext context) throws Siren4JException {
        ActionBuilder builder = ActionBuilder.newInstance();
        builder.setName(actionAnno.name()).setHref(actionAnno.href()).setMethod(actionAnno.method());
        if (ArrayUtils.isNotEmpty(actionAnno.actionClass())) {
            builder.setComponentClass(actionAnno.actionClass());
        }
        if (StringUtils.isNotBlank(actionAnno.title())) {
            builder.setTitle(actionAnno.title());
        }
        if (StringUtils.isNotBlank(actionAnno.type())) {
            builder.setType(actionAnno.type());
        }
        if (ArrayUtils.isNotEmpty(actionAnno.fields())) {
            for (Siren4JActionField f : actionAnno.fields()) {
                builder.addField(annotationToField(f, context));
            }
        }
        return builder.build();
    }

    /**
     * Convert a field annotation to an actual field.
     *
     * @param fieldAnno assumed not <code>null</code>.
     * @return new field, never <code>null</code>.
     */
    private com.google.code.siren4j.component.Field annotationToField(Siren4JActionField fieldAnno,
                                                                      EntityContext context)
            throws Siren4JException {
        FieldBuilder builder = FieldBuilder.newInstance();
        builder.setName(fieldAnno.name());
        if (ArrayUtils.isNotEmpty(fieldAnno.fieldClass())) {
            builder.setComponentClass(fieldAnno.fieldClass());
        }
        if(StringUtils.isNotBlank(fieldAnno.title())) {
            builder.setTitle(fieldAnno.title());
        }
        if (fieldAnno.max() > -1) {
            builder.setMax(fieldAnno.max());
        }
        if (fieldAnno.min() > -1) {
            builder.setMin(fieldAnno.min());
        }
        if (fieldAnno.maxLength() > -1) {
            builder.setMaxLength(fieldAnno.maxLength());
        }
        if (StringUtils.isNotBlank(fieldAnno.step())) {
            builder.setStep(fieldAnno.step());
        }
        if (fieldAnno.required()) {
            builder.setRequired(true);
        }
        if (StringUtils.isNotBlank(fieldAnno.pattern())) {
            builder.setPattern(fieldAnno.pattern());
        }
        if (StringUtils.isNotBlank(fieldAnno.type())) {
            builder.setType(FieldType.valueOf(fieldAnno.type().toUpperCase()));
        }
        if (StringUtils.isNotBlank(fieldAnno.value())) {
            builder.setValue(handleTokenReplacement(fieldAnno.value(), context));
        }
        if (ArrayUtils.isNotEmpty(fieldAnno.options())) {
            for (Siren4JFieldOption optAnno : fieldAnno.options()) {
                FieldOption opt = new FieldOption();
                if (StringUtils.isNotBlank(optAnno.title())) {
                    opt.setTitle(optAnno.title());
                }
                if (StringUtils.isNotBlank(optAnno.value())) {
                    opt.setValue(handleTokenReplacement(optAnno.value(), context));
                }
                opt.setOptionDefault(optAnno.optionDefault());
                if (ArrayUtils.isNotEmpty(optAnno.data())) {
                    for (Siren4JOptionData data : optAnno.data()) {
                        opt.putData(data.key(), handleTokenReplacement(data.value(), context));
                    }
                }
                builder.addOption(opt);
            }
        }
        if (StringUtils.isNotBlank(fieldAnno.optionsURL())) {
            builder.setOptionsURL(resolveUri(fieldAnno.optionsURL(), context, false));
        }
        if (StringUtils.isNotBlank(fieldAnno.placeHolder())) {
            builder.setPlaceholder(fieldAnno.placeHolder());
        }
        if(ArrayUtils.isNotEmpty(fieldAnno.metaData())) {
            Map<String, String> metaData = new HashMap();
            for(Siren4JMetaData mdAnno : fieldAnno.metaData()) {
                metaData.put(mdAnno.key(), mdAnno.value());
            }
            builder.setMetaData(metaData);
        }
        return builder.build();
    }

    /**
     * Determine if the property or entity should be skipped based on any existing include policy. The TYPE annotation is
     * checked first and then the field annotation, the field annotation takes precedence.
     *
     * @param obj assumed not <code>null</code>.
     * @param field assumed not <code>null</code>.
     * @return <code>true</code> if the property/enity should be skipped.
     * @throws Siren4JException
     */
    private boolean skipProperty(Object obj, Field field) throws Siren4JException {
        boolean skip = false;
        Class<?> clazz = obj.getClass();
        Include inc = Include.ALWAYS;
        Siren4JInclude typeInclude = clazz.getAnnotation(Siren4JInclude.class);
        if (typeInclude != null) {
            inc = typeInclude.value();
        }
        Siren4JInclude fieldInclude = field.getAnnotation(Siren4JInclude.class);
        if (fieldInclude != null) {
            inc = fieldInclude.value();
        }
        try {
            Object val = field.get(obj);
            switch (inc) {
                case NON_EMPTY:
                    if (val != null) {
                        if (String.class.equals(field.getType())) {
                            skip = StringUtils.isBlank((String) val);
                        } else if (CollectionResource.class.equals(field.getType())) {
                            skip = ((CollectionResource<?>) val).isEmpty();
                        }
                    } else {
                        skip = true;
                    }
                    break;
                case NON_NULL:
                    if (val == null) {
                        skip = true;
                    }
                    break;
                case ALWAYS:
            }
        } catch (Exception e) {
            throw new Siren4JRuntimeException(e);
        }
        return skip;
    }

    /**
     * Determine entity class by first using the name set on the Siren entity and then if not found using the actual class
     * name, though it is preferable to use the first option to not tie to a language specific class.
     *
     * @param obj
     * @param name
     * @return
     */
    public String[] getEntityClass(Object obj, String name, Siren4JEntity entityAnno) {
        Class<?> clazz = obj.getClass();
        String[] compClass = entityAnno == null ? null : entityAnno.entityClass();
        //If entity class specified then use it.
        if (compClass != null && !ArrayUtils.isEmpty(compClass)) {
            return compClass;
        }
        //Else use name or class.
        List<String> entityClass = new ArrayList<String>();
        entityClass.add(StringUtils.defaultString(name, clazz.getName()));
        if (obj instanceof CollectionResource) {
            String tag = getCollectionClassTag();
            if (StringUtils.isNotBlank(tag)) {
                entityClass.add(tag);
            }
        }
        return entityClass.toArray(new String[]{});
    }

    /**
     * Determine if the field is a Collection class and not a CollectionResource class which needs special treatment.
     *
     * @param field
     * @return
     */
    public boolean isCollection(Object obj, Field field) {
        try {
            Object val = field.get(obj);
            boolean isCollResource = false;
            if (val != null) {
                isCollResource = CollectionResource.class.equals(val.getClass());
            }
            return (!isCollResource && !field.getType().equals(CollectionResource.class))
                    && (Collection.class.equals(field.getType()) || ArrayUtils.contains(field.getType().getInterfaces(),
                    Collection.class));
        } catch (Exception e) {
            throw new Siren4JRuntimeException(e);
        }
    }

    /**
     * Returns the collection tag to be added to a collection's class. The
     * default is 'collection'. Can be overridden to change tag or set to return
     * <code>null</code> or empty in which case no tag will be added.
     *
     * @return may be <code>null</code> or empty.
     */
    protected String getCollectionClassTag() {
        return "collection";
    }

}
