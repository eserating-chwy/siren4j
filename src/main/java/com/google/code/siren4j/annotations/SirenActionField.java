package com.google.code.siren4j.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SirenActionField {
	String name();
	String type() default "";
	String value() default "";	
	String pattern() default "";	
	boolean required() default false;
	int min() default -1;	
	int max() default -1;
	int maxLength() default -1;
	int step() default -1;
}
