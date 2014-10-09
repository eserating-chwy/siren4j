package com.google.code.siren4j.component.impl;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.code.siren4j.component.Action;
import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.component.Link;

/**
 * Entity impl class to mark embedded entity and to allow suppressing of
 * properties on Jackson serialization.
 *
 */
public class EmbeddedEntityImpl extends EntityImpl {

    @Override
    @JsonIgnore
    public Map<String, Object> getProperties() {
        return super.getProperties();
    }

    @Override
    @JsonIgnore
    public List<Entity> getEntities() {
        return super.getEntities();
    }

    @Override
    @JsonIgnore
    public List<Link> getLinks() {
        return super.getLinks();
    }

    @Override
    @JsonIgnore
    public List<Action> getActions() {
        return super.getActions();
    }

    

}
