package com.google.code.siren4j.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.google.code.siren4j.component.testpojos.Course;
import com.google.code.siren4j.converter.ReflectedInfo;

public class ReflectionUtilsTest {
	
	
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
    public void testReplaceFieldTokens() throws Exception {
	    
		List<ReflectedInfo> fields = ReflectionUtils.getExposedFieldInfo(Course.class);
		
		
		Course course = new Course();
		course.setCourseid("course1");
		
		//In subMode only this. prefixed fields should be replaced.
		assertEquals("/somepath/course1/{courseid}", 
				ReflectionUtils.replaceFieldTokens(course, "/somepath/{parent.courseid}/{courseid}", fields, true));
		//Both tokens should be replaced
		assertEquals("/somepath/{parent.courseid}/course1", 
				ReflectionUtils.replaceFieldTokens(course, "/somepath/{parent.courseid}/{courseid}", fields, false));
		//Unknown field name foo should not get replaced 
		assertEquals("/somepath/course1/{foo}", 
				ReflectionUtils.replaceFieldTokens(course, "/somepath/{courseid}/{foo}", fields, false));
		
    }
	
	@Test
	public void testGetTokenKeys() {
		
		Set<String> keys = ReflectionUtils.getTokenKeys("/path/{foo}/hello/{[bar]}/yo/{world}");
		assertEquals(3, keys.size());
		assertTrue(keys.contains("foo"));
		assertTrue(keys.contains("[bar]"));
		assertTrue(keys.contains("world"));
		
		
	}

}
