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


import com.google.code.siren4j.component.Link;
import com.google.code.siren4j.component.impl.LinkImpl;
import com.google.code.siren4j.error.Siren4JBuilderValidationException;
import com.google.code.siren4j.util.ComponentUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class LinkBuilder extends BaseBuilder<Link> {

    private LinkBuilder() {

    }

    /**
     * Retrieve a new instance of an <code>LinkBuilder</code>.
     * @return new instance, never <code>null</code>.
     */
    public static LinkBuilder newInstance() {
        return new LinkBuilder();
    }

    /**
     * Retrieve a new instance of an <code>LinkBuilder</code>. Added to allow this method to be used with
     * static method importing.
     * @since 1.1.0
     * @return new instance, never <code>null</code>.
     */
    public static LinkBuilder createLinkBuilder() {
        return newInstance();
    }

    /**
     * Set the component class of the entity to be built. This method can be called many times
     * but only the value of the last call is used in the built entity. This is an optional property as specified
     * by the Siren specification.
     * @param componentClass may be <code>null</code> or empty.
     * @return <code>this</code> builder, never <code>null</code>.
     */
    public LinkBuilder setComponentClass(String... componentClass) {
        addStep("setComponentClass", new Object[] { componentClass }, new Class<?>[] {String[].class});
        return this;
    }

    public LinkBuilder setRelationship(String... rel) {
        if(ComponentUtils.isStringArrayEmpty(rel)) {
            throw new IllegalArgumentException("rel cannot be null or empty. Required property.");
        }
        addStep("setRel", new Object[] { rel }, new Class[] { String[].class });
        return this;
    }

    public LinkBuilder setHref(String href) {
        if(StringUtils.isBlank(href)) {
            throw new IllegalArgumentException("href cannot be null or empty. Required property.");
        }
        addStep("setHref", new Object[] { href });
        return this;
    }
    
    public LinkBuilder setTitle(String title) {
        addStep("setTitle", new Object[] { title }, new Class<?>[] {String.class});
        return this;
    }

    public LinkBuilder setType(String type) {
        addStep("setType", new Object[] { type }, new Class<?>[] {String.class});
        return this;
    }

    @Override
    protected Link createInstance() {
        return new LinkImpl();
    }

    @Override
    protected void validate(Link obj) {
        String requiredMsg = "Required property.";
        if (obj.getRel() == null || ArrayUtils.isEmpty(obj.getRel())) {
            throw new Siren4JBuilderValidationException("rel", obj.getClass(), requiredMsg);
        }
        if (obj.getHref() == null) {
            throw new Siren4JBuilderValidationException("href", obj.getClass(), requiredMsg);
        }
    }   
    

}
