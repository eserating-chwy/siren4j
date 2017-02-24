/*******************************************************************************************
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2013 Erik R Serating
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *********************************************************************************************/
package com.google.code.siren4j.util;

import com.google.code.siren4j.annotations.Siren4JProperty;
import com.google.code.siren4j.annotations.Siren4JPropertyIgnore;
import com.google.code.siren4j.converter.ReflectedInfo;
import com.google.code.siren4j.converter.ReflectedInfoBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ReflectionUtilsExposedFieldInfoTest {

    private Class testClass;
    private ReflectedInfo expectedInfo;

    public ReflectionUtilsExposedFieldInfoTest(Class testClass, ReflectedInfo expectedInfo) {
        this.testClass = testClass;
        this.expectedInfo = expectedInfo;
    }

    @Test
    public void testGetReflectedInfo() throws Exception {
        List<ReflectedInfo> result = ReflectionUtils.getExposedFieldInfo(testClass);

        if(expectedInfo == null) {
            assertEquals(result.size(), 0);
        } else {
            assertEquals(result.size(), 1);
            assertEquals(result.get(0), expectedInfo);
        }

    }


    class TestEntity {
        private String field;
    }

    private static class WithGetter {
        private String field;

        public String getField() {
            return field;
        }
    }

    private static class WithIgnoredField {
        @Siren4JPropertyIgnore
        private String field;

        public String getField() {
            return field;
        }
    }

    private static class WithIgnoredMethod {
        private String field;

        @Siren4JPropertyIgnore
        public String getField() {
            return field;
        }
    }

    private static class WithFieldOverride {
        @Siren4JProperty(name = "fieldOverride")
        private String field;

        public String getField() {
            return field;
        }
    }

    private static class WithFieldAndMethodOverride {
        @Siren4JProperty(name = "fieldOverride")
        private String field;

        @Siren4JProperty(name = "methodOverride")
        public String getField() {
            return field;
        }
    }

    private static class WithMethodOverride {
        private String field;

        @Siren4JProperty(name = "methodOverride")
        public String getField() {
            return field;
        }
    }

    private static class WithGetterNotBackedField {

        @Siren4JProperty
        public String getField() {
            return "methodField";
        }
    }

    private static class WithGetterNotBackedFieldAndOverride {

        @Siren4JProperty(name = "overrideMethodName")
        public String getField() {
            return "methodField";
        }
    }

    private static class WithMethodNotBackedField{

        @Siren4JProperty
        public String field() {
            return "calculatedField";
        }
    }

    private static class WithMethodNotBackedFieldAndOverride {

        @Siren4JProperty(name = "overrideMethodName")
        public String field() {
            return "calculatedField";
        }
    }

    @Parameterized.Parameters
    public static Collection keyInput() throws Exception {
        return Arrays.asList(new Object[][]{
                {TestEntity.class, null},
                { WithGetter.class, buildReflectedInfoFor(new WithGetter()) },
                { WithIgnoredField.class, null },
                { WithIgnoredMethod.class, null },
                { WithFieldOverride.class, buildReflectedInfoFor(new WithFieldOverride()) },
                { WithMethodOverride.class, buildReflectedInfoFor(new WithMethodOverride()) },
                { WithFieldAndMethodOverride.class, buildReflectedInfoFor(new WithFieldAndMethodOverride()) },
                { WithGetterNotBackedField.class, buildReflectedInfoFor(new WithGetterNotBackedField()) },
                { WithGetterNotBackedFieldAndOverride.class, buildReflectedInfoFor(new WithGetterNotBackedFieldAndOverride()) },
                { WithMethodNotBackedField.class, buildReflectedInfoFor(new WithMethodNotBackedField()) },
                { WithMethodNotBackedFieldAndOverride.class, buildReflectedInfoFor(new WithMethodNotBackedFieldAndOverride()) },
        });
    }

    private static ReflectedInfoBuilder reflectedInfo(String name) {
        return ReflectedInfoBuilder.aReflectedInfo().withEffectiveName(name);
    }

    private static ReflectedInfo buildReflectedInfoFor(WithMethodNotBackedFieldAndOverride obj)  throws Exception {
        return reflectedInfo("overrideMethodName").withGetter(obj.getClass().getMethod("field")).build();
    }

    private static ReflectedInfo buildReflectedInfoFor(WithMethodNotBackedField obj) throws Exception {
        return reflectedInfo("field").withGetter(obj.getClass().getMethod("field")).build();
    }

    private static ReflectedInfo buildReflectedInfoFor(WithGetterNotBackedFieldAndOverride obj) throws Exception {
        return reflectedInfo("overrideMethodName").withGetter(obj.getClass().getMethod("getField")).build();
    }

    private static ReflectedInfo buildReflectedInfoFor(WithGetterNotBackedField obj) throws Exception {
        return reflectedInfo("field").withGetter(obj.getClass().getMethod("getField")).build();
    }

    private static ReflectedInfo buildReflectedInfoFor(WithFieldAndMethodOverride obj) throws Exception {
        Class cls = obj.getClass();
        return reflectedInfo("fieldOverride").
                    withField(cls.getDeclaredField("field")).withGetter(cls.getMethod("getField")).build();
    }

    private static ReflectedInfo buildReflectedInfoFor(WithMethodOverride obj) throws Exception {
        Class cls = obj.getClass();
        return reflectedInfo("methodOverride").
                withField(cls.getDeclaredField("field")).withGetter(cls.getMethod("getField")).build();
    }

    private static ReflectedInfo buildReflectedInfoFor(WithFieldOverride obj) throws Exception {
        Class cls = obj.getClass();
        return reflectedInfo("fieldOverride").
                withField(cls.getDeclaredField("field")).withGetter(cls.getMethod("getField")).build();
    }

    private static ReflectedInfo buildReflectedInfoFor(WithGetter obj) throws Exception {
        Class cls = obj.getClass();
        return reflectedInfo("field").
                withField(cls.getDeclaredField("field")).withGetter(cls.getMethod("getField")).build();
    }


}
