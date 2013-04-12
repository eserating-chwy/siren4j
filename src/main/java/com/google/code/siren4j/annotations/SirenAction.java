package com.google.code.siren4j.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.code.siren4j.component.impl.ActionImpl.Method;


@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SirenAction {
	String name();
    String[] actionClass() default {};
    Method method() default Method.GET;
    String href();
    String title() default "";
    String type() default "";
    SirenActionField[] fields() default {};
}
