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

import org.junit.Test;

import com.google.code.siren4j.component.Action;

public class ActionBuilderTest {

	@Test
	public void testBuild() throws Exception {
		ActionBuilder builder = ActionBuilder.newInstance();
		
		Action result = builder.setName("testAction")
			.setHref("/some/path")
		    .build();
	}
	
	@Test
	public void  testSettingOptionalFieldNull() throws Exception {
        ActionBuilder builder = ActionBuilder.newInstance();
        
        Action result = builder.setName("testAction")
            .setHref("/some/path")
            .setTitle(null)
            .build();
	}
	
	@Test
	public void testSetActionClass() {
	    Action action = ActionBuilder.newInstance()
	    .setName("testAction")
            .setHref("/some/path")
	    .setActionClass("someClassName")
	    .build();
	    System.out.println(action.toString());
	}

}
