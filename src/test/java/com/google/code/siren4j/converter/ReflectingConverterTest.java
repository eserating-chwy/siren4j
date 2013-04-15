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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Ignore;
import org.junit.Test;

import com.google.code.siren4j.component.Action;
import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.component.Link;
import com.google.code.siren4j.component.builder.LinkBuilder;
import com.google.code.siren4j.component.testpojos.Author;
import com.google.code.siren4j.component.testpojos.Comment;
import com.google.code.siren4j.component.testpojos.Comment.Status;
import com.google.code.siren4j.component.testpojos.Course;
import com.google.code.siren4j.resource.CollectionResource;

public class ReflectingConverterTest {

    @Test
    @Ignore
    public void testToEntity() throws Exception {

        Entity ent = ReflectingConverter.getInstance().toEntity(getTestCourse());
        
        System.out.println(ent.toString());

    }
    
    @Test
    //@Ignore
    public void testSubEntityRelOverride() throws Exception{
    	// If rel specified in sub entity annotation, it should be used by the sub entity.
    	Entity ent = ReflectingConverter.getInstance().toEntity(getTestCourse());
    	Entity subEnt = getEntityByRel(ent.getEntities(), "lastComment");
    	assertNotNull("Should have found sub entity with rel equal to 'lastComment'", subEnt);
    }
    
    @Test
    public void testSubEntityOverrideLinks() throws Exception{
    	Entity ent = ReflectingConverter.getInstance().toEntity(getTestCourse());
    	Entity subEnt = this.getEntityByRel(ent.getEntities(), "courseComment");
    	
    	assertNotNull("Should have found sub entity with rel equal to 'courseComment'", subEnt);
    	
    	Link courseLink = getLinkByRel(subEnt.getLinks(), "course");
    	assertEquals("/courses/testCourseID1/overridden", courseLink.getHref());
    	
    	Link fooLink = getLinkByRel(subEnt.getLinks(), "foo");
    	assertNotNull("Expected to find a link named 'foo'", fooLink);
    	
    }
    
    @Test
    public void testSubEntityOverrideActions() throws Exception{
    	Entity ent = ReflectingConverter.getInstance().toEntity(getTestCourse());
    	Entity subEnt = getEntityByRel(ent.getEntities(), "courseComment");
    	
    	assertNotNull("Should have found sub entity with rel equal to 'courseComment'", subEnt);
    	
    	Action deleteAction = getActionByName(subEnt.getActions(), "Delete");
    	assertEquals("/comments/14/overridden", deleteAction.getHref());
    	
    	Action rejectAction = getActionByName(subEnt.getActions(), "Reject");
    	assertNotNull("Expected action with the name 'Reject'", rejectAction);
    	
    }
    
    @Test
    public void testDynamicLinksOverride() throws Exception{
    	Course course = getTestCourse();
    	String overridenHref = "/overridden";
    	List<Link> dynamicLinks = new ArrayList<Link>();
    	dynamicLinks.add(LinkBuilder.newInstance().setRelationship("reviews").setHref(overridenHref).build());
    	course.setEntityLinks(dynamicLinks);
    	
    	Entity ent = ReflectingConverter.getInstance().toEntity(course);
    	
    	Link reviewsLink = getLinkByRel(ent.getLinks(), "reviews");
    	assertEquals(overridenHref, reviewsLink.getHref());
    }
    
    @Test
    public void testDynamicActionOverride() {
    	
    }
    
    @Test
    public void testNoResolveTokens() {
    	
    }
    
    @Test
    public void testOverrideEmbeddedLink() {
    	
    }
    
    @Test
    public void testOverrideRelationship() {
    	
    }
    
    @Test
    public void testActionAnnotation() {
    	
    }
    
    @Test
    public void testLinksAnnotation() {
    	
    }
    
    @Test
    //@Ignore
    public void testPropertyNameOverride() throws Exception {
    	Comment comment = getTestComment("12", "testCourseID1", "X113", "This course is great.");
    	Entity ent = ReflectingConverter.getInstance().toEntity(comment);
    	Map<String, Object> props = ent.getProperties();
    	System.out.println(ent.toString());
    	assertTrue("Expecting property named 'user' to exist.", props.containsKey("user"));
    	
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
        
        course.setLastComment(getTestComment("12", "testCourseID1", "X113", "This course is great."));
        course.setFirstComment(getTestComment("14", "testCourseID1", "X115", "This course is too easy."));

        return course;
    }
    
    private Comment getTestComment(String id, String courseid, String userid, String commentText) {
    	Comment comment = new Comment();
    	comment.setId(id);
    	comment.setCourseid(courseid);
    	comment.setUserid(userid);
    	comment.setCommentText(commentText);
    	comment.setCreatedate(new Date());
    	comment.setStatus(Status.PENDING);
    	
    	return comment;
    }
    
    private Entity getEntityByRel(List<Entity> entities, String... rel) {
    	if(entities == null) return null;
    	Entity entity = null;
    	for(Entity e : entities) {
    		if(ArrayUtils.isNotEmpty(e.getRel()) && ArrayUtils.toString(rel).equals(ArrayUtils.toString(e.getRel()))) {
    			entity = e;
    			break;
    		}
    	}
    	return entity;
    }
    
    private Link getLinkByRel(List<Link> links, String... rel) {
    	if(links == null) return null;
    	Link link = null;
    	for(Link l : links) {
    		if(ArrayUtils.isNotEmpty(l.getRel()) && ArrayUtils.toString(rel).equals(ArrayUtils.toString(l.getRel()))) {
    			link = l;
    			break;
    		}
    	}
    	return link;
    }
    
    private Action getActionByName(List<Action> actions, String name) {
    	if(actions == null) return null;
    	Action action = null;
    	for(Action a : actions) {
    		if(a.getName().equals(name)) {
    			action = a;
    			break;
    		}
    	}
    	return action;
    }
    
    

}
