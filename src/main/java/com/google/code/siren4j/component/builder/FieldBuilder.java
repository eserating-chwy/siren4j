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

import java.util.Map;
import org.apache.commons.lang.StringUtils;

import com.google.code.siren4j.component.Field;
import com.google.code.siren4j.component.impl.FieldImpl;
import com.google.code.siren4j.error.Siren4JBuilderValidationException;
import com.google.code.siren4j.meta.FieldOption;
import com.google.code.siren4j.meta.FieldType;

public class FieldBuilder extends BaseBuilder<Field> {

    private FieldBuilder() {

    }

    /**
     * Retrieve a new instance of an <code>FieldBuilder</code>.
     * @return new instance, never <code>null</code>.
     */
    public static FieldBuilder newInstance() {
        return new FieldBuilder();
    }

    /**
     * Retrieve a new instance of an <code>FieldBuilder</code>. Added to allow this method to be used with
     * static method importing.
     * @since 1.1.0
     * @return new instance, never <code>null</code>.
     */
    public static FieldBuilder createFieldBuilder() {
        return newInstance();
    }

    /**
     * Set the component class of the entity to be built. This method can be called many times
     * but only the value of the last call is used in the built entity. This is an optional property as specified
     * by the Siren specification.
     * @param componentClass may be <code>null</code> or empty.
     * @return <code>this</code> builder, never <code>null</code>.
     */
    public FieldBuilder setComponentClass(String... componentClass) {
        addStep("setComponentClass", new Object[] { componentClass }, new Class<?>[] {String[].class});
        return this;
    }

    public FieldBuilder setName(String name) {
        if(StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name cannot be null or empty.");
        }
        addStep("setName", new Object[] { name });
        return this;
    }

    public FieldBuilder setType(FieldType type) {
        addStep("setType", new Object[] { type }, new Class<?>[] {FieldType.class});
        return this;
    }
    

    public FieldBuilder addOption(FieldOption option) {
        addStep("addOption", new Object[] { option }, new Class<?>[] {FieldOption.class});
        return this;
    }

    public FieldBuilder addOption(String title, String value, boolean isDefault) {
        return addOption(new FieldOption(title, value, isDefault));
    }

    public FieldBuilder addOption(String title, String value, boolean isDefault, Map<String, String> data) {
        return addOption(new FieldOption(title, value, isDefault, data));
    }

    public FieldBuilder setOptionsURL(String url) {
        addStep("setOptionsURL", new Object[] { url }, new Class<?>[] {String.class});
        return this;
    }
	
    public FieldBuilder setTitle(String title) {
        addStep("setTitle", new Object[] { title }, new Class<?>[] {String.class});
        return this;
    }

    public FieldBuilder setValue(String value) {
        addStep("setValue", new Object[] { value }, new Class<?>[] {String.class});
        return this;
    }

    public FieldBuilder setPattern(String pattern) {
        addStep("setPattern", new Object[] { pattern }, new Class<?>[] {String.class});
        return this;
    }

    public FieldBuilder setRequired(boolean required) {
        addStep("setRequired", new Object[] { required }, new Class[] { boolean.class });
        return this;
    }

    public FieldBuilder setMax(int max) {
        addStep("setMax", new Object[] { max }, new Class[] { int.class });
        return this;
    }

    public FieldBuilder setMin(int min) {
        addStep("setMin", new Object[] { min }, new Class[] { int.class });
        return this;
    }

    public FieldBuilder setMaxLength(int maxLength) {
        addStep("setMaxLength", new Object[] { maxLength }, new Class[] { int.class });
        return this;
    }

    public FieldBuilder setStep(Integer step) {
        addStep("setStep", new Object[] { step }, new Class[] { Integer.class });
        return this;
    }
    
    public FieldBuilder setPlaceholder(String placeholder) {
        addStep("setPlaceholder", new Object[] { placeholder }, new Class<?>[] {String.class});
        return this;
    }

    public FieldBuilder setMetaData(Map<String, String> metaData) {
        addStep("setMetaData", new Object[] {metaData}, new Class[] {Map.class});
        return this;
    }

    @Override
    protected void validate(Field obj) {
        String requiredMsg = "Required property.";
        if (StringUtils.isBlank(obj.getName())) {
            throw new Siren4JBuilderValidationException("name", obj.getClass(), requiredMsg);
        }
    }

    @Override
    protected Field createInstance() {
        return new FieldImpl();
    }

}
