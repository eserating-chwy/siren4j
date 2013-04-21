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
package com.google.code.siren4j.component.builder;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.component.Link;

public class EntityBuilderTest {

	private EntityBuilder builder;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		builder = EntityBuilder.newInstance();
	}
	
	@Test
	public void testBuild() throws Exception {
		Link selfLink = LinkBuilder.newInstance()
				.setRelationship(Link.RELATIONSHIP_SELF)
				.setHref("/self/link")
				.build();
		Collection<Integer> coll = new ArrayList<Integer>();
		coll.add(4);
		coll.add(5);
		coll.add(6);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("key1", "foo");
		map.put("key2", "bar");
		Entity result = builder
				.setEntityClass("test")
				.addProperty("foo", "hello")
				.addProperty("number", 1)
				.addProperty("array", new String[] {"hey", "this", "is", "array"})
				.addProperty("collection", coll)
				.addProperty("map", map)
				.addLink(selfLink)
				.build();
		assertEquals("test", result.getEntityClass()[0]);
		assertEquals(5, result.getProperties().size());
		assertTrue(result.getProperties().containsKey("number"));
		System.out.println(result.toString());
	}
	
	
	
}
