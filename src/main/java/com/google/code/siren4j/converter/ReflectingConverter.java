package com.google.code.siren4j.converter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.google.code.siren4j.component.Entity;

public class ReflectingConverter {

    public ReflectingConverter() {
	
    }
    
    public static Entity toEntity(Object obj) throws Exception{
	
	Class clazz = obj.getClass();
	Field[] fields = clazz.getDeclaredFields();
	for(Field f : fields) {
	    f.setAccessible(true);
	    System.out.println(f.getName());
	    System.out.println("Type: " + f.getType().getName());
	    System.out.println(f.get(obj));
	}
	
	return null;
    }
    
    public static Object toObject(Entity entity) {
	return null;
    }
    
    /**
     * Retrieve all fields deemed as Exposed, i.e. they
     * are public or have a public accessor method or are marked by an
     * annotation to be exposed.
     * @param obj cannot be <code>null</code>.
     * @return
     */
    public static List<Field> getExposedFields(Object obj) {
	if(obj == null) {
	    throw new IllegalArgumentException("obj cannot be null.");
	}
	Class clazz = obj.getClass();
	List<Field> exposed = new ArrayList<Field>();
	Field[] fields = clazz.getDeclaredFields();
	Method[] methods = clazz.getDeclaredMethods();
	Set<String> methodNames = new HashSet<String>();
	for(Method m : methods) {
	    if(Modifier.isPublic(m.getModifiers())) {
		String name = m.getName();
		String[] splitname = StringUtils.splitByCharacterTypeCamelCase(name);
		if(splitname[0].equals("is") || splitname[0].equals("get")) {
		    methodNames.add(name);
		}		
	    }
	}
	for(Field f : fields) {
	    String prefix = f.getType().equals(boolean.class) || f.getType().equals(Boolean.class)? "is" : "get";
	   
	    if(Modifier.isPublic(f.getModifiers())) {
		exposed.add(f);
		continue;
	    } else if(methodNames.contains(prefix + StringUtils.capitalize(f.getName()))) {
		exposed.add(f);
		continue;
	    }
	}
	return exposed;
    }

}
