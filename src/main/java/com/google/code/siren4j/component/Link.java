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
package com.google.code.siren4j.component;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.code.siren4j.component.impl.LinkImpl;

/**
 * Links represent navigational transitions. In JSON Siren, links are represented as an
 * array inside the entity, such as { "links": [{ "rel": [ "self" ], "href": "http://api.x.io/orders/42"}] }
 */
@JsonDeserialize(as = LinkImpl.class)
public interface Link {

    public static final String RELATIONSHIP_FIRST = "first";
    public static final String RELATIONSHIP_LAST = "last";
    public static final String RELATIONSHIP_SELF = "self";
    public static final String RELATIONSHIP_NEXT = "next";
    public static final String RELATIONSHIP_PREVIOUS = "previous";
    public static final String RELATIONSHIP_BASEURI = "baseUri";
    
    /**
     * Defines the relationship of the link to its entity, per Web Linking (RFC5899).
     * (Required).
     * @return never <code>null</code> or empty.
     */
    public String[] getRel();
    
    /**
     * The URI of the linked resource. Required.
     * @return never <code>null</code> or empty.
     */
    public String getHref();

    /**
     * Set the href for the link.
     * 
     * @param href
     * cannot be <code>null</code> or empty.
     */
    public void setHref(String href);

}