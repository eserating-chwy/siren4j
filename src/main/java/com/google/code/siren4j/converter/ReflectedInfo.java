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
package com.google.code.siren4j.converter;

import com.google.common.base.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectedInfo {

    private final Field field;
    private final Method getter;
    private final Method setter;
    private final String effectiveName;

    public ReflectedInfo(Field field, Method getter, Method setter, String effectiveName) {
        this.field = field;
        this.getter = getter;
        this.setter = setter;
        this.effectiveName = effectiveName;
    }

    public Field getField() {
        return field;
    }

    public Method getGetter() {
        return getter;
    }

    public Method getSetter() {
        return setter;
    }

    public String getEffectiveName() {
        return effectiveName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReflectedInfo)) return false;
        ReflectedInfo that = (ReflectedInfo) o;
        return Objects.equal(field, that.field) &&
                Objects.equal(getter, that.getter) &&
                Objects.equal(setter, that.setter) &&
                Objects.equal(effectiveName, that.effectiveName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(field, getter, setter, effectiveName);
    }

    @Override
    public String toString() {
    	return new ToStringBuilder(this)
    			.append(effectiveName)
    			.toString();
    }
}