package com.google.code.siren4j.converter;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.google.code.siren4j.annotations.SirenEntity;
import com.google.code.siren4j.annotations.SirenPropertyIgnore;
import com.google.code.siren4j.annotations.SirenSubEntity;
import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.component.Link;
import com.google.code.siren4j.component.builder.EntityBuilder;
import com.google.code.siren4j.component.builder.LinkBuilder;
import com.google.code.siren4j.resource.CollectionResource;
import com.google.code.siren4j.resource.Resource;
import com.google.code.siren4j.util.ReflectionUtils;

public class ReflectingConverter {

    public ReflectingConverter() {

    }

    public static Entity toEntity(Object obj) throws Exception {
	return toEntity(obj, null, null, null);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Entity toEntity(Object obj, Field parentField, Object parentObj, List<ReflectedInfo> parentFieldInfo)
	    throws Exception {
	
	EntityBuilder builder = EntityBuilder.newInstance();
	Class clazz = obj.getClass();
	boolean embeddedLink = false;
	List<ReflectedInfo> fieldInfo = ReflectionUtils
		.getExposedFieldInfo(clazz);
		
	String cname = parentField != null ? parentField.getName() : null;
	String uri = "";
	
	SirenEntity entityAnno = 
		(SirenEntity)clazz.getAnnotation(SirenEntity.class);
	if(entityAnno != null) {
	    cname = StringUtils.defaultIfEmpty(entityAnno.name(), cname);
	    uri = StringUtils.defaultIfEmpty(entityAnno.uri(), uri);
	}
	    
	SirenSubEntity parentSubAnno = 
		parentField != null 
		? parentField.getAnnotation(SirenSubEntity.class) 
			: null;
		
	if(parentSubAnno != null) {
	    cname = StringUtils.defaultIfEmpty(parentSubAnno.name(), cname);
	    uri = StringUtils.defaultIfEmpty(parentSubAnno.uri(), uri);
	    embeddedLink = parentSubAnno.link();
	    if(obj instanceof Resource) {
		Boolean overrideLink = ((Resource)obj).getOverrideEmbeddedLink();
		if(overrideLink != null) {
		    embeddedLink = overrideLink.booleanValue();
		}
	    }
	}
	//Handle uri overriding or token replacement
	String resolvedUri = resolveUri(uri, obj, fieldInfo, parentField, parentObj, parentFieldInfo);
	
	builder.setEntityClass(getEntityClass(obj, cname));
	if (parentSubAnno != null) {

	    builder.setRelationship(isStringArrayEmpty(parentSubAnno.rel()) ? new String[]{cname}
		    : parentSubAnno.rel());
	}
	if(embeddedLink) {
	    builder.setHref(resolvedUri);
	} else {
	    for (ReflectedInfo info : fieldInfo) {
		Field currentField = info.getField();
		if (ArrayUtils.contains(ReflectionUtils.propertyTypes,
			currentField.getType())) {
		    // Property
		    builder.addProperty(currentField.getName(),
			    currentField.get(obj));
		} else {
		    // Sub Entity
		    handleSubEntity(builder, obj, currentField, fieldInfo);
		}
	    }
	    handleSelfLink(builder, resolvedUri);
	}
	return builder.build();
    }
    
    /**
     * Handles sub entities.
     * @param builder
     * @param obj
     * @param currentField
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    private static void handleSubEntity(EntityBuilder builder, Object obj, Field currentField, List<ReflectedInfo> fieldInfo) throws Exception{

	SirenSubEntity subAnno = currentField
		.getAnnotation(SirenSubEntity.class);
	if (subAnno != null) {
	    if (isCollection(currentField)) {
		Collection coll = (Collection) currentField.get(obj);
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
     * 
     * @param rawUri
     * @param obj
     * @param fieldInfo
     * @param parentField
     * @param parentObj
     * @param parentFieldInfo
     * @return
     * @throws Exception
     */
    private static String resolveUri(String rawUri, Object obj, List<ReflectedInfo> fieldInfo, 
	    Field parentField, Object parentObj, List<ReflectedInfo> parentFieldInfo) throws Exception{
	String resolvedUri = null;
	if(obj instanceof Resource) {
	    String override = ((Resource)obj).getOverrideUri();
	    if(StringUtils.isNotBlank(override)) {
		resolvedUri = override;
	    }
	}
	if(resolvedUri == null) {
	    //First resolve parents
	    resolvedUri = ReflectionUtils.replaceFieldTokens(parentObj, rawUri, parentFieldInfo, true);
	    //Now resolve others
	    resolvedUri = ReflectionUtils.replaceFieldTokens(obj, resolvedUri, fieldInfo, false);
	}
	return resolvedUri;
    }
    
    private static void handleSelfLink(EntityBuilder builder, String resolvedUri) {
	Link link = LinkBuilder.newInstance()
		.setRelationship(Link.RELATIONSHIP_SELF)
		.setHref(resolvedUri)
		.build();
	builder.addLink(link);
    }

    public static Object toObject(Entity entity) {
	return null;
    }

    /**
     * Determine entity class by first using the name set on the Siren entity
     * and then if not found using the actual class name, though it is
     * preferable to use the first option to not tie to a language specific
     * class.
     * 
     * @param clazz
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static String[] getEntityClass(Object obj, String name) {
	Class clazz = obj.getClass();
	List<String> entityClass = new ArrayList<String>();
	entityClass.add(StringUtils.defaultString(name, clazz.getName()));
	if (obj instanceof CollectionResource) {
	    entityClass.add("collection");
	}

	return entityClass.toArray(new String[] {});
    }

    /**
     * Determine if the field is a Collection class.
     * 
     * @param field
     * @return
     */
    public static boolean isCollection(Field field) {
	return (!field.getType().equals(CollectionResource.class)) &&
		(Collection.class.equals(field.getType())
		|| ArrayUtils.contains(field.getType().getInterfaces(),
			Collection.class));
    }

    /**
     * Determine if the field or method is ignored because the
     * <code>SirenPropertyIgnore</code> annotation is present.
     * 
     * @param obj
     * @return
     */
    public static boolean isIgnored(AccessibleObject obj) {
	return obj.isAnnotationPresent(SirenPropertyIgnore.class);
    }
    
    /**
     * Determine if the string array is empty. It is considered empty
     * if zero length or all items are blank strings;
     * @param arr
     * @return
     */
    @SuppressWarnings({ "unused", "null" })
    private static boolean isStringArrayEmpty(String[] arr) {
	boolean empty = true;
	if(arr == null) {
	    if(arr.length > 0) {
		for(String s : arr) {
		    if(StringUtils.isNotBlank(s)) {
			empty = false;
			break;
		    }
		}
	    }
	}
	return empty;
    }
    

}
