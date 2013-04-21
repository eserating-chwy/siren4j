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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.siren4j.annotations.Siren4JAction;
import com.google.code.siren4j.annotations.Siren4JActionField;
import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.annotations.Siren4JInclude;
import com.google.code.siren4j.annotations.Siren4JInclude.Include;
import com.google.code.siren4j.annotations.Siren4JLink;
import com.google.code.siren4j.annotations.Siren4JProperty;
import com.google.code.siren4j.annotations.Siren4JSubEntity;
import com.google.code.siren4j.component.Action;
import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.component.Link;
import com.google.code.siren4j.component.builder.ActionBuilder;
import com.google.code.siren4j.component.builder.EntityBuilder;
import com.google.code.siren4j.component.builder.FieldBuilder;
import com.google.code.siren4j.component.builder.LinkBuilder;
import com.google.code.siren4j.error.Siren4JConversionException;
import com.google.code.siren4j.error.Siren4JException;
import com.google.code.siren4j.error.Siren4JRuntimeException;
import com.google.code.siren4j.meta.FieldType;
import com.google.code.siren4j.resource.CollectionResource;
import com.google.code.siren4j.resource.Resource;
import com.google.code.siren4j.util.ComponentUtils;
import com.google.code.siren4j.util.ReflectionUtils;

public class ReflectingConverter implements ResourceConverter {

    private static Logger LOG = LoggerFactory.getLogger(ReflectingConverter.class);
    
    private ResourceRegistry registry;

    /**
     * Private ctor to prevent direct instantiation.
     * @throws Siren4JException 
     */
    private ReflectingConverter() throws Siren4JException {
       this(null);
    }
    
    private ReflectingConverter(ResourceRegistry registry) throws Siren4JException {
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

    /* (non-Javadoc)
     * @see com.google.code.siren4j.converter.ResourceConverter#toEntity(java.lang.Object)
     */
    public Entity toEntity(Object obj) throws Siren4JConversionException {
        try {
            return toEntity(obj, null, null, null);
        } catch (Siren4JException e) {
            throw new Siren4JConversionException(e);
        }
    }

    /* (non-Javadoc)
     * @see com.google.code.siren4j.converter.ResourceConverter#toObject(com.google.code.siren4j.component.Entity)
     */
    public Object toObject(Entity entity) throws Siren4JConversionException {
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
            String[] eClass = entity.getEntityClass();
            if (eClass == null || eClass.length == 0) {
                throw new Siren4JConversionException(
                    "No entity class defined, won't be able to match to Java class. Can't go on.");
            }
            if (!registry.containsEntityEntry(eClass[0])) {
                throw new Siren4JConversionException("No matching resource found in the registry. Can't go on.");
            }
            Class<?> clazz = registry.getClassByEntityName(eClass[0]);
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
            
            resource = (Resource)obj;
        }
        return resource;
    }
    
    /**
     * Sets field value for an entity's property field.
     * @param obj assumed not <code>null</code>.
     * @param clazz assumed not <code>null</code>.
     * @param entity assumed not <code>null</code>.
     * @param fieldInfo assumed not <code>null</code>.
     * @throws Siren4JConversionException
     */
    private void handleSetProperties(Object obj, Class<?> clazz, Entity entity, List<ReflectedInfo> fieldInfo) throws Siren4JConversionException{
        if(!MapUtils.isEmpty(entity.getProperties())) {
            for(String key :entity.getProperties().keySet()) {
                ReflectedInfo info = ReflectionUtils.getFieldInfoByEffectiveName(fieldInfo, key);
                if(info != null) {
                    Object val = entity.getProperties().get(key);
                    try {
                        ReflectionUtils.setFieldValue(obj, info, val);
                    } catch (Siren4JException e) {
                        throw new Siren4JConversionException(e);
                    }                       
                } else {
                    //Houston we have a problem!!
                    throw new Siren4JConversionException("Unable to find field: " + key + " for class: " + clazz.getName());
                }
            }
        } 
    }
    
