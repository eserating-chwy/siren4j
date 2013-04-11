package com.google.code.siren4j.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.google.code.siren4j.converter.ReflectedInfo;
import com.google.code.siren4j.converter.ReflectingConverter;

public class ReflectionUtils {
	
	
	public static final String GETTER_PREFIX = "get";
	public static final String GETTER_PREFIX_BOOLEAN = "is";
	public static final String SETTER_PREFIX = "set";
	
	@SuppressWarnings("rawtypes")
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
	
	private ReflectionUtils(){
		
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
	 * Retrieve all fields deemed as Exposed, i.e. they are public or have a
	 * public accessor method or are marked by an annotation to be exposed.
	 * 
	 * @param obj
	 *            cannot be <code>null</code>.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<ReflectedInfo> getExposedFieldInfo(Class clazz) {
		List<ReflectedInfo> exposed = new ArrayList<ReflectedInfo>();
		for (Method m : clazz.getMethods()) {
			if (ReflectionUtils.isGetter(m) && !ReflectingConverter.isIgnored(m)) {
				Field f = getGetterField(m);
				if (f != null && !ReflectingConverter.isIgnored(f)) {
					f.setAccessible(true);
					exposed.add(new ReflectedInfo(f, m, ReflectionUtils.getSetter(clazz, f)));
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
	@SuppressWarnings("rawtypes")
	public static Method getSetter(Class clazz, Field f) {
		Method setter = null;
		for (Method m : clazz.getMethods()) {
			if (ReflectionUtils.isSetter(m)
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
	 * Replaces field tokens with the actual value in the fields. The field types must be
	 * simple property types
	 * @param str
	 * @param fields
	 * @param subMode
	 * @param obj
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static String replaceFieldTokens(Object obj, String str, List<Field> fields, boolean subMode) throws IllegalArgumentException, IllegalAccessException {
	    Map<String, Field> index = new HashMap<String, Field>();
	    for(Field f : fields) {
		index.put(f.getName(), f);
	    }
	    for(String key : ReflectionUtils.getTokenKeys(str, null, null)) {
		if(!subMode || (subMode && key.startsWith("this."))) {
		    String fieldname = key.startsWith("this.") ? key.substring(5) : key;
		    if(index.containsKey(fieldname)) {
			Field f = index.get(fieldname);
			if(ArrayUtils.contains(propertyTypes, f.getType())) {
			    str = str.replaceAll("\\{" + key + "\\}", "" + f.get(obj));
			}
		    }
		}
	    }
	    return str;
	    
	}

	/**
	 * Retrieve all token keys from a string. A token is found by its
	 * its start and end delimiters which are open and close curly braces by default.
	 * 
	 * @param str the string to parse, may be <code>null</code> or empty.
	 * @param startDelim may be <code>null</code>, defaults to '{'.
	 * @param startDelim may be <code>null</code>, defaults to '}'.
	 * @return the set of unique token keys. Never <code>null</code>.May be empty.
	 */
	public static Set<String> getTokenKeys(String str, String startDelim, String endDelim) {
	    
		Set<String> results = new HashSet<String>();
		String sDelim = StringUtils.defaultString(startDelim, "{");
		String eDelim = StringUtils.defaultString(endDelim, "}");
		if(StringUtils.isBlank(str)) {
	    	return results;
	    }
		int start = -1;
	    int end = 0;
	    
	    do {
		start = str.indexOf(sDelim, end);
		if(start != -1) {
		    end = str.indexOf(eDelim, start);
		    if(end != -1) {
			results.add(str.substring(start + sDelim.length(), end));			
		    }
		}
	    } while (start != -1 && end != -1);
	    return results;
	}

}
