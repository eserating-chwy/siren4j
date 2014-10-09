package com.google.code.siren4j.converter;

import com.google.code.siren4j.component.Entity;

public interface ResourceConverter {

    /**
     * Converts an <code>Object</code> into a Siren Entity. Reflection is used to determine properties, sub entities
     * ,actions and links. Annotations are also used to figure out the values for class names and references as well as for
     * adding actions and links.
     * 
     * @param obj
     * @return
     */
    public Entity toEntity(Object obj);

    /**
     * Converts the entity back to a resource object. The resource class to reconstitute is determined by
     * first looking at the special '$siren4j.class' property if it exists and if not then it uses the first element
     * of the componentClass. It then uses the ResourceRegistry to make sure the class is actually a resource.
     * @param entity the entity to convert, should not be <code>null</code>.
     * @return the reconstituted resource object.
     */
    public Object toObject(Entity entity);

    /**
     * Converts the entity back to a resource object. The resource class to reconstitute is determined by
     * first using the passed in target class then if <code>null</code>
     * looking at the special '$siren4j.class' property if it exists and if not then it uses the first element
     * of the componentClass. It then uses the ResourceRegistry to make sure the class is actually a resource.
     * @param entity  the entity to convert, should not be <code>null</code>.
     * @param targetClass explicit target class, the class needs to be a resource that is picked up by the registry.
     * The converter will need to be able to handle missing properties on the target.
     * @return the reconstituted resource object.
     */
    public Object toObject(Entity entity, Class targetClass);



}