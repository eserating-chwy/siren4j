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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;

import com.google.code.siren4j.annotations.Siren4JCondition.Is;

public abstract class BaseConditionTest {
    
    protected static final Object OBJECT = new Object();
    protected static final String EMPTY_STRING = "";
    protected static final String NON_EMPTY_STRING = "test";
    protected static final Object[] EMPTY_ARRAY = new Object[]{};
    protected static final Object[] NON_EMPTY_ARRAY = new Object[]{"test"};
    protected static final Collection<String> EMPTY_COLLECTION = new ArrayList<String>();
    protected static final Collection<String> NON_EMPTY_COLLECTION = new ArrayList<String>();
    static {
        NON_EMPTY_COLLECTION.add("test");
    }
    protected static final Map<String, String> EMPTY_MAP = new HashMap<String, String>();
    protected static final Map<String, String> NON_EMPTY_MAP = new HashMap<String, String>();
    static {
        NON_EMPTY_MAP.put("test", "test");
    }
    protected static final Integer INTEGER_ZERO = Integer.valueOf(0);
    protected static final Integer INTEGER_NEGATIVE = Integer.valueOf(-1);
    protected static final Integer INTEGER_POSITIVE = Integer.valueOf(1);
    
    protected static final int INT_ZERO = 0;
    protected static final int INT_NEGATIVE = -1;
    protected static final int INT_POSITIVE = 1;
    
    protected static final Long LONG_ZERO = Long.valueOf(0);
    protected static final Long LONG_NEGATIVE = Long.valueOf(-1);
    protected static final Long LONG_POSITIVE = Long.valueOf(1);
    
    protected static final Float FLOAT_ZERO = Float.valueOf(0);
    protected static final Float FLOAT_NEGATIVE = Float.valueOf(-1);
    protected static final Float FLOAT_POSITIVE = Float.valueOf(1);
    
    protected static final Byte BYTE_ZERO = Byte.valueOf("0");
    protected static final Byte BYTE_NEGATIVE = Byte.valueOf("-1");
    protected static final Byte BYTE_POSITIVE = Byte.valueOf("1");
    
    protected static final Boolean BOOLEAN_TRUE = Boolean.TRUE;
    protected static final Boolean BOOLEAN_FALSE = Boolean.FALSE;
    
    protected static final boolean BOOLEAN_PRIMATIVE_TRUE = true;
    protected static final boolean BOOLEAN_PRIMATIVE_FALSE = false;
    
    protected Condition cond;
    
    protected abstract Is getType();   

    
    @Before
    public void setUp() {
        cond = ConditionFactory.getInstance().getCondition(getType());
    }
    
}