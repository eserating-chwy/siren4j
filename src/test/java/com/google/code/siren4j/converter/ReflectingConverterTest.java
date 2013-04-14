/*******************************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Erik R Serating
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *********************************************************************************************/
package com.google.code.siren4j.converter;

import java.util.Date;

import org.junit.Ignore;
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
    
    @Test
    @Ignore
    public void testToEntityPerformance() throws Exception {
        Date start = new Date();
        for(int i = 1; i < 10000; i++) {
            ReflectingConverter.getInstance().toEntity(getTestCourse());
        }
        Date end = new Date();
        long elapsed = end.getTime() - start.getTime();
        System.out.println("Elapsed time: " + elapsed + " milliseconds");

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