    /**
     * Sets field value for an entity's sub entities field.
     * @param obj assumed not <code>null</code>.
     * @param clazz assumed not <code>null</code>.
     * @param entity assumed not <code>null</code>.
     * @param fieldInfo assumed not <code>null</code>.
     * @throws Siren4JConversionException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void handleSetSubEntities(Object obj, Class<?> clazz, Entity entity, List<ReflectedInfo> fieldInfo) throws Siren4JConversionException{
        if(!CollectionUtils.isEmpty(entity.getEntities())) {
            for(Entity ent : entity.getEntities()) {
               //Skip embedded as we can't deal with them.
               if(StringUtils.isNotBlank(ent.getHref())) {
                   continue;
               }
               String[] rel = ent.getRel(); 
               if(ArrayUtils.isEmpty(rel)) {
                   throw new Siren4JConversionException("No relationship set on sub entity. Can't go on.");
               }
               String fieldKey = rel.length == 1 ? rel[0] : ArrayUtils.toString(rel);
               ReflectedInfo info = ReflectionUtils.getFieldInfoByEffectiveName(fieldInfo, fieldKey);
               if(info != null) {
                   try {
                    Object subObj = toObject(ent);
                    if(subObj != null) {
                        if(subObj.getClass().equals(CollectionResource.class)) {
                            ReflectionUtils.setFieldValue(obj, info, subObj);
                            continue; // If subObj is collection resource then continue
                                      // or it will get wrapped into another collection which we don't want.
                        }
                        if(isCollection(obj, info.getField())) {
                            //If we are a collection we need to add each subObj via the add method
                            //and not a setter. So we need to grab the collection from the field value.
                            try {
                                Collection coll = (Collection)info.getField().get(obj);
                                if(coll == null) {
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
                   throw new Siren4JConversionException("Unable to find field: " + fieldKey + " for class: " + clazz.getName());
               }
            }
        }
    }
    
    /**
     * The recursive method that actually does the work of converting from a resource to an entity.
     * 
     * @param obj
     * the object to be converted, this could be a <code>Resource</code> or another <code>Object</code>. Only
     * <code>Resource</code> objects are allowed in via public methods, other object types may come from recursing into the
     * original resource. May be <code>null</code>.
     * @param parentField
     * the parent field, ie the field that contained this object, may be <code>null</code>.
     * @param parentObj
     * the object that contains the field that contains this object, may be <code>null</code>.
     * @param parentFieldInfo
     * field info for all exposed parent object fields, may be <code>null</code>.
     * @return the entity created from the resource. May be <code>null</code>.
     * @throws Exception
     */
    private Entity toEntity(Object obj, Field parentField, Object parentObj, List<ReflectedInfo> parentFieldInfo)
        throws Siren4JException {
        if (obj == null) {
            return null;
        }
        EntityBuilder builder = EntityBuilder.newInstance();
        Class<?> clazz = obj.getClass();
        boolean embeddedLink = false;
        List<ReflectedInfo> fieldInfo = ReflectionUtils.getExposedFieldInfo(clazz);

        String cname = parentField != null ? parentField.getName() : null;
        String uri = "";

        Siren4JEntity entityAnno = (Siren4JEntity) clazz.getAnnotation(Siren4JEntity.class);
        if (entityAnno != null) {
            cname = StringUtils.defaultIfEmpty(entityAnno.name(), cname);
            uri = StringUtils.defaultIfEmpty(entityAnno.uri(), uri);
        }

        Siren4JSubEntity parentSubAnno = null;
        if(parentField != null && parentFieldInfo != null) {
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
        String resolvedUri = resolveUri(uri, obj, fieldInfo, parentField, parentObj, parentFieldInfo);

        builder.setEntityClass(getEntityClass(obj, cname));
        if (parentSubAnno != null) {
            String fname = parentField != null ? parentField.getName() : cname;
            builder.setRelationship(ComponentUtils.isStringArrayEmpty(parentSubAnno.rel()) ? new String[] { fname } : parentSubAnno.rel());
        }
        if (embeddedLink) {
            builder.setHref(resolvedUri);
        } else {
            for (ReflectedInfo info : fieldInfo) {
                Field currentField = info.getField();
                Object fieldVal = null;
                try {
                    fieldVal = currentField.get(obj);
                } catch (Exception e) {
                   throw new Siren4JConversionException(e);
                }
                if (ReflectionUtils.isSirenProperty(currentField.getType(), fieldVal)) {
                    // Property
                    if (!skipProperty(obj, currentField)) {
                        String propName = currentField.getName();
                        Siren4JProperty propAnno = currentField.getAnnotation(Siren4JProperty.class);

                        if (propAnno != null && StringUtils.isNotBlank(propAnno.name())) {
                            // Override field name from annotation
                            propName = propAnno.name();
                        }
                        builder.addProperty(propName, ReflectionUtils.getFieldValue(currentField, obj));
                    }
                } else {
                    // Sub Entity
                    if (!skipProperty(obj, currentField)) {
                        handleSubEntity(builder, obj, currentField, fieldInfo);
                    }
                }
            }
            handleSelfLink(builder, resolvedUri);
            handleEntityLinks(builder, obj, fieldInfo, parentField, parentObj, parentFieldInfo);
            handleEntityActions(builder, obj, fieldInfo, parentField, parentObj, parentFieldInfo);
        }
        return builder.build();
    }

    /**
     * Handles sub entities.
     * 
     * @param builder
     * assumed not <code>null</code>.
     * @param obj
     * assumed not <code>null</code>.
     * @param currentField
     * assumed not <code>null</code>.
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
     * @param rawUri
     * assumed not <code>null</code> or .
     * @param obj
     * @param fieldInfo
     * @param parentField
     * @param parentObj
     * @param parentFieldInfo
     * @return uri with tokens resolved.
     * @throws Siren4JException
     */
    private String resolveUri(String rawUri, Object obj, List<ReflectedInfo> fieldInfo, Field parentField,
        Object parentObj, List<ReflectedInfo> parentFieldInfo) throws Siren4JException {
        String resolvedUri = null;
        if (obj instanceof Resource) {
            String override = ((Resource) obj).getOverrideUri();
            if (StringUtils.isNotBlank(override)) {
                resolvedUri = override;
            }
        }
        if (resolvedUri == null) {
            // First resolve parents
            resolvedUri = ReflectionUtils.replaceFieldTokens(parentObj, rawUri, parentFieldInfo, true);
            // Now resolve others
            resolvedUri = ReflectionUtils.flattenReservedTokens(ReflectionUtils.replaceFieldTokens(obj, resolvedUri,
                fieldInfo, false));
        }
        return resolvedUri;
    }
    
    /**
     * Helper to retrieve a sub entity annotation from either the field itself or the getter.
     * If an annotation exists on both, then the field wins.
     * @param currentField assumed not <code>null</code>.
     * @param fieldInfo assumed not <code>null</code>.
     * @return the annotation if found else <code>null</code>.
     */
    private Siren4JSubEntity getSubEntityAnnotation(Field currentField, List<ReflectedInfo> fieldInfo) {
        Siren4JSubEntity result = null;
        result = currentField.getAnnotation(Siren4JSubEntity.class);
        if(result == null && fieldInfo != null) {
            ReflectedInfo info = ReflectionUtils.getFieldInfoByName(fieldInfo, currentField.getName());
            if(info != null && info.getGetter() != null) {
                result = info.getGetter().getAnnotation(Siren4JSubEntity.class);
            }
        }
        return result;
    }

    /**
     * Add the self link to the entity.
     * 
     * @param builder
     * assumed not <code>null</code>.
     * @param resolvedUri
     * the token resolved uri. Assumed not blank.
     */
    private void handleSelfLink(EntityBuilder builder, String resolvedUri) {
        if(StringUtils.isBlank(resolvedUri)) {
            return;
        }
        Link link = LinkBuilder.newInstance().setRelationship(Link.RELATIONSHIP_SELF).setHref(resolvedUri).build();
        builder.addLink(link);
    }

    /**
     * Handles getting all entity links both dynamically set and via annotations and merges them together overriding with
     * the proper precedence order which is Dynamic > SubEntity > Entity. Href and uri's are resolved with the correct data
     * bound in.
     * 
     * @param builder
     * assumed not <code>null</code>.
     * @param obj
     * assumed not <code>null</code>.
     * @param fieldInfo
     * assumed not <code>null</code>.
     * @param parentField
     * assumed not <code>null</code>.
     * @param parentObj
     * assumed not <code>null</code>.
     * @param parentFieldInfo
     * assumed not <code>null</code>.
     * @throws Exception
     */
    private void handleEntityLinks(EntityBuilder builder, Object obj, List<ReflectedInfo> fieldInfo, Field parentField,
        Object parentObj, List<ReflectedInfo> parentFieldInfo) throws Siren4JException {

        Class<?> clazz = obj.getClass();
        Map<String, Link> links = new HashMap<String, Link>();
        /* Caution!! Order matters when adding to the links map */

        Siren4JEntity entity = clazz.getAnnotation(Siren4JEntity.class);
        if (entity != null && ArrayUtils.isNotEmpty(entity.links())) {
            for (Siren4JLink l : entity.links()) {
                links.put(ArrayUtils.toString(l.rel()), annotationToLink(l));
            }
        }
        if (parentField != null) {
            Siren4JSubEntity subentity = parentField.getAnnotation(Siren4JSubEntity.class);
            if (subentity != null && ArrayUtils.isNotEmpty(subentity.links())) {
                for (Siren4JLink l : subentity.links()) {
                    links.put(ArrayUtils.toString(l.rel()), annotationToLink(l));
                }
            }
        }

        Collection<Link> resourceLinks = obj instanceof Resource ? ((Resource) obj).getEntityLinks() : null;
        if (resourceLinks != null) {
            for (Link l : resourceLinks) {
                links.put(ArrayUtils.toString(l.getRel()), l);
            }
        }
        for (Link l : links.values()) {
            l.setHref(resolveUri(l.getHref(), obj, fieldInfo, parentField, parentObj, parentFieldInfo));
            builder.addLink(l);
        }

    }

    /**
     * Handles getting all entity actions both dynamically set and via annotations and merges them together overriding with
     * the proper precedence order which is Dynamic > SubEntity > Entity. Href and uri's are resolved with the correct data
     * bound in.
     * 
     * @param builder
     * @param obj
     * @param fieldInfo
     * @param parentField
     * @param parentObj
     * @param parentFieldInfo
     * @throws Exception
     */
    private void handleEntityActions(EntityBuilder builder, Object obj, List<ReflectedInfo> fieldInfo, Field parentField,
        Object parentObj, List<ReflectedInfo> parentFieldInfo) throws Siren4JException {
        Class<?> clazz = obj.getClass();
        Map<String, Action> actions = new HashMap<String, Action>();
        /* Caution!! Order matters when adding to the actions map */

        Siren4JEntity entity = clazz.getAnnotation(Siren4JEntity.class);
        if (entity != null && ArrayUtils.isNotEmpty(entity.actions())) {
            for (Siren4JAction a : entity.actions()) {
                actions.put(a.name(), annotationToAction(a));
            }
        }
        if (parentField != null) {
            Siren4JSubEntity subentity = parentField.getAnnotation(Siren4JSubEntity.class);
            if (subentity != null && ArrayUtils.isNotEmpty(subentity.actions())) {
                for (Siren4JAction a : subentity.actions()) {
                    actions.put(a.name(), annotationToAction(a));
                }
            }
        }

        Collection<Action> resourceLinks = obj instanceof Resource ? ((Resource) obj).getEntityActions() : null;
        if (resourceLinks != null) {
            for (Action a : resourceLinks) {
                actions.put(a.getName(), a);
            }
        }
        for (Action a : actions.values()) {
            a.setHref(resolveUri(a.getHref(), obj, fieldInfo, parentField, parentObj, parentFieldInfo));
            builder.addAction(a);
        }
    }

    /**
     * Convert a link annotation to an actual link. The href will not be resolved in the instantiated link, this will need
     * to be post processed.
     * 
     * @param linkAnno
     * assumed not <code>null</code>.
     * @return new link, never <code>null</code>.
     */
    private Link annotationToLink(Siren4JLink linkAnno) {
        return LinkBuilder.newInstance().setRelationship(linkAnno.rel()).setHref(linkAnno.href()).build();
    }

    /**
     * Convert an action annotation to an actual action. The href will not be resolved in the instantiated action, this will
     * need to be post processed.
     * 
     * @param actionAnno
     * assumed not <code>null</code>.
     * @return new action, never <code>null</code>.
     */
    private Action annotationToAction(Siren4JAction actionAnno) {
        ActionBuilder builder = ActionBuilder.newInstance();
        builder.setName(actionAnno.name()).setHref(actionAnno.href()).setMethod(actionAnno.method());
        if (ArrayUtils.isNotEmpty(actionAnno.actionClass())) {
            builder.setActionClass(actionAnno.actionClass());
        }
        if (StringUtils.isNotBlank(actionAnno.title())) {
            builder.setTitle(actionAnno.title());
        }
        if (StringUtils.isNotBlank(actionAnno.type())) {
            builder.setType(actionAnno.type());
        }
        if (ArrayUtils.isNotEmpty(actionAnno.fields())) {
            for (Siren4JActionField f : actionAnno.fields()) {
                builder.addField(annotationToField(f));
            }
        }
        return builder.build();
    }

    /**
     * Convert a field annotation to an actual field.
     * 
     * @param fieldAnno
     * assumed not <code>null</code>.
     * @return new field, never <code>null</code>.
     */
    private com.google.code.siren4j.component.Field annotationToField(Siren4JActionField fieldAnno) {
        FieldBuilder builder = FieldBuilder.newInstance();
        builder.setName(fieldAnno.name());
        if (fieldAnno.max() > -1) {
            builder.setMax(fieldAnno.max());
        }
        if (fieldAnno.min() > -1) {
            builder.setMin(fieldAnno.min());
        }
        if (fieldAnno.maxLength() > -1) {
            builder.setMaxLength(fieldAnno.maxLength());
        }
        if (fieldAnno.step() > -1) {
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
            builder.setValue(fieldAnno.value());
        }
        return builder.build();
    }

    /**
     * Determine if the property or entity should be skipped based on any existing include policy. The TYPE annotation is
     * checked first and then the field annotation, the field annotation takes precedence.
     * 
     * @param obj
     * assumed not <code>null</code>.
     * @param field
     * assumed not <code>null</code>.
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
     * @param clazz
     * @return
     */
    public String[] getEntityClass(Object obj, String name) {
        Class<?> clazz = obj.getClass();
        List<String> entityClass = new ArrayList<String>();
        entityClass.add(StringUtils.defaultString(name, clazz.getName()));
        if (obj instanceof CollectionResource) {
            entityClass.add("collection");
        }

        return entityClass.toArray(new String[] {});
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

}
