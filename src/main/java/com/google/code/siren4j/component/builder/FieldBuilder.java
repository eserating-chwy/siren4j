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

import org.apache.commons.lang.StringUtils;

import com.google.code.siren4j.component.Field;
import com.google.code.siren4j.component.impl.FieldImpl;
import com.google.code.siren4j.error.BuilderValidationException;
import com.google.code.siren4j.meta.FieldType;

public class FieldBuilder extends BaseBuilder<Field> {

	private FieldBuilder() {

	}

	public static FieldBuilder newInstance() {
		return new FieldBuilder();
	}

	public FieldBuilder setName(String name) {
		addStep("setName", new Object[] { name });
		return this;
	}

	public FieldBuilder setType(FieldType type) {
		addStep("setType", new Object[] { type });
		return this;
	}

	public FieldBuilder setValue(String value) {
		addStep("setValue", new Object[] { value });
		return this;
	}
	
	public FieldBuilder setPattern(String pattern) {
		addStep("setPattern", new Object[] {pattern});
		return this;
	}
	
	public FieldBuilder setRequired(boolean required) {
		addStep("setRequired", new Object[] {required}, new Class[]{boolean.class});
		return this;
	}
	
	public FieldBuilder setMax(int max) {
		addStep("setMax", new Object[] {max}, new Class[]{int.class});
		return this;
	}
	
	public FieldBuilder setMin(int min) {
		addStep("setMin", new Object[] {min}, new Class[]{int.class});
		return this;
	}
	
	public FieldBuilder setMaxLength(int maxLength) {
		addStep("setMaxLength", new Object[] {maxLength}, new Class[]{int.class});
		return this;
	}
	
	public FieldBuilder setStep(int step) {
		addStep("setStep", new Object[] {step}, new Class[]{int.class});
		return this;
	}

	@Override
	protected void validate(Field obj) {
		String requiredMsg = "Required property.";
		if (StringUtils.isBlank(obj.getName())) {
			throw new BuilderValidationException("name", obj.getClass(),
					requiredMsg);
		}		
	}

	@Override
	protected Field createInstance() {
		return new FieldImpl();
	}

}
