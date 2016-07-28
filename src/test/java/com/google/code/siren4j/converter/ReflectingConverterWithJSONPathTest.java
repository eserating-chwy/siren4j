package com.google.code.siren4j.converter;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.code.siren4j.component.testpojos.Author;
import com.google.code.siren4j.component.testpojos.Comment;
import com.google.code.siren4j.component.testpojos.Comment.Status;
import com.google.code.siren4j.component.testpojos.Course;
import com.google.code.siren4j.component.testpojos.Course.MYENUM;
import com.google.code.siren4j.error.Siren4JException;
import com.google.code.siren4j.resource.CollectionResource;

/**
 * Unit tests for {@link ReflectingConverter} using jsonpath matchers against
 * the generated JSON mappings.
 * 
 * @author downeyt
 *
 */
public class ReflectingConverterWithJSONPathTest {

	private ResourceConverter converter;
	
	private String json;

	private Date createdate = new Date();
	
	@Before
	public void setup() throws Siren4JException {
		converter = ReflectingConverter.newInstance();
		json = converter.toEntity(getTestCourse()).toString();
	}
	
	@Test
	public void testClass() {
		assertThat(json, hasJsonPath("$.class[0]", is("course")));
	}
	
	@Test
	public void testProperties() {
		assertThat(json, hasJsonPath("$.properties.type", is("Online")));
		assertThat(json, hasJsonPath("$.properties.courseid", is("testCourseID1")));
		assertThat(json, hasJsonPath("$.properties.title", is("Test Course 1 Title")));
		assertThat(json, hasJsonPath("$.properties.description", is("Test Course 1 Description")));
	}
	
	@Test
	public void testDateProperty() {
		assertThat(json, hasJsonPath("$.properties.createdate", 
				is(DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(createdate))));
	}
	
	@Test
	public void testArray() {
		assertThat(json, hasJsonPath("$.properties.allowedTypes", contains("Online", "Classroom", "External")));
	}
	
	@Test
	public void testBasicCollection() {
		assertThat(json, hasJsonPath("$.properties.basicCollection", contains(56, 10, 15)));
	}
	
	@Test
	public void testSortedSetOfStrings() {
		assertThat(json, hasJsonPath("$.properties.setOfStrings", contains("bar", "bozz", "foo")));
	}
	
	@Test
	public void testEnum() {
		assertThat(json, hasJsonPath("$.properties.myenum", is(MYENUM.ENUM_VALUE2.toString())));
	}
	
	@Test
	public void testEntities() {
		assertThat(json, hasJsonPath("$.entities[0].class", contains("siren4J.collectionResource", "collection")));
		assertThat(json, hasJsonPath("$.entities[0].rel", contains("authors")));
		assertThat(json, hasJsonPath("$.entities[0].properties.size", is(2)));
	}
	
    private Course getTestCourse() {
        Course course = new Course();
        course.setCourseid("testCourseID1");
        course.setCreatedate(createdate);
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
        
        SortedSet<String> someStrings = new TreeSet<String>();
        someStrings.add("foo");
        someStrings.add("bar");
        someStrings.add("bozz");
        course.setSetOfStrings(someStrings);
        
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
