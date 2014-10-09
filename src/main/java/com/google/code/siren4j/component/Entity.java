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

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.code.siren4j.component.impl.EntityImpl;

/**
 * <pre>
 * An Entity is a URI-addressable resource that has properties and actions associated with it. 
 * It may contain sub-entities and navigational links.
 *
 * Root entities and sub-entities that are embedded representations MUST contain a links 
 * collection with at least one item contain a rel value of self and an href attribute with
 *  a value of the entity's URI.
 *
 * Sub-entities that are embedded links MUST contain an href attribute with a value of its URI.
 * </pre>
 *
 */
@JsonDeserialize(as = EntityImpl.class)
public interface Entity extends Component{

    /**
     * A set of key-value pairs that describe the state of an entity. 
     * In JSON Siren, this is an object such as { "name": "Kevin", "age": 30 }. (Optional).
     * @return map may be <code>null</code> or empty.
     */
    public Map<String, Object> getProperties();
    
    /**
     * A collection of related sub-entities. If a sub-entity contains an href value, it should be
     * treated as an embedded link. Clients may choose to optimistically load embedded links.
     * If no href value exists, the sub-entity is an embedded entity representation that contains
     * all the characteristics of a typical entity. 
     * One difference is that a sub-entity MUST contain a rel attribute to describe its
     * relationship to the parent entity.
     *
     * In JSON Siren, this is represented as an array. (Optional).
     * @return list of entities, may be <code>null</code> or empty.
     */
    public  List<Entity> getEntities();
    
    /**
     * A collection of items that describe navigational links, distinct from entity relationships.
     * Link items should contain a rel attribute to describe the relationship and an href
     * attribute to point to the target URI. Entities should include a link rel to self.
     * In JSON Siren, this is represented as "links": [{ "rel": "self", "href": "http://api.x.io/orders/1234" }]
     * (Optional).
     * @return list of links, may be <code>null</code> or empty.
     */
    public List<Link> getLinks();

    /**
     * A collection of action objects, represented in JSON Siren as an array such as { "actions": [{ ... }] }.
     * (Optional).
     * @return list of actions, may be <code>null</code> or empty.
     */
    public List<Action> getActions();
    
    /**
     * The URI of the linked sub-entity. (Required if embedded link).
     * @return may be <code>null</code> or empty.
     */
    public String getHref();

    /**
     * Defines media type of the linked resource, per Web Linking (RFC5899). Optional.
     * Only needed for embedded link.
     * @since 1.1.0
     * @return the media type, may be <code>null</code> or empty.
     */
    public String getType();
    
    /**
     * Defines the relationship of the sub-entity to its parent, per Web Linking (RFC5899). 
     * MUST be an array of strings. (Required if a sub-entity or embedded link).
     * @return may be <code>null</code> or empty.
     */
    public String[] getRel();   
    
    /**
     * Descriptive text about the entity. (Optional).
     * @return may be <code>null</code> or empty.
     */
    public String getTitle();

}