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

import org.junit.Before;
import org.junit.Test;

import com.google.code.siren4j.component.Field;
import com.google.code.siren4j.meta.FieldType;

public class FieldBuilderTest {
	
	private FieldBuilder builder;
	
	@Before
	public void setUp() {
		builder = FieldBuilder.newInstance();
	}
	
	@Test
	public void testBuild() throws Exception {
		String fieldName = "code";
		FieldType fieldType = FieldType.HIDDEN;
		String fieldValue = "3455";
		
		Field result = builder.setName(fieldName)
		    .setType(fieldType)
		    .setValue(fieldValue)
		    .build();
		
		assertEquals(fieldName, result.getName());
		assertEquals(fieldType, result.getType());
		assertEquals(fieldValue, result.getValue());
		
	}
	
	@Test
	public void testRequiredName() throws Exception {
		try {
			builder.setType(FieldType.BUTTON).build();
			fail("Expected validation exception.");
		} catch (Exception e) {
			//Success
		}
		
	}
	
	@Test
	public void testSetterOrdering() throws Exception {
		//Last call to a particular setter should win.
		//This is really testing the BaseBuilder.
		Field result = builder.setName("foo")
				.setType(FieldType.TEXT)
				.setName("Bar")
				.setName("I win")
				.build();
		
		assertEquals("I win", result.getName());
	}
	

}
