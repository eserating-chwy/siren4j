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

import org.junit.Test;

import com.google.code.siren4j.component.Link;
import com.google.code.siren4j.error.Siren4JBuilderValidationException;

public class LinkBuilderTest {

	
	@Test
	public void testNormalBuild() throws Exception {
		LinkBuilder builder = LinkBuilder.newInstance();
		
		String path = "/some/path";
		Link result = builder.setRelationship(Link.RELATIONSHIP_SELF)
		.setHref(path)
		.build();
		
		assertEquals(Link.RELATIONSHIP_SELF, result.getRel()[0]);
		assertEquals(path, result.getHref());
		
	}
	
	@Test
	public void testNoRelationship() throws Exception {
	    LinkBuilder builder = LinkBuilder.newInstance();
            
            String path = "/some/path";
            try {
                Link result = builder.setHref(path).build();
                fail("Expected validation exception for missing relationship.");
            } catch (Exception e) {
               assertTrue(e instanceof Siren4JBuilderValidationException);
            }
            
           
	}
	
	@Test
	public void testNoHref() throws Exception {
	    LinkBuilder builder = LinkBuilder.newInstance();
            
            String path = "/some/path";
            try {
                Link result = builder.setRelationship(Link.RELATIONSHIP_SELF)
                    .build();
                fail("Expected validation exception for missing href.");
            } catch (Exception e) {
                assertTrue(e instanceof Siren4JBuilderValidationException);
            }
            
            
	}
}
