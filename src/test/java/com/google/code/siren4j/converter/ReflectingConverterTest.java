package com.google.code.siren4j.converter;

import java.util.Date;

import org.junit.Test;

import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.component.testpojos.Course;

public class ReflectingConverterTest {

    @Test
    public void testToEntity() throws Exception {
	
	Entity ent = ReflectingConverter.toEntity(getTestCourse());
	
    }
    
    private Course getTestCourse() {
	Course course = new Course();
	course.setCourseid("testCourseID1");
	course.setCreatedate(new Date());
	course.setTitle("Test Course 1 Title");
	course.setDescription("Test Course 1 Description");
	course.setType("Online");
	return course;
    }
	

}
