package com.google.code.siren4j.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SirenInclude {
    Include value() default Include.ALWAYS;    
    
    
    public enum Include {
    	/**
         * Value that indicates that property is to be always included,
         * independent of value of the property.
         */
    	ALWAYS,
    	/**
         * Value that indicates that only properties with non-null
         * values are to be included.
         */
    	NON_NULL,
    	/**
         * Value that indicates that only properties that have values
         * that values that are null or what is considered empty are
         * not to be included.
         */
    	NON_EMPTY
    }
}
