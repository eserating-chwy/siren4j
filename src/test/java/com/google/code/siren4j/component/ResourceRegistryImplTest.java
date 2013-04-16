package com.google.code.siren4j.component;

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.code.siren4j.component.testpojos.Author;
import com.google.code.siren4j.component.testpojos.Course;
import com.google.code.siren4j.converter.ResourceRegistry;
import com.google.code.siren4j.converter.ResourceRegistryImpl;

public class ResourceRegistryImplTest {
    
    @Test
    public void testResourceRegistryScanning() throws Exception {
        ResourceRegistry registry = ResourceRegistryImpl.newInstance("com.google.code.siren4j");
        assertNotNull(registry.getClassByEntityName("course"));
        assertNotNull(registry.getClassByEntityName("courseComment"));
        assertNotNull(registry.getClassByEntityName("author"));
        
        assertTrue(registry.containsClassEntry(Author.class));
        assertTrue(registry.containsEntityEntry("siren4J.collectionResource"));
        
    }
    
    @Test
    public void testOverwriteNotAllowed() throws Exception {
        ResourceRegistry registry = ResourceRegistryImpl.newInstance("com.google.code.siren4j");
        try {
            registry.putEntry("course", Author.class, false);
            fail("Expected Siren4JException for attempt at adding duplicate.");
        } catch (Exception e) {
            
        }
    }
    
    @Test
    public void testAddNewEntry() throws Exception {
        ResourceRegistry registry = ResourceRegistryImpl.newInstance("com.google.code.siren4j");
        registry.putEntry("foo", Course.class, false);
        Class<?> result = registry.getClassByEntityName("foo");
        assertEquals(Course.class, result);
    }
    
    @Test
    public void testOverwriteEntry() throws Exception {
        ResourceRegistry registry = ResourceRegistryImpl.newInstance("com.google.code.siren4j");
        registry.putEntry("course", Author.class, true);
        Class<?> result = registry.getClassByEntityName("course");
        assertEquals(Author.class, result);
    }

}
