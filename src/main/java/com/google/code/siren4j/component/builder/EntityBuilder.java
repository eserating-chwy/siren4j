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
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.google.code.siren4j.component.Action;
import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.component.Link;
import com.google.code.siren4j.component.impl.ActionImpl;
import com.google.code.siren4j.component.impl.EmbeddedEntityImpl;
import com.google.code.siren4j.component.impl.EntityImpl;
import com.google.code.siren4j.component.impl.LinkImpl;
import com.google.code.siren4j.error.Siren4JBuilderValidationException;

/**
 * The EntityBuilder allows the construction of an Entity object via a fluent
 * builder API.
 * 
 * <pre>
 * <b>Example Usage:</b>
 * 
 * EntityBuilder builder = EntityBuilder.newInstance();
 * Entity result = builder
 *    .setEntityClass("test")
 *    .addProperty("foo", "hello")
 *    .addProperty("number", 1)
 *    .addLinks(getLinks())
 *    .addActions(getActions())
 *    .build();    
 *    
 * </pre>
 * 
 */
public class EntityBuilder extends BaseBuilder<Entity> {

    private List<Entity> subEntities = new ArrayList<Entity>();

    private List<Link> links = new ArrayList<Link>();

    private List<Action> actions = new ArrayList<Action>();

    private Map<String, Object> properties = new LinkedHashMap<String, Object>();
    
    private boolean isEmbedded;

    private EntityBuilder() {
    }
    
    /**
     * Creates a new instance of the <code>EntityBuilder</code>.
     * @return new instance of the builder, never <code>null</code>.
     */
    public static EntityBuilder newInstance() {
        return new EntityBuilder();
    }
    
    /**
     * Set the entity class of the entity to be built. This method can be called many times
     * but only the value of the last call is used in the built entity. This is an optional property as specified
     * by the Siren specification.
     * @param entityClass cannot be <code>null</code> or empty.
     * @return <code>this</code> builder, never <code>null</code>.
     */
    public EntityBuilder setEntityClass(String... entityClass) {
    	if(ArrayUtils.isEmpty(entityClass)) {
    		throw new IllegalArgumentException("entityClass cannot be null or empty.");
    	}
        addStep("setEntityClass", new Object[] { entityClass });
        return this;
    }
    
    /**
     * Sets the relationship of the entity to be built. Generally only used for sub entities. This method can be called many times
     * but only the value of the last call is used in the built entity. This is an optional property as specified
     * by the Siren specification.
     * @param rel cannot be <code>null</code> or empty.
     * @return <code>this</code> builder, never <code>null</code>.
     */
    public EntityBuilder setRelationship(String... rel) {
    	if(ArrayUtils.isEmpty(rel)) {
    		throw new IllegalArgumentException("rel cannot be null or empty.");
    	}
    	addStep("setRel", new Object[] { rel });
        return this;
    }
    
    /**
     * Sets the title of the entity to be built. This method can be called many times
     * but only the value of the last call is used in the built entity. This is an optional property as specified
     * by the Siren specification.
     * @param title cannot be <code>null</code> or empty.
     * @return <code>this</code> builder, never <code>null</code>.
     */
    public EntityBuilder setTitle(String title) {
        if(StringUtils.isBlank(title)) {
            throw new IllegalArgumentException("title cannot be null or empty.");
        }
        addStep("setTitle", new Object[] { title });
        return this;
    }
    
    /**
     * Set the href of the entity to be built. This method can be called many times
     * but only the value of the last call is used in the built entity. This is a required property if this entity is an embedded link, as specified
     * by the Siren specification. If set then this entity will be considered an embedded link.
     * @param href cannot be <code>null</code> or empty.
     * @return <code>this</code> builder, never <code>null</code>.
     */
    public EntityBuilder setHref(String href) {
    	if(StringUtils.isBlank(href)) {
    		throw new IllegalArgumentException("href cannot be null or empty.");
    	}
        addStep("setHref", new Object[] { href });
        isEmbedded = true;
        return this;
    }
    
    /**
     * Adds a single property to the entity to be built. 
     * Properties are optional according to the Siren specification.
     * @param name cannot be <code>null</code> or empty.
     * @param value may be <code>null</code> or empty.
     * @return <code>this</code> builder, never <code>null</code>.
     */
    public EntityBuilder addProperty(String name, Object value) {
    	if(StringUtils.isBlank(name)) {
    		throw new IllegalArgumentException("name cannot be null or empty.");
    	}
        addStep("_addProperty", new Object[] { name, value }, true);
        return this;
    }
    
    /**
     * Adds a map of properties to the entity to be built. 
     * Properties are optional according to the Siren specification.
     * @param properties cannot be <code>null</code>.
     * @return <code>this</code> builder, never <code>null</code>.
     */
    public EntityBuilder addProperties(Map<String, Object> properties) {
    	if(MapUtils.isEmpty(properties)) {
    		throw new IllegalArgumentException("properties cannot be null or empty.");
    	}
        for (String key : properties.keySet()) {
            addProperty(key, properties.get(key));
        }
        return this;
    }
    
    /**
     * Add a sub entity to the entity to be built. 
     * Sub entities are optional according to the Siren specification.
     * @param subEntity cannot be <code>null</code>.
     * @return <code>this</code> builder, never <code>null</code>.
     */
    public EntityBuilder addSubEntity(Entity subEntity) {
    	if(subEntity == null) {
    		throw new IllegalArgumentException("subEntity cannot be null.");
    	}
        addStep("_addEntity", new Object[] { subEntity }, true);
        return this;
    }
    
