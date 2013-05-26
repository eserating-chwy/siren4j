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
