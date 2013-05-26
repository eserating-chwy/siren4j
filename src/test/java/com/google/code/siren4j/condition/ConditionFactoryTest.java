package com.google.code.siren4j.condition;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.code.siren4j.annotations.Siren4JCondition.Is;

public class ConditionFactoryTest {

    @Test
    public void testGetInstance() {
        
        assertNotNull(ConditionFactory.getInstance());
        assertTrue(ConditionFactory.getInstance() == ConditionFactory.getInstance());
    }

    @Test
    public void testGetCondition() {
        ConditionFactory factory = ConditionFactory.getInstance();
        assertEquals(EmptyCondition.class, factory.getCondition(Is.EMPTY).getClass());
        assertEquals(FalseCondition.class, factory.getCondition(Is.FALSE).getClass());
        assertEquals(NotEmptyCondition.class, factory.getCondition(Is.NOTEMPTY).getClass());
        assertEquals(NotNullCondition.class, factory.getCondition(Is.NOTNULL).getClass());
        assertEquals(NullCondition.class, factory.getCondition(Is.NULL).getClass());
        assertEquals(TrueCondition.class, factory.getCondition(Is.TRUE).getClass());
    }

}
