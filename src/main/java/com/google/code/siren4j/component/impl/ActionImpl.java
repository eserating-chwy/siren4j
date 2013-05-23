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
package com.google.code.siren4j.component.impl;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.code.siren4j.component.Action;
import com.google.code.siren4j.component.Field;

@JsonInclude(Include.NON_NULL)
public class ActionImpl extends Siren4JBaseComponent implements Action {

    private String name;
    
    @JsonProperty(value = "class")
    private String[] actionClass;

    private Method method = Method.GET;

    private String href;

    private String title;

    private String type = "application/x-www-form-urlencoded";

    private List<Field> fields;

    /*
     * (non-Javadoc)
     * 
     * @see com.google.code.siren4j.component.impl.Action#getName()
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.code.siren4j.component.impl.Action#getActionClass()
     */
    public String[] getActionClass() {
        return actionClass;
    }

    public void setActionClass(String... actionClass) {
        this.actionClass = actionClass;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.code.siren4j.component.impl.Action#getMethod()
     */
    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.code.siren4j.component.impl.Action#getHref()
     */
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.code.siren4j.component.impl.Action#getTitle()
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.code.siren4j.component.impl.Action#getType()
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.code.siren4j.component.impl.Action#getFields()
     */
    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public enum Method {
        GET, PUT, POST, DELETE, PATCH
    }
    
    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(name);
        return hashCodeBuilder.toHashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ActionImpl)) {
            return false;
        }
        ActionImpl other = (ActionImpl) obj;
        EqualsBuilder equalsBuilder = new EqualsBuilder();
        equalsBuilder.append(name, other.name);
        return equalsBuilder.isEquals();
    }

}
