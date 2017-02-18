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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectedInfoBuilder {
    private Field field;
    private Method getter;
    private Method setter;
    private String effectiveName;

    private ReflectedInfoBuilder() {
    }

    public static ReflectedInfoBuilder aReflectedInfo() {
        return new ReflectedInfoBuilder();
    }

    public ReflectedInfoBuilder withField(Field field) {
        this.field = field;
        return this;
    }

    public ReflectedInfoBuilder withGetter(Method getter) {
        this.getter = getter;
        return this;
    }

    public ReflectedInfoBuilder withSetter(Method setter) {
        this.setter = setter;
        return this;
    }

    public ReflectedInfoBuilder withEffectiveName(String effectiveName) {
        this.effectiveName = effectiveName;
        return this;
    }

    public ReflectedInfoBuilder but() {
        return aReflectedInfo().withField(field).withGetter(getter).withSetter(setter).withEffectiveName(effectiveName);
    }

    public ReflectedInfo build() {
        ReflectedInfo reflectedInfo = new ReflectedInfo(field, getter, setter, effectiveName);
        return reflectedInfo;
    }
}
