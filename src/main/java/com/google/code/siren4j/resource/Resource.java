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
package com.google.code.siren4j.resource;

import java.util.Collection;

import com.google.code.siren4j.component.Action;
import com.google.code.siren4j.component.Link;

public interface Resource {

    /**
     * Overrides annotation settings for the uri of this resource. The override ONLY occurs if this method is called.
     * 
     * @param uri
     * the uri to override with. Cannot be <code>null</code> or empty.
     */
    public void setOverrideUri(String uri);

    /**
     * Retrieve the override uri if set.
     * 
     * @return override uri or <code>null</code> if not set.
     */
    public String getOverrideUri();

    /**
     * Override annotation settings indicating that if this is a sub entity and if it should be an embedded link. The
     * override ONLY occurs if this method is called.
     * 
     * @param isEmbeddedLink
     */
    public void setOverrideEmbeddedLink(Boolean isEmbeddedLink);

    /**
     * Retrieve the override embedded link setting if set.
     * 
     * @return <code>null</code> if not set.
     */
    public Boolean getOverrideEmbeddedLink();

    /**
     * Set links for this entity resource. These will be merged with links set by annotation by the reflecting converter..
     * Links set here will override the same named links done in annotations.
     * 
     * @param links
     * may be <code>null</code> or empty.
     */
    public void setEntityLinks(Collection<Link> links);

    /**
     * Get links set on this resource by the setter method, does not retrieve those set by annotation.
     * 
     * @return links for the entity, may be <code>null</code> or empty.
     */
    public Collection<Link> getEntityLinks();

    /**
     * Set actions for this entity resource. These will be merged with actions set by annotation by the reflecting
     * converter. Actions set here will override the same named actions done in annotations.
     * 
     * @param actions
     * may be <code>null</code> or empty.
     */
    public void setEntityActions(Collection<Action> actions);

    /**
     * Get actions set on this resource by the setter method, does not retrieve those set by annotation.
     * 
     * @return actions may be <code>null</code> or empty.
     */
    public Collection<Action> getEntityActions();
    
    /**
     * Gets the base uri for this resource. Generally Set by the webservice.
     * If not <code>null</code>, will be prefixed to all resolved href/uri's when
     * converted.
     * @return the base uri, may be <code>null</code>.
     */
    public String getBaseUri();
    
    /**
     * The base uri to prefix the href/uri's.
     * @param base may be <code>null</code> or empty.
     */
    public void setBaseUri(String base);
    
    /**
     * If <code>true</code> and the baseUri is set, then the link
     * will be generated as fully qualified. Defaults to <code>null</code> which
     * will act as <code>false</code> but allows us to tell if it was intentionally set.
     * @return
     */
    public Boolean isFullyQualifiedLinks();
    
    public void setFullyQualifiedLinks(Boolean fullyQualified);

}
