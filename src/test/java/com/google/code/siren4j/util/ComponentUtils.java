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
package com.google.code.siren4j.util;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.google.code.siren4j.component.Action;
import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.component.Link;

/**
 * Various utility and helper methods to work with Siren4J components.
 */
public class ComponentUtils {

    private ComponentUtils() {
        
    }
    
    /**
     * Retrieve a sub entity by its relationship.
     * @param entity cannot be <code>null</code>.
     * @param rel cannot be <code>null</code> or empty.
     * @return the located entity or <code>null</code> if not found.
     */
    public static Entity getSubEntityByRel(Entity entity, String... rel) {
        if(entity == null) {
            throw new IllegalArgumentException("entity cannot be null.");
        }
        if(ArrayUtils.isEmpty(rel)) {
            throw new IllegalArgumentException("rel cannot be null or empty");
        }
        List<Entity> entities = entity.getEntities();
        if (entities == null)
            return null;
        Entity ent = null;
        for (Entity e : entities) {
            if (ArrayUtils.isNotEmpty(e.getRel()) && ArrayUtils.toString(rel).equals(ArrayUtils.toString(e.getRel()))) {
                ent = e;
                break;
            }
        }
        return ent;
    }
    
    /**
     * Retrieve a link by its relationship.
     * @param entity cannot be <code>null</code>.
     * @param rel cannot be <code>null</code> or empty.
     * @return the located link or <code>null</code> if not found.
     */
    public static Link getLinkByRel(Entity entity, String... rel) {
        if(entity == null) {
            throw new IllegalArgumentException("entity cannot be null.");
        }
        if(ArrayUtils.isEmpty(rel)) {
            throw new IllegalArgumentException("rel cannot be null or empty");
        }
        List<Link> links = entity.getLinks();
        if (links == null)
            return null;
        Link link = null;
        for (Link l : links) {
            if (ArrayUtils.isNotEmpty(l.getRel()) && ArrayUtils.toString(rel).equals(ArrayUtils.toString(l.getRel()))) {
                link = l;
                break;
            }
        }
        return link;
    }
    
    /**
     * Retrieve an action by its name.
     * @param entity cannot be <code>null</code>.
     * @param name cannot be <code>null</code> or empty.
     * @return the located action or <code>null</code> if not found.
     */
    public static Action getActionByName(Entity entity, String name) {
        if(entity == null) {
            throw new IllegalArgumentException("entity cannot be null.");
        }
        if(StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name cannot be null or empty.");
        }
        List<Action> actions = entity.getActions();
        if (actions == null)
            return null;
        Action action = null;
        for (Action a : actions) {
            if (a.getName().equals(name)) {
                action = a;
                break;
            }
        }
        return action;
    }

}
