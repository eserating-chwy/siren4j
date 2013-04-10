package com.google.code.siren4j.collections;

import java.util.Collection;

import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.component.builder.EntityBuilder;

public class BasicCollection {
    
    private String name;
    private Collection coll;
    
    public static final String COLLECTION = "collection";
    public static final String COLLECTION_ENTRY = "collectionEntry";
    
    public BasicCollection(String name, Collection coll) {
	this.name = name;
	this.coll = coll;
    }
    
    public BasicCollection(Entity entity) {
	
    }
    
    public Entity toEntity() {
	EntityBuilder builder = EntityBuilder.newInstance();
	builder.setEntityClass(new String[]{name, COLLECTION})
	    .setRelationship(COLLECTION);
	int count = 0;
	for(Object e : coll) {
	    builder.addSubEntity(createEntry(e, count++));
	}
	
	return builder.build();
    }
    
    private Entity createEntry(Object obj, int index) {
	EntityBuilder builder = EntityBuilder.newInstance();
	builder.setRelationship(COLLECTION_ENTRY)
	    .addProperty("index", index)
	    .addProperty("value", obj);
	return builder.build();
    }
    
    public Collection toCollection() {
	return coll;
    }
    
    public String getName() {
	return name;
    }

}
