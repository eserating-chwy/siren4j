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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import com.google.code.siren4j.component.testpojos.EntityClassAndNamePojo;
import com.google.code.siren4j.component.testpojos.ExtendedNormalPojo;
import com.google.code.siren4j.component.testpojos.NoNamePojo;
import com.google.code.siren4j.component.testpojos.OverriddenCollection;
import com.google.code.siren4j.component.testpojos.Video;
import com.google.code.siren4j.component.testpojos.Video.Rating;
import com.google.code.siren4j.error.Siren4JRuntimeException;
import com.google.code.siren4j.resource.CollectionResource;
import com.google.code.siren4j.util.ComponentUtils;

public class ReflectingConverterTest {

    @Test
    //@Ignore
    public void testToJacksonThereAndBackEntity() throws Exception {

        Entity ent = ReflectingConverter.newInstance().toEntity(getTestCourse());
        String there = ent.toString();
        
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        Entity back = mapper.readValue(there, Entity.class);
        assertEquals(ent.toString(), back.toString());
    }
    
    @Test
    public void testOverriddenCollectionToEntity() throws Exception {
        OverriddenCollection resource = new OverriddenCollection();
        Entity ent = ReflectingConverter.newInstance().toEntity(resource);
    }
    
    @Test
    public void testNonResourceClass() throws Exception {
        ExtendedNormalPojo pojo = new ExtendedNormalPojo();
        pojo.setId(12);
        pojo.setName("Test pojo 1");
        pojo.setLastmodify(new Date());
        Collection<String> refs = new CollectionResource<String>();
        refs.add("Foo");
        refs.add("Bar");
        pojo.setRefs(refs);
        Map<String, String> map = new HashMap<String, String>();
        map.put("key1", "dog");
        map.put("key2", "cat");
        pojo.setTestMap(map);
         
        Entity ent = ReflectingConverter.newInstance().toEntity(pojo);
        System.out.println("Normal pojo:");
        System.out.println(ent.toString());
    }
    
    @Test
    //@Ignore
    public void testSubEntityRelOverride() throws Exception {
        // If rel specified in sub entity annotation, it should be used by the
        // sub entity.
        Entity ent = ReflectingConverter.newInstance().toEntity(getTestCourse());
        Entity subEnt = ComponentUtils.getSubEntityByRel(ent, "lastComment");
        assertNotNull("Should have found sub entity with rel equal to 'lastComment'", subEnt);
    }

    @Test
    //@Ignore
    public void testSubEntityOverrideLinks() throws Exception {
        Entity ent = ReflectingConverter.newInstance().toEntity(getTestCourse());
        Entity subEnt = ComponentUtils.getSubEntityByRel(ent, "firstComment");

        assertNotNull("Should have found sub entity with rel equal to 'firstComment'", subEnt);

        Link courseLink = ComponentUtils.getLinkByRel(subEnt, "course");
        assertEquals("/courses/testCourseID1/overridden", courseLink.getHref());

        Link fooLink = ComponentUtils.getLinkByRel(subEnt, "foo");
        assertNotNull("Expected to find a link named 'foo'", fooLink);

    }

    @Test
    //@Ignore
    public void testSubEntityOverrideActions() throws Exception {
        Entity ent = ReflectingConverter.newInstance().toEntity(getTestCourse());
        Entity subEnt = ComponentUtils.getSubEntityByRel(ent, "firstComment");

        assertNotNull("Should have found sub entity with rel equal to 'firstComment'", subEnt);

        Action deleteAction = ComponentUtils.getActionByName(subEnt, "Delete");
        assertEquals("/comments/14/overridden", deleteAction.getHref());

        Action rejectAction = ComponentUtils.getActionByName(subEnt, "Reject");
        assertNotNull("Expected action with the name 'Reject'", rejectAction);

    }

    @Test
    //@Ignore
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
    //@Ignore
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
    //@Ignore
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
    //@Ignore
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
    //@Ignore
    public void testOverrideEmbeddedLink() throws Exception {
        Course testCourse = getTestCourse();
        testCourse.setDescription("");
        Entity ent = ReflectingConverter.newInstance().toEntity(testCourse);
        System.out.println("Test override embedded Link:");
        System.out.println(ent.toString());
    }
    
    @Test
    //@Ignore
    public void testCondition() throws Exception {
        Course testCourse = getTestCourse();
        testCourse.setDescription("");
        Entity ent = ReflectingConverter.newInstance().toEntity(testCourse);
        System.out.println("Test condition:");
        System.out.println(ent.toString());
        assertNull(ent.getActions());
    }

    @Test
    //@Ignore
    public void testOverrideRelationship() throws Exception {
        
    }
    
    @Test
    //@Ignore
    public void testEnumProperties() throws Exception {
        Video video = new Video();
        video.setId("z1977");
        video.setName("Star Wars");
        video.setDescription("An epic science fiction space opera");
        video.setRating(Rating.PG);
        video.setGenre("scifi");
        Entity ent = ReflectingConverter.newInstance().toEntity(video);
        assertEquals(Rating.PG, ent.getProperties().get("rating"));
    }
    
    @Test
    //@Ignore
    public void testSubEntityUsesFieldnameForRel() throws Exception {
        Entity ent = ReflectingConverter.newInstance().toEntity(getTestCourse());
        Entity authorsEnt = ComponentUtils.getSubEntityByRel(ent, "authors");
        assertNotNull("Expected subentity with 'authors' relationship to exist.", authorsEnt);
    }
    

    @Test
    //@Ignore
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
    public void testCollectionResourceTopLevelEntity() throws Exception {
        
        CollectionResource<NoNamePojo> coll = new CollectionResource<NoNamePojo>();
        coll.add(new NoNamePojo("id1", "foo", "bar"));
        coll.add(new NoNamePojo("id2", "hello", "world"));
        Entity ent = ReflectingConverter.newInstance().toEntity(coll);
        System.out.println("testCollectionResourceTopLevelEntity:");
        System.out.println(ent.toString());
        
    }
    
    @Test
    public void testToResource() throws Exception {
        Entity ent = ReflectingConverter.newInstance().toEntity(getTestCourse());
        
        ResourceRegistry reg = ResourceRegistryImpl.newInstance("com.google.code.siren4j");
        Object result = ReflectingConverter.newInstance(reg).toObject(ent);
        System.out.println("ToResource: ");
        System.out.println(ReflectingConverter.newInstance().toEntity((Course)result).toString());
    }
    
    @Test
    public void testBaseUri() throws Exception {
        Course course = getTestCourse();
        course.setBaseUri("http://myhost:8080/rest/");
        course.setFullyQualifiedLinks(true);
        Entity ent = ReflectingConverter.newInstance().toEntity(course);
        System.out.println("testBaseUri: ");
        System.out.println(ent.toString());
    }
    
    @Test(expected = Siren4JRuntimeException.class)
    public void testUsingBothEntityClassAndName() throws Exception {
        EntityClassAndNamePojo pojo = new EntityClassAndNamePojo();        
        Entity ent = ReflectingConverter.newInstance().toEntity(pojo);        
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
        
        Collection<Integer> basicColl = new ArrayList<Integer>();
        basicColl.add(56);
        basicColl.add(10);
        basicColl.add(15);
        course.setBasicCollection(basicColl);
        
        Map<String, Boolean> boolMap = new HashMap<String, Boolean>();
        boolMap.put("firstEntry", true);
        boolMap.put("secondEntry", false);
        
        course.setBoolMap(boolMap);
        
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
