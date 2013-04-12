package com.google.code.siren4j.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SirenProperty {
   String name();
   Type type() default Type.SIMPLE; 
   
   public enum Type {
	    EMBEDDED_ENTITY,
	    ENTITY_LINK,
	    SIMPLE
	}
   
   
}
