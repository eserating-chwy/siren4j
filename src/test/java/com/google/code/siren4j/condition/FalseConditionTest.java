package com.google.code.siren4j.condition;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.code.siren4j.annotations.Siren4JCondition.Is;

public class FalseConditionTest extends BaseConditionTest {

    @Test
    public void testEvaluateString() {
        assertFalse(cond.evaluate("true"));
        assertTrue(cond.evaluate(EMPTY_STRING));
        assertTrue(cond.evaluate(NON_EMPTY_STRING));        
    }
    
    @Test
    public void testEvaluateArray() {
        try {
            assertFalse(cond.evaluate(EMPTY_ARRAY));
            fail("Expected runtime exception.");
        } catch (Exception e) {
            
        }         
    }
    
    @Test
    public void testEvaluateCollection() {
        try {
            assertFalse(cond.evaluate(EMPTY_COLLECTION));
            fail("Expected runtime exception.");
        } catch (Exception e) {
           
        }         
    }
    
    @Test
    public void testEvaluateMap() {
        try {
            cond.evaluate(EMPTY_MAP);
            fail("Expected runtime exception.");
        } catch (Exception e) {
            
        }       
    }
    
    @Test
    public void testEvaluateNull() {
        assertTrue(cond.evaluate(null));         
    }
    
    @Test
    public void testEvaluateObject() {
        try {
            cond.evaluate(OBJECT);
            fail("Expected runtime exception.");
        } catch (Exception e) {
            
        }         
    }
    
    @Test
    public void testEvaluateIntegerPrimative() {
        assertTrue(cond.evaluate(INT_ZERO));
        assertFalse(cond.evaluate(INT_NEGATIVE));
        assertFalse(cond.evaluate(INT_POSITIVE));        
    }
    
    @Test
    public void testEvaluateInteger() {
        assertTrue(cond.evaluate(INTEGER_ZERO));
        assertFalse(cond.evaluate(INTEGER_NEGATIVE));
        assertFalse(cond.evaluate(INTEGER_POSITIVE));        
    }
    
    @Test
    public void testEvaluateFloat() {
        assertTrue(cond.evaluate(FLOAT_ZERO));
        assertFalse(cond.evaluate(FLOAT_NEGATIVE));
        assertFalse(cond.evaluate(FLOAT_POSITIVE));        
    }
    
    @Test
    public void testEvaluateLong() {
        assertTrue(cond.evaluate(LONG_ZERO));
        assertFalse(cond.evaluate(LONG_NEGATIVE));
        assertFalse(cond.evaluate(LONG_POSITIVE));        
    }
    
    @Test
    public void testEvaluateByte() {
        assertTrue(cond.evaluate(BYTE_ZERO));
        assertFalse(cond.evaluate(BYTE_NEGATIVE));
        assertFalse(cond.evaluate(BYTE_POSITIVE));        
    }
    
    @Test
    public void testEvaluteBoolean() {
        assertFalse(cond.evaluate(BOOLEAN_TRUE));
        assertTrue(cond.evaluate(BOOLEAN_FALSE));       
    }
    
    @Test
    public void testEvaluteBooleanPrimative() {
        assertFalse(cond.evaluate(BOOLEAN_PRIMATIVE_TRUE));
        assertTrue(cond.evaluate(BOOLEAN_PRIMATIVE_FALSE));         
    }
    
    @Override
    protected Is getType() {
        return Is.FALSE;
    }

}
