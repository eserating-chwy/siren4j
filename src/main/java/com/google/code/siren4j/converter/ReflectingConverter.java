package com.google.code.siren4j.converter;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.google.code.siren4j.annotations.SirenEntity;
import com.google.code.siren4j.annotations.SirenPropertyIgnore;
import com.google.code.siren4j.annotations.SirenSubEntity;
import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.component.builder.EntityBuilder;
import com.google.code.siren4j.util.ReflectionUtils;

public class ReflectingConverter {

	public ReflectingConverter() {

	}

	public static Entity toEntity(Object obj) throws Exception {
             return toEntity(obj, null);		
	}
	
	@SuppressWarnings("rawtypes")
	private static Entity toEntity(Object obj, Field parentField) throws Exception{
	    EntityBuilder builder = EntityBuilder.newInstance();
		Class clazz = obj.getClass();
		List<ReflectedInfo> fieldInfo = ReflectionUtils.getExposedFieldInfo(clazz);
            builder.setEntityClass(getEntityClass(clazz));
            for(ReflectedInfo info : fieldInfo) {
               Field currentField = info.getField();
               if(ArrayUtils.contains(ReflectionUtils.propertyTypes, currentField.getType())) {
                   // Property
                   builder.addProperty(currentField.getName(), currentField.get(obj));
               } else {
                   // Entity
                   SirenSubEntity subEntityAnno = 
            	       currentField.getAnnotation(SirenSubEntity.class);
                   if(subEntityAnno != null) {
            	       if(isCollection(currentField)) {
            		   
            	       } else {
            		   
            	       }
                   }
               }
            }
		return builder.build();
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
	public static String[] getEntityClass(Class clazz) {
		List<String> entityClass = new ArrayList<String>();
		SirenEntity sirenEntity = (SirenEntity) clazz
				.getAnnotation(SirenEntity.class);

		entityClass.add(sirenEntity != null
				&& StringUtils.isNotBlank(sirenEntity.name()) ? sirenEntity
				.name() : clazz.getName());
		if (sirenEntity != null && sirenEntity.collection()) {
			entityClass.add("collection");
		}
		return entityClass.toArray(new String[] {});
	}

	/**
	 * Determine if the field is a Collection class.
	 * @param field
	 * @return
	 */
	public static boolean isCollection(Field field) {
	    return ArrayUtils.contains(field.getType().getInterfaces(), Collection.class);
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
	
	

}
