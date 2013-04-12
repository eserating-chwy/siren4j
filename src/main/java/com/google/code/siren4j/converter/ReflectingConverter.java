package com.google.code.siren4j.converter;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.google.code.siren4j.annotations.SirenAction;
import com.google.code.siren4j.annotations.SirenActionField;
import com.google.code.siren4j.annotations.SirenEntity;
import com.google.code.siren4j.annotations.SirenInclude;
import com.google.code.siren4j.annotations.SirenInclude.Include;
import com.google.code.siren4j.annotations.SirenLink;
import com.google.code.siren4j.annotations.SirenPropertyIgnore;
import com.google.code.siren4j.annotations.SirenSubEntity;
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
	    embeddedLink = parentSubAnno.embeddedLink();
			if (obj instanceof Resource) {
				Boolean overrideLink = ((Resource) obj)
						.getOverrideEmbeddedLink();
				if (overrideLink != null) {
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
			if(!skipProperty(obj, currentField)) {
		       builder.addProperty(currentField.getName(),
			       currentField.get(obj));
			}
		} else {
		    // Sub Entity
			if(!skipProperty(obj, currentField)) {
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
     * @param builder
     * @param obj
     * @param currentField
     * @throws Exception
     */
	private static void handleSubEntity(EntityBuilder builder, Object obj,
			Field currentField, List<ReflectedInfo> fieldInfo) throws Exception {

		SirenSubEntity subAnno = currentField
				.getAnnotation(SirenSubEntity.class);
		if (subAnno != null) {
			if (isCollection(obj, currentField)) {
				Collection<?> coll = (Collection<?>) currentField.get(obj);
				if (coll != null) {
					for (Object o : coll) {
						builder.addSubEntity(toEntity(o, currentField, obj,
								fieldInfo));
					}
				}
			} else {
				Object subObj = currentField.get(obj);
				if (subObj != null) {
					builder.addSubEntity(toEntity(subObj, currentField, obj,
							fieldInfo));
				}
			}
		}
	}
    
    /**
     * Resolves the raw uri by replacing field tokens with the actual data.
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
	    resolvedUri = ReflectionUtils.flattenReservedTokens(
	    		ReflectionUtils.replaceFieldTokens(obj, resolvedUri, fieldInfo, false));
	}
	return resolvedUri;
    }
    
    /**
     * Add the self link to the entity.
     * @param builder assumed not <code>null</code>.
     * @param resolvedUri the token resolved uri. Assumed not blank.
     */
    private static void handleSelfLink(EntityBuilder builder, String resolvedUri) {
	Link link = LinkBuilder.newInstance()
		.setRelationship(Link.RELATIONSHIP_SELF)
		.setHref(resolvedUri)
		.build();
	builder.addLink(link);
    }
    
    /**
     * Handles getting all entity links both dynamically set and via annotations and merges them together overriding
     * with the proper precedence order which is Dynamic > SubEntity > Entity. Href and uri's are resolved with
     * the correct data bound in.
     * @param builder
     * @param obj
     * @param fieldInfo
     * @param parentField
     * @param parentObj
     * @param parentFieldInfo
     * @throws Exception
     */
	private static void handleEntityLinks(EntityBuilder builder, Object obj,
			List<ReflectedInfo> fieldInfo, Field parentField, Object parentObj,
			List<ReflectedInfo> parentFieldInfo) throws Exception {
		
		Class<?> clazz = obj.getClass();
		Map<String[], Link> links = new HashMap<String[], Link>();
		/* Caution!! Order matters when adding to the links map */
		
		SirenEntity entity = clazz.getAnnotation(SirenEntity.class);
		if(entity != null && ArrayUtils.isNotEmpty(entity.links())) {
			for(SirenLink l : entity.links()) {
				links.put(l.rel(), annotationToLink(l));
			}
		}
		SirenSubEntity subentity = clazz.getAnnotation(SirenSubEntity.class);
		if(subentity != null && ArrayUtils.isNotEmpty(subentity.links())) {
			for(SirenLink l : subentity.links()) {
				links.put(l.rel(), annotationToLink(l));
			}
		}
		Collection<Link> resourceLinks = obj instanceof Resource ? ((Resource) obj)
				.getEntityLinks() : null;
		if (resourceLinks != null) {
			for (Link l : resourceLinks) {
				links.put(l.getRel(), l);
			}
		}
		for(Link l : links.values()) {
			l.setHref(resolveUri(l.getHref(), obj, fieldInfo, parentField, parentObj, parentFieldInfo));
			builder.addLink(l);
		}
				
	}
    
    private static void handleEntityActions(EntityBuilder builder, Object obj,
    		List<ReflectedInfo> fieldInfo, Field parentField, Object parentObj, List<ReflectedInfo> parentFieldInfo) throws Exception {
    	
    }
    
    /**
     * Convert a link annotation to an actual link. The href will not be resolved
     * in the instantiated link, this will need to be post processed.
     * @param linkAnno assumed not <code>null</code>.
     * @return new link, never <code>null</code>.
     */
    private static Link annotationToLink(SirenLink linkAnno) {
    	return LinkBuilder.newInstance()
    			.setRelationship(linkAnno.rel())
    			.setHref(linkAnno.href())
    			.build();
    }
    
    /**
     * Convert an action annotation to an actual action. The href will not be resolved
     * in the instantiated action, this will need to be post processed.
     * @param actionAnno assumed not <code>null</code>.
     * @return new action, never <code>null</code>.
     */
    private static Action annotationToAction(SirenAction actionAnno) {
    	ActionBuilder builder = ActionBuilder.newInstance();
    	builder.setName(actionAnno.name())
    	    .setHref(actionAnno.href())
    	    .setMethod(actionAnno.method());
    	if(ArrayUtils.isNotEmpty(actionAnno.actionClass())) {
    		builder.setActionClass(actionAnno.actionClass());
    	}
    	if(StringUtils.isNotBlank(actionAnno.title())){
    		builder.setTitle(actionAnno.title());
    	}
    	if(StringUtils.isNotBlank(actionAnno.type())) {
    		builder.setType(actionAnno.type());
    	}
    	if(ArrayUtils.isNotEmpty(actionAnno.fields())) {
    		for(SirenActionField f : actionAnno.fields()) {
    			builder.addField(annotationToField(f));
    		}
    	}
    	return builder.build();
    }
    
    /**
     * Convert a field annotation to an actual field. 
     * @param fieldAnno assumed not <code>null</code>.
     * @return new field, never <code>null</code>.
     */
    private static com.google.code.siren4j.component.Field annotationToField(SirenActionField fieldAnno) {
    	FieldBuilder builder = FieldBuilder.newInstance();
    	builder.setName(fieldAnno.name());
    	if(fieldAnno.max() > -1) {
    	   builder.setMax(fieldAnno.max());
    	}
    	if(fieldAnno.min() > -1) {
    	   builder.setMin(fieldAnno.min());
    	}
    	if(fieldAnno.maxLength() > -1) {
    	   builder.setMaxLength(fieldAnno.maxLength());
    	}
    	if(fieldAnno.step() > -1) {
    	   builder.setStep(fieldAnno.step());
    	}
    	if(fieldAnno.required()) {
    		builder.setRequired(true);
    	}
    	if(StringUtils.isNotBlank(fieldAnno.pattern())) {
    		builder.setPattern(fieldAnno.pattern());
    	}
    	if(StringUtils.isNotBlank(fieldAnno.type())) {
    		builder.setType(FieldType.valueOf(fieldAnno.type()));
    	}
    	if(StringUtils.isNotBlank(fieldAnno.value())) {
    		builder.setValue(fieldAnno.value());
    	}
    	return builder.build();
    }
    
    /**
     * Determine if the property or entity should be skipped based on any existing include policy.
     * The TYPE annotation is checked first and then the field annotation, the field annotation takes
     * precedence.
     * @param obj assumed not <code>null</code>.
     * @param field  assumed not <code>null</code>.
     * @return <code>true</code> if the property/enity should be skipped.
     * @throws Exception
     */
    private static boolean skipProperty(Object obj, Field field) throws Exception{
    	boolean skip = false;
    	Class<?> clazz = obj.getClass();
    	Include inc = Include.ALWAYS;
    	SirenInclude typeInclude = clazz.getAnnotation(SirenInclude.class);
    	if(typeInclude != null) {
    		inc = typeInclude.value();
    	}
    	SirenInclude fieldInclude = field.getAnnotation(SirenInclude.class);
    	if(fieldInclude != null) {
    		inc = fieldInclude.value();
    	}
    	Object val = field.get(obj);
    	switch(inc) {
    	   case NON_EMPTY:
    		   if(val != null) {
    			   if(String.class.equals(field.getType())) {
    				   skip = StringUtils.isBlank((String)val);
    			   } else if(CollectionResource.class.equals(field.getType())) {
    				   skip = ((CollectionResource<?>)val).isEmpty();
    			   }
    		   } else {
    			   skip = true;
    		   }
    		   break;
    	   case NON_NULL:
    		   if(val == null) {
    			   skip = true;
    		   }
    		   break;
    	   case ALWAYS:
    	}
    	return skip;
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
    public static String[] getEntityClass(Object obj, String name) {
	Class<?> clazz = obj.getClass();
	List<String> entityClass = new ArrayList<String>();
	entityClass.add(StringUtils.defaultString(name, clazz.getName()));
	if (obj instanceof CollectionResource) {
	    entityClass.add("collection");
	}

	return entityClass.toArray(new String[] {});
    }

    /**
     * Determine if the field is a Collection class and not a CollectionResource class
     * which needs special treatment.
     * 
     * @param field
     * @return
     */
    public static boolean isCollection(Object obj, Field field) throws Exception{
	    Object val = field.get(obj);
	    boolean isCollResource = false;
	    if(val != null) {
	    	isCollResource = CollectionResource.class.equals(val.getClass());
	    }
    	return (!isCollResource && !field.getType().equals(CollectionResource.class)) &&
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
    @SuppressWarnings({ "null" })
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
