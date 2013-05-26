package com.google.code.siren4j.condition;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.code.siren4j.annotations.Siren4JCondition.Is;

public class NotNullConditionTest extends BaseConditionTest {

    @Test
    public void testEvaluateString() {
        assertTrue(cond.evaluate(EMPTY_STRING));
        assertTrue(cond.evaluate(NON_EMPTY_STRING));        
    }
    
    @Test
    public void testEvaluateArray() {
        assertTrue(cond.evaluate(EMPTY_ARRAY));
        assertTrue(cond.evaluate(NON_EMPTY_ARRAY));         
    }
    
    @Test
    public void testEvaluateCollection() {
        assertTrue(cond.evaluate(EMPTY_COLLECTION));
        assertTrue(cond.evaluate(NON_EMPTY_COLLECTION));         
    }
    
    @Test
    public void testEvaluateMap() {
        assertTrue(cond.evaluate(EMPTY_MAP));
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
        assertTrue(cond.evaluate(INT_ZERO));
        assertTrue(cond.evaluate(INT_NEGATIVE));
        assertTrue(cond.evaluate(INT_POSITIVE));        
    }
    
    @Test
    public void testEvaluateInteger() {
        assertTrue(cond.evaluate(INTEGER_ZERO));
        assertTrue(cond.evaluate(INTEGER_NEGATIVE));
        assertTrue(cond.evaluate(INTEGER_POSITIVE));        
    }
    
    @Test
    public void testEvaluateFloat() {
        assertTrue(cond.evaluate(FLOAT_ZERO));
        assertTrue(cond.evaluate(FLOAT_NEGATIVE));
        assertTrue(cond.evaluate(FLOAT_POSITIVE));        
    }
    
    @Test
    public void testEvaluateLong() {
        assertTrue(cond.evaluate(LONG_ZERO));
        assertTrue(cond.evaluate(LONG_NEGATIVE));
        assertTrue(cond.evaluate(LONG_POSITIVE));        
    }
    
    @Test
    public void testEvaluateByte() {
        assertTrue(cond.evaluate(BYTE_ZERO));
        assertTrue(cond.evaluate(BYTE_NEGATIVE));
        assertTrue(cond.evaluate(BYTE_POSITIVE));        
    }
    
    @Test
    public void testEvaluteBoolean() {
        assertTrue(cond.evaluate(BOOLEAN_TRUE));
        assertTrue(cond.evaluate(BOOLEAN_FALSE));       
    }
    
    @Test
    public void testEvaluteBooleanPrimative() {
        assertTrue(cond.evaluate(BOOLEAN_PRIMATIVE_TRUE));
        assertTrue(cond.evaluate(BOOLEAN_PRIMATIVE_FALSE));         
    }
    
    @Override
    protected Is getType() {
        return Is.NOTNULL;
    }

}
