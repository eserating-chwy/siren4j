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
package com.google.code.siren4j.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
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
	
	@Test
	public void testIsSirenProperty() {
	    assertTrue(ReflectionUtils.isSirenProperty(String.class, null));
	    assertTrue(ReflectionUtils.isSirenProperty(String[].class, null));
	    assertTrue(ReflectionUtils.isSirenProperty(int.class, null));
            assertTrue(ReflectionUtils.isSirenProperty(int[].class, null));
            assertTrue(ReflectionUtils.isSirenProperty(Integer.class, null));
            assertTrue(ReflectionUtils.isSirenProperty(Integer[].class, null));
            List coll = new ArrayList<String>();
            coll.add("String");
            assertTrue(ReflectionUtils.isSirenProperty(List.class, coll));
	    
	}

}
