package com.google.code.siren4j.converter;

import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.component.testpojos.Author;
import com.google.code.siren4j.component.testpojos.Course;
import com.google.code.siren4j.resource.CollectionResource;

public class ReflectingConverterTest {

    @Test
    public void testToEntity() throws Exception {
	
	    Entity ent = ReflectingConverter.getInstance().toEntity(getTestCourse());
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	    System.out.println(mapper.writeValueAsString(ent));
	
    }
    
    
    
	private Course getTestCourse() {
		Course course = new Course();
		course.setCourseid("testCourseID1");
		course.setCreatedate(new Date());
		course.setTitle("Test Course 1 Title");
		course.setDescription("Test Course 1 Description");
		course.setType("Online");
		CollectionResource<Author> authors = new CollectionResource<Author>();
		
		Author author1 = new Author();
		author1.setFirstname("Jim");
		author1.setMiddlename("T");
		author1.setLastname("Smith");
		author1.setId("X111");
		authors.add(author1);
		
		Author author2 = new Author();
		author2.setFirstname("Anne");
		author2.setMiddlename("C");
		author2.setLastname("Frank");
		author2.setId("X211");
		authors.add(author2);
		
		course.setAuthors(authors);
		
		return course;
	}
	

}
