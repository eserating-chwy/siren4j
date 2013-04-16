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
package com.google.code.siren4j.converter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.google.code.siren4j.component.Action;
import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.component.Link;
import com.google.code.siren4j.component.builder.ActionBuilder;
import com.google.code.siren4j.component.builder.LinkBuilder;
import com.google.code.siren4j.component.impl.ActionImpl.Method;
import com.google.code.siren4j.component.testpojos.Author;
import com.google.code.siren4j.component.testpojos.Comment;
import com.google.code.siren4j.component.testpojos.Comment.Status;
import com.google.code.siren4j.component.testpojos.Course;
import com.google.code.siren4j.resource.CollectionResource;
import com.google.code.siren4j.resource.Resource;
import com.google.code.siren4j.util.ComponentUtils;

public class ReflectingConverterTest {

    @Test
    @Ignore
    public void testToEntity() throws Exception {

        Entity ent = ReflectingConverter.newInstance().toEntity(getTestCourse());

        System.out.println(ent.toString());

    }

    @Test
     @Ignore
    public void testSubEntityRelOverride() throws Exception {
        // If rel specified in sub entity annotation, it should be used by the
        // sub entity.
        Entity ent = ReflectingConverter.newInstance().toEntity(getTestCourse());
        Entity subEnt = ComponentUtils.getSubEntityByRel(ent, "lastComment");
        assertNotNull("Should have found sub entity with rel equal to 'lastComment'", subEnt);
    }

    @Test
     @Ignore
    public void testSubEntityOverrideLinks() throws Exception {
        Entity ent = ReflectingConverter.newInstance().toEntity(getTestCourse());
        Entity subEnt = ComponentUtils.getSubEntityByRel(ent, "courseComment");

        assertNotNull("Should have found sub entity with rel equal to 'courseComment'", subEnt);

        Link courseLink = ComponentUtils.getLinkByRel(subEnt, "course");
        assertEquals("/courses/testCourseID1/overridden", courseLink.getHref());

        Link fooLink = ComponentUtils.getLinkByRel(subEnt, "foo");
        assertNotNull("Expected to find a link named 'foo'", fooLink);

    }

    @Test
     @Ignore
    public void testSubEntityOverrideActions() throws Exception {
        Entity ent = ReflectingConverter.newInstance().toEntity(getTestCourse());
        Entity subEnt = ComponentUtils.getSubEntityByRel(ent, "courseComment");

        assertNotNull("Should have found sub entity with rel equal to 'courseComment'", subEnt);

        Action deleteAction = ComponentUtils.getActionByName(subEnt, "Delete");
        assertEquals("/comments/14/overridden", deleteAction.getHref());

        Action rejectAction = ComponentUtils.getActionByName(subEnt, "Reject");
        assertNotNull("Expected action with the name 'Reject'", rejectAction);

    }

    @Test
     @Ignore
    public void testDynamicLinksOverride() throws Exception {
        Course course = getTestCourse();
        String overriddenHref = "/overridden";
        List<Link> dynamicLinks = new ArrayList<Link>();
        dynamicLinks.add(LinkBuilder.newInstance().setRelationship("reviews").setHref(overriddenHref).build());
        course.setEntityLinks(dynamicLinks);

        Entity ent = ReflectingConverter.newInstance().toEntity(course);

        Link reviewsLink = ComponentUtils.getLinkByRel(ent, "reviews");
        assertEquals(overriddenHref, reviewsLink.getHref());
    }

    @Test
     @Ignore
    public void testDynamicActionOverride() throws Exception {
        Course course = getTestCourse();
        String overridenHref = "/overridden";
        List<Action> dynamicActions = new ArrayList<Action>();
        dynamicActions.add(ActionBuilder.newInstance().setName("addReview").setHref(overridenHref)
            .setMethod(Method.DELETE).build());
        course.setEntityActions(dynamicActions);
        Entity ent = ReflectingConverter.newInstance().toEntity(course);

        Action deleteAction = ComponentUtils.getActionByName(ent, "addReview");
        assertEquals(overridenHref, deleteAction.getHref());
    }

