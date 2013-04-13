/*******************************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Erik R Serating
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *********************************************************************************************/
package com.google.code.siren4j.converter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.google.code.siren4j.annotations.Siren4JAction;
import com.google.code.siren4j.annotations.Siren4JActionField;
import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.annotations.Siren4JInclude;
import com.google.code.siren4j.annotations.Siren4JInclude.Include;
import com.google.code.siren4j.annotations.Siren4JLink;
import com.google.code.siren4j.annotations.Siren4JSubEntity;
import com.google.code.siren4j.component.Action;
import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.component.Link;
import com.google.code.siren4j.component.builder.ActionBuilder;
import com.google.code.siren4j.component.builder.EntityBuilder;
import com.google.code.siren4j.component.builder.FieldBuilder;
import com.google.code.siren4j.component.builder.LinkBuilder;
import com.google.code.siren4j.meta.FieldType;
import com.google.code.siren4j.resource.CollectionResource;
import com.google.code.siren4j.resource.Resource;
import com.google.code.siren4j.util.ReflectionUtils;

public class ReflectingConverter {

    /**
     * The singleton instance for this class.
     */
    private static ReflectingConverter instance;

    /**
     * Private ctor to prevent direct instantiation.
     */
    private ReflectingConverter() {

    }

    /**
     * Gets the singleton instance of the converter.
     * 
     * @return the converter, never <code>null</code>.
     */
    public static ReflectingConverter getInstance() {
        if (instance == null) {
            instance = new ReflectingConverter();
        }
        return instance;
    }

    public Entity toEntity(Resource resource) throws Exception {
        return toEntity(resource, null, null, null);
    }

    public Resource toResource(Entity entity) {
        return null;
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
        throws Exception {
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

        Siren4JSubEntity parentSubAnno = parentField != null ? parentField.getAnnotation(Siren4JSubEntity.class) : null;

        if (parentSubAnno != null) {
            cname = StringUtils.defaultIfEmpty(parentSubAnno.name(), cname);
            uri = StringUtils.defaultIfEmpty(parentSubAnno.uri(), uri);
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

            builder.setRelationship(isStringArrayEmpty(parentSubAnno.rel()) ? new String[] { cname } : parentSubAnno.rel());
        }
        if (embeddedLink) {
            builder.setHref(resolvedUri);
        } else {
            for (ReflectedInfo info : fieldInfo) {
                Field currentField = info.getField();
                if (ArrayUtils.contains(ReflectionUtils.propertyTypes, currentField.getType())) {
                    // Property
                    if (!skipProperty(obj, currentField)) {
                        builder.addProperty(currentField.getName(), currentField.get(obj));
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
     * @throws Exception
     */
    private void handleSubEntity(EntityBuilder builder, Object obj, Field currentField, List<ReflectedInfo> fieldInfo)
        throws Exception {

        Siren4JSubEntity subAnno = currentField.getAnnotation(Siren4JSubEntity.class);
        if (subAnno != null) {
            if (isCollection(obj, currentField)) {
                Collection<?> coll = (Collection<?>) currentField.get(obj);
                if (coll != null) {
                    for (Object o : coll) {
                        builder.addSubEntity(toEntity(o, currentField, obj, fieldInfo));
                    }
                }
            } else {
                Object subObj = currentField.get(obj);
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
     * @throws Exception
     */
    private String resolveUri(String rawUri, Object obj, List<ReflectedInfo> fieldInfo, Field parentField,
        Object parentObj, List<ReflectedInfo> parentFieldInfo) throws Exception {
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
     * Add the self link to the entity.
     * 
     * @param builder
     * assumed not <code>null</code>.
     * @param resolvedUri
     * the token resolved uri. Assumed not blank.
     */
    private void handleSelfLink(EntityBuilder builder, String resolvedUri) {
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
        Object parentObj, List<ReflectedInfo> parentFieldInfo) throws Exception {

        Class<?> clazz = obj.getClass();
        Map<String[], Link> links = new HashMap<String[], Link>();
        /* Caution!! Order matters when adding to the links map */

        Siren4JEntity entity = clazz.getAnnotation(Siren4JEntity.class);
        if (entity != null && ArrayUtils.isNotEmpty(entity.links())) {
            for (Siren4JLink l : entity.links()) {
                links.put(l.rel(), annotationToLink(l));
            }
        }
        Siren4JSubEntity subentity = clazz.getAnnotation(Siren4JSubEntity.class);
        if (subentity != null && ArrayUtils.isNotEmpty(subentity.links())) {
            for (Siren4JLink l : subentity.links()) {
                links.put(l.rel(), annotationToLink(l));
            }
        }
        Collection<Link> resourceLinks = obj instanceof Resource ? ((Resource) obj).getEntityLinks() : null;
        if (resourceLinks != null) {
            for (Link l : resourceLinks) {
                links.put(l.getRel(), l);
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
        Object parentObj, List<ReflectedInfo> parentFieldInfo) throws Exception {
        Class<?> clazz = obj.getClass();
        Map<String, Action> actions = new HashMap<String, Action>();
        /* Caution!! Order matters when adding to the actions map */

        Siren4JEntity entity = clazz.getAnnotation(Siren4JEntity.class);
        if (entity != null && ArrayUtils.isNotEmpty(entity.actions())) {
            for (Siren4JAction a : entity.actions()) {
                actions.put(a.name(), annotationToAction(a));
            }
        }
        Siren4JSubEntity subentity = clazz.getAnnotation(Siren4JSubEntity.class);
        if (subentity != null && ArrayUtils.isNotEmpty(subentity.actions())) {
            for (Siren4JAction a : subentity.actions()) {
                actions.put(a.name(), annotationToAction(a));
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
     * @throws Exception
     */
    private boolean skipProperty(Object obj, Field field) throws Exception {
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
    public boolean isCollection(Object obj, Field field) throws Exception {
        Object val = field.get(obj);
        boolean isCollResource = false;
        if (val != null) {
            isCollResource = CollectionResource.class.equals(val.getClass());
        }
        return (!isCollResource && !field.getType().equals(CollectionResource.class))
            && (Collection.class.equals(field.getType()) || ArrayUtils.contains(field.getType().getInterfaces(),
                Collection.class));
    }

    /**
     * Determine if the string array is empty. It is considered empty if zero length or all items are blank strings;
     * 
     * @param arr
     * @return
     */
    @SuppressWarnings({ "null" })
    private boolean isStringArrayEmpty(String[] arr) {
        boolean empty = true;
        if (arr == null) {
            if (arr.length > 0) {
                for (String s : arr) {
                    if (StringUtils.isNotBlank(s)) {
                        empty = false;
                        break;
                    }
                }
            }
        }
        return empty;
    }

}
