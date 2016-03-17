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


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.code.siren4j.component.Link;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonInclude(Include.NON_NULL)
public class LinkImpl extends Siren4JBaseComponent implements Link {

    private String[] rel;

    private String href;
    
    private String title;

    private String type;

    /*
     * (non-Javadoc)
     * 
     * @see com.google.code.siren4j.component.impl.Link#getRel()
     */
    public String[] getRel() {
        return rel;
    }

    public void setRel(String... rel) {
        this.rel = rel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.code.siren4j.component.impl.Link#getHref()
     */
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        if (StringUtils.isBlank(href)) {
            throw new IllegalArgumentException("href cannot be null or empty.");
        }
        this.href = href;
    }        

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(rel)
            .append(href)
            .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) { return false;}
        if(obj == this) { return true;}
        if(obj.getClass() != getClass()) {
          return false;
        }
        LinkImpl link = (LinkImpl)obj;
        return new EqualsBuilder()
            .append(href, link.href)
            .append(rel, link.rel)
            .isEquals();
    }    

}
