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
package com.google.code.siren4j.condition;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.code.siren4j.annotations.Siren4JCondition.Is;

public class NotEmptyConditionTest extends BaseConditionTest {

    @Test
    public void testEvaluateString() {
        assertFalse(cond.evaluate(EMPTY_STRING));
        assertTrue(cond.evaluate(NON_EMPTY_STRING));        
    }
    
    @Test
    public void testEvaluateArray() {
        assertFalse(cond.evaluate(EMPTY_ARRAY));
        assertTrue(cond.evaluate(NON_EMPTY_ARRAY));         
    }
    
    @Test
    public void testEvaluateCollection() {
        assertFalse(cond.evaluate(EMPTY_COLLECTION));
        assertTrue(cond.evaluate(NON_EMPTY_COLLECTION));         
    }
    
    @Test
    public void testEvaluateMap() {
        assertFalse(cond.evaluate(EMPTY_MAP));
        assertTrue(cond.evaluate(NON_EMPTY_MAP));         
    }
    
    @Test
    public void testEvaluateNull() {
        assertFalse(cond.evaluate(null));         
    }
    
    @Test
    public void testEvaluateObject() {
        assertTrue(cond.evaluate(OBJECT));         
    }
    
    @Test
    public void testEvaluateIntegerPrimative() {
        assertFalse(cond.evaluate(INT_ZERO));
        assertTrue(cond.evaluate(INT_NEGATIVE));
        assertTrue(cond.evaluate(INT_POSITIVE));        
    }
    
    @Test
    public void testEvaluateInteger() {
        assertFalse(cond.evaluate(INTEGER_ZERO));
        assertTrue(cond.evaluate(INTEGER_NEGATIVE));
        assertTrue(cond.evaluate(INTEGER_POSITIVE));        
    }
    
    @Test
    public void testEvaluateFloat() {
        assertFalse(cond.evaluate(FLOAT_ZERO));
        assertTrue(cond.evaluate(FLOAT_NEGATIVE));
        assertTrue(cond.evaluate(FLOAT_POSITIVE));        
    }
    
    @Test
    public void testEvaluateLong() {
        assertFalse(cond.evaluate(LONG_ZERO));
        assertTrue(cond.evaluate(LONG_NEGATIVE));
        assertTrue(cond.evaluate(LONG_POSITIVE));        
    }
    
    @Test
    public void testEvaluateByte() {
        assertFalse(cond.evaluate(BYTE_ZERO));
        assertTrue(cond.evaluate(BYTE_NEGATIVE));
        assertTrue(cond.evaluate(BYTE_POSITIVE));        
    }
    
    @Test
    public void testEvaluteBoolean() {
        try {
            cond.evaluate(BOOLEAN_TRUE);
            fail("Expected runtime exception");
        } catch (Exception e) {
            
        }
        try {
            cond.evaluate(BOOLEAN_FALSE);
            fail("Expected runtime exception");
        } catch (Exception e) {
            
        }         
    }
    
    @Test
    public void testEvaluteBooleanPrimative() {
        try {
            cond.evaluate(BOOLEAN_PRIMATIVE_TRUE);
            fail("Expected runtime exception");
        } catch (Exception e) {
            
        }
        try {
            cond.evaluate(BOOLEAN_PRIMATIVE_FALSE);
            fail("Expected runtime exception");
        } catch (Exception e) {
            
        }          
    }
    
    @Override
    protected Is getType() {
        return Is.NOTEMPTY;
    }

}
