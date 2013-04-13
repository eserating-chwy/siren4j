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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.google.code.siren4j.component.Action;
import com.google.code.siren4j.component.Field;
import com.google.code.siren4j.component.impl.ActionImpl;
import com.google.code.siren4j.component.impl.ActionImpl.Method;
import com.google.code.siren4j.component.impl.FieldImpl;
import com.google.code.siren4j.error.Siren4JBuilderValidationException;

public class ActionBuilder extends BaseBuilder<Action> {

    private List<Field> fields = new ArrayList<Field>();

    private ActionBuilder() {

    }

    public static ActionBuilder newInstance() {
        return new ActionBuilder();
    }

    public ActionBuilder setName(String name) {
        addStep("setName", new Object[] { name });
        return this;
    }

    public ActionBuilder setActionClass(String... actionClass) {
        addStep("setActionClass", new Object[] { actionClass });
        return this;
    }

    public ActionBuilder setMethod(Method method) {
        addStep("setMethod", new Object[] { method });
        return this;
    }

    public ActionBuilder setHref(String href) {
        addStep("setHref", new Object[] { href });
        return this;
    }

    public ActionBuilder setTitle(String title) {
        addStep("setTitle", new Object[] { title });
        return this;
    }

    public ActionBuilder setType(String type) {
        addStep("setType", new Object[] { type });
        return this;
    }

    public ActionBuilder addField(Field field) {
        addStep("_addField", new Object[] { field }, true);
        return this;
    }

    public ActionBuilder addFields(List<Field> fields) {
        for (Field f : fields) {
            addField(f);
        }
        return this;
    }

    protected void _addField(FieldImpl field) {
        fields.add(field);
    }

    @Override
    protected void postProcess(Action obj) {
        ActionImpl action = (ActionImpl) obj;
        if (!CollectionUtils.isEmpty(fields)) {
            action.setFields(fields);
        }
    }

    @Override
    protected void validate(Action obj) {
        String requiredMsg = "Required property.";
        if (StringUtils.isBlank(obj.getName())) {
            throw new Siren4JBuilderValidationException("name", obj.getClass(), requiredMsg);
        }
        if (obj.getHref() == null) {
            throw new Siren4JBuilderValidationException("href", obj.getClass(), requiredMsg);
        }
    }

    @Override
    protected Action createInstance() {
        return new ActionImpl();
    }

}
