package com.google.code.siren4j.converter;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.google.code.siren4j.annotations.SirenEntity;
import com.google.code.siren4j.annotations.SirenPropertyIgnore;
import com.google.code.siren4j.annotations.SirenSubEntity;
import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.component.builder.EntityBuilder;

public class ReflectingConverter {

	private static final String GETTER_PREFIX = "get";
	private static final String GETTER_PREFIX_BOOLEAN = "is";
	private static final String SETTER_PREFIX = "set";
	
	public static final Class[] propertyTypes = new Class[]{
	    int.class, Integer.class,
	    long.class, Long.class,
	    double.class, Double.class,
	    float.class, Float.class,
	    short.class, Short.class,
	    byte.class, Byte.class,
	    boolean.class, Boolean.class,
	    String.class,
	    Date.class
	};

	public ReflectingConverter() {

	}

	public static Entity toEntity(Object obj) throws Exception {
             return toEntity(obj, null);		
	}
	
	private static Entity toEntity(Object obj, Map<String, Object> meta) throws Exception{
	    EntityBuilder builder = EntityBuilder.newInstance();
		Class clazz = obj.getClass();
		List<ReflectedInfo> fieldInfo = getExposedFieldInfo(clazz);
            builder.setEntityClass(getEntityClass(clazz));
            for(ReflectedInfo info : fieldInfo) {
               Field currentField = info.getField();
               currentField.setAccessible(true); 
               if(ArrayUtils.contains(propertyTypes, currentField.getType())) {
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
	 * Retrieve all fields deemed as Exposed, i.e. they are public or have a
	 * public accessor method or are marked by an annotation to be exposed.
	 * 
	 * @param obj
	 *            cannot be <code>null</code>.
	 * @return
	 */
	public static List<ReflectedInfo> getExposedFieldInfo(Class clazz) {
		List<ReflectedInfo> exposed = new ArrayList<ReflectedInfo>();
		for (Method m : clazz.getMethods()) {
			if (isGetter(m) && !isIgnored(m)) {
				Field f = getGetterField(m);
				if (f != null && !isIgnored(f)) {
					exposed.add(new ReflectedInfo(f, m, getSetter(clazz, f)));
				}
			}
		}
		return exposed;
	}

	/**
	 * Retrieve the setter for the specified class/field if it exists.
	 * 
	 * @param clazz
	 * @param f
	 * @return
	 */
	public static Method getSetter(Class clazz, Field f) {
		Method setter = null;
		for (Method m : clazz.getMethods()) {
			if (isSetter(m)
					&& m.getName()
							.equals(SETTER_PREFIX
									+ StringUtils.capitalize(f.getName()))) {
				setter = m;
				break;
			}
		}
		return setter;
	}

	/**
	 * Determine if the method is a getter.
	 * 
	 * @param method
	 * @return
	 */
	public static boolean isGetter(Method method) {
		String name = method.getName();
		String[] splitname = StringUtils.splitByCharacterTypeCamelCase(name);
		return !method.getReturnType().equals(void.class)
				&& Modifier.isPublic(method.getModifiers())
				&& (splitname[0].equals(GETTER_PREFIX_BOOLEAN) || splitname[0]
						.equals(GETTER_PREFIX));
	}

	/**
	 * Determine if the method is a setter.
	 * 
	 * @param method
	 * @return
	 */
	public static boolean isSetter(Method method) {
		String name = method.getName();
		String[] splitname = StringUtils.splitByCharacterTypeCamelCase(name);
		return method.getReturnType().equals(void.class)
				&& Modifier.isPublic(method.getModifiers())
				&& splitname[0].equals(SETTER_PREFIX);
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
	 * Find the field for the getter method based on the get methods name. It
	 * finds the field on the declaring class.
	 * 
	 * @param method
	 *            cannot be <code>null</code>.
	 * @return the field or <code>null</code> if not found.
	 */
	@SuppressWarnings("rawtypes")
	public static Field getGetterField(Method method) {
		if (method == null) {
			throw new IllegalArgumentException("method cannot be null.");
		}
		Class clazz = method.getDeclaringClass();
		String fName = stripGetterPrefix(method.getName());
		Field field = null;
		try {
			field = clazz.getDeclaredField(fName);
		} catch (Exception ignore) {
		}
		return field;

	}

	/**
	 * Remove the getter prefix ('is', 'get') from the the method name passed
	 * in.
	 * 
	 * @param name
	 *            the method name.
	 * @return the method name without the prefix, may be <code>null</code> or
	 *         empty if the name passed in was that way.
	 */
	public static String stripGetterPrefix(String name) {
		if (StringUtils.isBlank(name)) {
			return name; // This should never happen.
		}
		String[] mName = StringUtils.splitByCharacterTypeCamelCase(name);
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < mName.length; i++) {
			sb.append(mName[i]);
		}
		return StringUtils.uncapitalize(sb.toString());
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
	
	public static String replaceFieldTokens(Object obj, String str, List<Field> fields, boolean subMode) throws IllegalArgumentException, IllegalAccessException {
	    Map<String, Field> index = new HashMap<String, Field>();
	    for(Field f : fields) {
		index.put(f.getName(), f);
	    }
	    for(String key : getTokenKeys(str)) {
		if(!subMode || (subMode && key.startsWith("this."))) {
		    String fieldname = key.startsWith("this.") ? key.substring(5) : key;
		    if(index.containsKey(fieldname)) {
			Field f = index.get(fieldname);
			if(ArrayUtils.contains(propertyTypes, f.getType())) {
			    str = str.replaceAll("{" + key + "}", "" + f.get(obj));
			}
		    }
		}
	    }
	    return str;
	    
	}
	
	public static Set<String> getTokenKeys(String str) {
	    int start = -1;
	    int end = 0;
	    Set<String> results = new HashSet<String>();
	    do {
		start = str.indexOf("{", end);
		if(start != -1) {
		    end = str.indexOf("}", start);
		    if(end != -1) {
			results.add(str.substring(start + 1, end));			
		    }
		}
	    } while (start != -1 && end != -1);
	    return results;
	}
	
	

}