    @Test
     @Ignore
    public void testNoResolveTokens() throws Exception {
        // tokens with square brackets around the key should not be resolved to
        // the value
        // they should only end up as normal tokens themselves with square
        // brackets removed.
        Entity ent = ReflectingConverter.newInstance().toEntity(getTestCourse());
        Entity authorsEnt = ComponentUtils.getSubEntityByRel(ent, "authors2");
        // Check to be sure that both normal and parent. tokens get resolved
        // correctly.
        // Resolving: /authors?courseid={parent.courseid}/{offset} where
        // parent.courseid is the course object's field and offset is the
        // collection resources
        // field.
        String expected = "/authors?courseid=testCourseID1/{offset}";
        Link selfLink = ComponentUtils.getLinkByRel(authorsEnt, "self");
        assertEquals(expected, selfLink.getHref());
    }

    @Test
     @Ignore
    public void testResolveTokens() throws Exception {
        Entity ent = ReflectingConverter.newInstance().toEntity(getTestCourse());
        Entity authorsEnt = ComponentUtils.getSubEntityByRel(ent, "authors");
        // Check to be sure that both normal and parent. tokens get resolved
        // correctly.
        // Resolving: /authors?courseid={parent.courseid}/{offset} where
        // parent.courseid is the course object's field and offset is the
        // collection resources
        // field.
        String expected = "/authors?courseid=testCourseID1/10";
        Link selfLink = ComponentUtils.getLinkByRel(authorsEnt, "self");
        assertEquals(expected, selfLink.getHref());
    }

    @Test
     @Ignore
    public void testOverrideEmbeddedLink() throws Exception {
        Entity ent = ReflectingConverter.newInstance().toEntity(getTestCourse());

        System.out.println(ent.toString());
    }

    @Test
     @Ignore
    public void testOverrideRelationship() throws Exception {

    }
    

    @Test
     @Ignore
    public void testPropertyNameOverride() throws Exception {
        Comment comment = getTestComment("12", "testCourseID1", "X113", "This course is great.");
        Entity ent = ReflectingConverter.newInstance().toEntity(comment);
        Map<String, Object> props = ent.getProperties();
        System.out.println(ent.toString());
        assertTrue("Expecting property named 'user' to exist.", props.containsKey("user"));

    }

    @Test
    @Ignore
    public void testToEntityPerformance() throws Exception {
        Date start = new Date();
        for (int i = 1; i < 10000; i++) {
            ReflectingConverter.newInstance().toEntity(getTestCourse());
        }
        Date end = new Date();
        long elapsed = end.getTime() - start.getTime();
        System.out.println("Elapsed time: " + elapsed + " milliseconds");

    }
    
    @Test
    public void testToResource() throws Exception {
        Comment comment = getTestComment("12", "testCourseID1", "X113", "This course is great.");
        Entity ent = ReflectingConverter.newInstance().toEntity(comment);
        
        ResourceRegistry reg = ResourceRegistryImpl.newInstance("com.google.code.siren4j");
        Resource result = ReflectingConverter.newInstance(reg).toResource(ent);
    }

    private Course getTestCourse() {
        Course course = new Course();
        course.setCourseid("testCourseID1");
        course.setCreatedate(new Date());
        course.setTitle("Test Course 1 Title");
        course.setDescription("Test Course 1 Description");
        course.setType("Online");
        CollectionResource<Author> authors = new CollectionResource<Author>();
        authors.setOffset(10);

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
        course.setAuthors2(authors);

        course.setLastComment(getTestComment("12", "testCourseID1", "X113", "This course is great."));
        course.setFirstComment(getTestComment("14", "testCourseID1", "X115", "This course is too easy."));
        course.setEmbedComment(getTestComment("16", "testCourseID1", "X116", "This comment is embedded."));

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

}
