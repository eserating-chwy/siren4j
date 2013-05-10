package com.google.code.siren4j.converter;

import com.google.code.siren4j.component.Entity;

public interface ResourceConverter {

    /**
     * Converts an <code>Object</code> into a Siren Entity. Reflection is used to determine properties, sub enities
     * ,actions and links. Annotations are also used to figure out the values for class names and references as well as for
     * adding actions and links.
     * 
     * @param obj
     * @return
     */
    public abstract Entity toEntity(Object obj);

    public abstract Object toObject(Entity entity);

}