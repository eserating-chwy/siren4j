package com.google.code.siren4j.condition;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.code.siren4j.annotations.Siren4JCondition.Is;

public class TrueConditionTest extends BaseConditionTest {

    @Test
    public void testEvaluateString() {
        assertTrue(cond.evaluate("true"));
        assertFalse(cond.evaluate(EMPTY_STRING));
        assertFalse(cond.evaluate(NON_EMPTY_STRING));        
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
        assertFalse(cond.evaluate(null));         
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
        assertTrue(cond.evaluate(BOOLEAN_TRUE));
        assertFalse(cond.evaluate(BOOLEAN_FALSE));       
    }
    
    @Test
    public void testEvaluteBooleanPrimative() {
        assertTrue(cond.evaluate(BOOLEAN_PRIMATIVE_TRUE));
        assertFalse(cond.evaluate(BOOLEAN_PRIMATIVE_FALSE));         
    }
    
    @Override
    protected Is getType() {
        return Is.TRUE;
    }

}
