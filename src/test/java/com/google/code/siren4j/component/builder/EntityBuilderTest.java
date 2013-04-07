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

import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.component.Link;

public class EntityBuilderTest {

	@Mock
	private UriInfo uriInfo;
	
	private EntityBuilder builder;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		builder = EntityBuilder.newInstance(uriInfo);
	}
	
	@Test
	public void testBuild() throws Exception {
		Link selfLink = LinkBuilder.newInstance()
				.setRelationship(Link.RELATIONSHIP_SELF)
				.setHref("/self/link")
				.build();
		Entity result = builder
				.setEntityClass("test")
				.addProperty("foo", "hello")
				.addProperty("number", 1)
				.addLink(selfLink)
				.build();
		assertEquals("test", result.getEntityClass()[0]);
		assertEquals(2, result.getProperties().size());
		assertTrue(result.getProperties().containsKey("number"));
	}
	
	@Test
	public void testRequiredSelfRelationship() throws Exception {
	        try {
		    Entity result = builder
		    	.setEntityClass("test")
		    	.addProperty("foo", "hello")
		    	.addProperty("number", 1)
		    	.build();
		    fail("Expected validation exception.");
		} catch (Exception e) {
		    
		}
	}
	
}
