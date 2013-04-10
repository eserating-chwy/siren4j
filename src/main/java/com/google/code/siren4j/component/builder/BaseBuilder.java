/*******************************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Erik R Serating
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *********************************************************************************************/
package com.google.code.siren4j.component.builder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseBuilder<T> {

    protected List<Step> steps = new ArrayList<Step>();

    /**
     * Iterates through all of the build steps and uses reflection call the
     * specified method on either the builder itself or the component being
     * built. Also calls {@link #postProcess(Object)} and then
     * {@link #validate(Object)}.
     * 
     * @return the newly built component instance, never <code>null</code>.
     */
    public T build() {
	T obj = createInstance();
	try {
	    for (Step step : steps) {
		if (step.isBuilderMethodCall()) {
		    callMethod(this, step);
		} else {
		    callMethod(obj, step);
		}
	    }
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	postProcess(obj);
	validate(obj);
	return obj;
    }

    /**
     * A subclass can post process the component instance after built by the
     * base but before it is returned by {@link #build()}.
     * 
     * @param obj
     *            the component object, never <code>null</code>.
     */
    protected void postProcess(T obj) {

    }

    /**
     * Allows validation to occur on the built component object instance. This
     * is called after the object is built and after
     * {@link #postProcess(Object)} is called but before the component is
     * returned by {@link #build}.
     * 
     * @param obj
     *            the component object, never <code>null</code>.
     */
    protected void validate(T obj) {

    }

    /**
     * The subclass must implement this method and return an instance of the
     * component type specified by the return type.
     * 
     * @return never <code>null</code>.
     */
    protected abstract T createInstance();

    /**
     * Adds a builder step for this builder, upon build these steps will be
     * called in the same order they came in on.
     * 
     * @param methodName
     * @param args
     */
    protected void addStep(String methodName, Object[] args) {
	steps.add(new Step(methodName, args, false));
    }

    /**
     * Adds a builder step for this builder, upon build these steps will be
     * called in the same order they came in on.
     * 
     * @param methodName
     * @param args
     * @param builderMethod
     */
    protected void addStep(String methodName, Object[] args,
	    boolean builderMethod) {
	steps.add(new Step(methodName, args, builderMethod));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void callMethod(Object obj, Step step) throws SecurityException,
	    NoSuchMethodException, IllegalArgumentException,
	    IllegalAccessException, InvocationTargetException {
	Class clazz = obj.getClass();
	Method method = clazz.getDeclaredMethod(step.getMethodName(),
		getTypes(step.getArguments()));
	method.invoke(obj, step.getArguments());	
    }

    @SuppressWarnings("rawtypes")
    private Class[] getTypes(Object[] args) {
	if (args == null || args.length == 0) {
	    return null;
	}
	List<Class> classList = new ArrayList<Class>();
	for (Object obj : args) {
	    classList.add(obj.getClass());
	}
	return classList.toArray(new Class[] {});
    }

    /**
     * Simple inner class to represent a step in the build process.
     */
    class Step {

	private String methodName;

	private Object[] arguments;

	private boolean builderMethodCall;

	Step(String methodName, Object[] arguments, boolean builderMethod) {
	    this.methodName = methodName;
	    this.arguments = arguments;
	    this.builderMethodCall = builderMethod;
	}

	public String getMethodName() {
	    return methodName;
	}

	public Object[] getArguments() {
	    return arguments;
	}

	public boolean isBuilderMethodCall() {
	    return builderMethodCall;
	}

    }

}
