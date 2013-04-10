package com.google.code.siren4j.converter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectedInfo {
	private Field field;
	private Method getter;
	private Method setter;
	

	public ReflectedInfo(Field field, Method getter, Method setter) {
		super();
		this.field = field;
		this.getter = getter;
		this.setter = setter;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Method getGetter() {
		return getter;
	}

	public void setGetter(Method getter) {
		this.getter = getter;
	}

	public Method getSetter() {
		return setter;
	}

	public void setSetter(Method setter) {
		this.setter = setter;
	}
}