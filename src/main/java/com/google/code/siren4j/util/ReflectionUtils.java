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
package com.google.code.siren4j.util;

import java.lang.reflect.AccessibleObject;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.google.code.siren4j.annotations.Siren4JPropertyIgnore;
import com.google.code.siren4j.converter.ReflectedInfo;
import com.google.code.siren4j.error.Siren4JException;
import com.google.code.siren4j.error.Siren4JRuntimeException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class ReflectionUtils {

    public static final String GETTER_PREFIX = "get";
    public static final String GETTER_PREFIX_BOOLEAN = "is";
    public static final String SETTER_PREFIX = "set";
    /**
     * Field info is cached as it is an expensive operation but the values don't actually change
     * until code is changed and recompiled.
     */
    public static Cache<Class<?>, List<ReflectedInfo>> fieldInfoCache = 
        CacheBuilder.newBuilder().maximumSize(1000).build();

    public static final Class<?>[] propertyTypes = new Class<?>[] { int.class, Integer.class, long.class, Long.class,
        double.class, Double.class, float.class, Float.class, short.class, Short.class, byte.class, Byte.class,
        boolean.class, Boolean.class, String.class, Date.class };

    private ReflectionUtils() {

    }

    /**
     * Remove the getter prefix ('is', 'get') from the the method name passed in.
     * 
     * @param name
     * the method name.
     * @return the method name without the prefix, may be <code>null</code> or empty if the name passed in was that way.
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
     * Find the field for the getter method based on the get methods name. It finds the field on the declaring class.
     * 
     * @param method
     * cannot be <code>null</code>.
     * @return the field or <code>null</code> if not found.
     */
    public static Field getGetterField(Method method) {
        if (method == null) {
            throw new IllegalArgumentException("method cannot be null.");
        }
        Class<?> clazz = method.getDeclaringClass();
        String fName = stripGetterPrefix(method.getName());
        Field field = null;
        try {
            field = clazz.getDeclaredField(fName);
        } catch (Exception ignore) {
        }
        return field;

    }

    /**
     * Retrieve all fields deemed as Exposed, i.e. they are public or have a public accessor method or are marked by an
     * annotation to be exposed.
     * 
     * @param obj cannot be <code>null</code>.
     * @return
     */
    public static List<ReflectedInfo> getExposedFieldInfo(final Class<?> clazz) {
        List<ReflectedInfo> results = null;
        try {
            results = fieldInfoCache.get(clazz, new Callable<List<ReflectedInfo>>() {
                /**
                 * This method is called if the value is not found in the cache. This
                 * is where the real reflection work is done.
                 */
                public List<ReflectedInfo> call() throws Exception {
                    List<ReflectedInfo> exposed = new ArrayList<ReflectedInfo>();
                    for (Method m : clazz.getMethods()) {
                        if (ReflectionUtils.isGetter(m) && !isIgnored(m)) {
                            Field f = getGetterField(m);
                            if (f != null && !isIgnored(f)) {
                                f.setAccessible(true);
                                exposed.add(new ReflectedInfo(f, m, ReflectionUtils.getSetter(clazz, f)));
                            }
                        }
                    }
                    return exposed;
                }
                
            });
        } catch (ExecutionException e) {
           throw new Siren4JRuntimeException(e);     
        }
        return results;        
        
    }

    /**
     * Retrieve the setter for the specified class/field if it exists.
     * 
     * @param clazz
     * @param f
     * @return
     */
    public static Method getSetter(Class<?> clazz, Field f) {
        Method setter = null;
        for (Method m : clazz.getMethods()) {
            if (ReflectionUtils.isSetter(m) && m.getName().equals(SETTER_PREFIX + StringUtils.capitalize(f.getName()))) {
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
        return !method.getReturnType().equals(void.class) && Modifier.isPublic(method.getModifiers())
            && (splitname[0].equals(GETTER_PREFIX_BOOLEAN) || splitname[0].equals(GETTER_PREFIX));
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
        return method.getReturnType().equals(void.class) && Modifier.isPublic(method.getModifiers())
            && splitname[0].equals(SETTER_PREFIX);
    }

    /**
     * Replaces field tokens with the actual value in the fields. The field types must be simple property types
     * 
     * @param str
     * @param fields
     * info
     * @param parentMode
     * @param obj
     * @return
     * @throws Siren4JException
     */
    public static String replaceFieldTokens(Object obj, String str, List<ReflectedInfo> fields, boolean parentMode)
        throws Siren4JException {
        Map<String, Field> index = new HashMap<String, Field>();
        if (StringUtils.isBlank(str)) {
            return str;
        }
        if (fields != null) {
            for (ReflectedInfo info : fields) {
                Field f = info.getField();
                if (f != null) {
                    index.put(f.getName(), f);
                }
            }
        }
        try {
			for (String key : ReflectionUtils.getTokenKeys(str)) {
			    if ((!parentMode && !key.startsWith("parent.")) || (parentMode && key.startsWith("parent."))) {
			        String fieldname = key.startsWith("parent.") ? key.substring(7) : key;
			        if (index.containsKey(fieldname)) {
			            Field f = index.get(fieldname);
			            if (ArrayUtils.contains(propertyTypes, f.getType())) {
			                str = str.replaceAll("\\{" + key + "\\}", "" + f.get(obj));
			            }
			        }
			    }
			}
		} catch (Exception e) {
			throw new Siren4JException(e);
		} 
        return str;

    }

    /**
     * Removes the square brackets that signify reserved from inside the tokens in the string.
     * 
     * @param str
     * @return
     */
    public static String flattenReservedTokens(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return str.replaceAll("\\{\\[", "{").replaceAll("\\]\\}", "}");
    }

    /**
     * Determine if the field or method is ignored because the <code>SirenPropertyIgnore</code> annotation is present.
     * 
     * @param obj
     * @return
     */
    public static boolean isIgnored(AccessibleObject obj) {
        return obj.isAnnotationPresent(Siren4JPropertyIgnore.class);
    }

    /**
     * Retrieve all token keys from a string. A token is found by its its start and end delimiters which are open and close
     * curly braces.
     * 
     * @param str
     * the string to parse, may be <code>null</code> or empty.
     * @return the set of unique token keys. Never <code>null</code>.May be empty.
     */
    public static Set<String> getTokenKeys(String str) {

        Set<String> results = new HashSet<String>();
        String sDelim = "{";
        String eDelim = "}";
        if (StringUtils.isBlank(str)) {
            return results;
        }
        int start = -1;
        int end = 0;

        do {
            start = str.indexOf(sDelim, end);
            if (start != -1) {
                end = str.indexOf(eDelim, start);
                if (end != -1) {
                    results.add(str.substring(start + sDelim.length(), end));
                }
            }
        } while (start != -1 && end != -1);
        return results;
    }
    
    /**
     * Convenience method to retrieve the field value for the specified object wrapped to
     * catch exceptions and re throw as <code>Siren4JRuntimeException</code>.
     * @param field cannot be <code>null</code>.
     * @param obj may be <code>null</code>.
     * @return the value, may be <code>null</code>.
     */
    public static Object getFieldValue(Field field, Object obj) {
    	if(field == null) {
    		throw new IllegalArgumentException("field cannot be null.");
    	}
    	try {
			return field.get(obj);
		} catch (IllegalArgumentException e) {
			throw new Siren4JRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new Siren4JRuntimeException(e);
		}
    }

}