    /**
     * Add a list of sub entities to the entity to be built. 
     * Sub entities are optional according to the Siren specification.
     * @param entities cannot be <code>null</code>, may be empty.
     * @return <code>this</code> builder, never <code>null</code>.
     */
    public EntityBuilder addSubEntities(List<Entity> entities) {
    	if(entities == null) {
    		throw new IllegalArgumentException("entities cannot be null.");
    	}
    	for (Entity entity : entities) {
            addSubEntity(entity);
        }
        return this;
    }
    
    /**
     * Add a link to the entity to be built. Links are optional according to the Siren specification, however
     * an entity should always have a 'self' link to be considered HATEAOS compliant.
     * @param link cannot be <code>null</code> or empty.
     * @return <code>this</code> builder, never <code>null</code>.
     */
    public EntityBuilder addLink(Link link) {
    	if(link == null) {
    		throw new IllegalArgumentException("link cannot be null.");
    	}
    	addStep("_addLink", new Object[] { link }, true);
        return this;
    }

    /**
     * Adds list of links to the entity to be built. Links are optional according to the Siren specification, however
     * an entity should always have a 'self' link to be considered HATEAOS compliant.
     * @param links cannot be <code>null</code>, may be empty.
     * @return <code>this</code> builder, never <code>null</code>.
     */
    public EntityBuilder addLinks(List<Link> links) {
    	if(links == null) {
    		throw new IllegalArgumentException("links cannot be null.");
    	}
        for (Link link : links) {
            addLink(link);
        }
        return this;
    }
    
    /**
     * Add an action to the entity to be built. Actions are options according to the Siren specification.
     * @param action cannot be <code>null</code>.
     * @return <code>this</code> builder, never <code>null</code>.
     */
    public EntityBuilder addAction(Action action) {
    	if(action == null) {
    		throw new IllegalArgumentException("action cannot be null.");
    	}
    	addStep("_addAction", new Object[] { action }, true);
        return this;
    }
    
    /**
     * Add a list of actions to the entity to be built. Actions are options according to the Siren specification.
     * @param actions cannot be <code>null</code>, may be empty.
     * @return <code>this</code> builder, never <code>null</code>.
     */
    public EntityBuilder addActions(List<Action> actions) {
    	if(actions == null) {
    		throw new IllegalArgumentException("actions cannot be null.");
    	}
        for (Action action : actions) {
            addAction(action);
        }
        return this;
    }
    
    void _addEntity(EmbeddedEntityImpl entity) {
        subEntities.add(entity);
    }

    void _addEntity(EntityImpl entity) {
        subEntities.add(entity);
    }

    void _addLink(LinkImpl link) {
        links.add(link);
    }

    void _addAction(ActionImpl action) {
        actions.add(action);
    }

    void _addProperty(String name, Object value) {
        properties.put(name, value);
    }

    void _addProperty(String name, String value) {
        properties.put(name, value);
    }

    void _addProperty(String name, Integer value) {
        properties.put(name, value);
    }

    void _addProperty(String name, Long value) {
        properties.put(name, value);
    }

    void _addProperty(String name, Float value) {
        properties.put(name, value);
    }

    void _addProperty(String name, Double value) {
        properties.put(name, value);
    }

    void _addProperty(String name, Boolean value) {
        properties.put(name, value);
    }

    void _addProperty(String name, Date value) {
        properties.put(name, value);
    }

    @Override
    protected void postProcess(Entity obj) {
        EntityImpl entity = (EntityImpl) obj;
        if (!CollectionUtils.isEmpty(subEntities)) {
            entity.setEntities(subEntities);
        }

        if (!CollectionUtils.isEmpty(actions)) {
            entity.setActions(actions);
        }

        if (!CollectionUtils.isEmpty(links)) {
            entity.setLinks(links);
        }

        if (!MapUtils.isEmpty(properties)) {
            entity.setProperties(properties);
        }
    }

    @Override
    protected void validate(Entity obj) {
        validateSubEntities(obj);
    }
    
    /**
     * Validate sub entities ensuring the existence of a relationship and if an embedded link then ensure the
     * existence of an href.
     * @param obj assumed not <code>null</code>.
     */
    private void validateSubEntities(Entity obj) {
        // Validate that all sub entities have a "rel" set.
        String relRequired = "Sub entities are required to have a <rel> property set.";
        String hrefReqForEmbed = "Sub entities that are embedded links are required to have a <href> property set.";
        if (!CollectionUtils.isEmpty(obj.getEntities())) {
            for (Entity e : obj.getEntities()) {
                if (e.getRel() == null || ArrayUtils.isEmpty(e.getRel())) {
                    throw new Siren4JBuilderValidationException("entities", obj.getClass(), relRequired);
                }
                if ((e instanceof EmbeddedEntityImpl) && StringUtils.isBlank(e.getHref())) {
                    throw new Siren4JBuilderValidationException("entities", obj.getClass(), hrefReqForEmbed);
                }
            }
        }
    }

    @Override
    protected Entity createInstance() {
        if(isEmbedded) {
            return new EmbeddedEntityImpl();
        }
        return new EntityImpl();
    }

}
