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

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.code.siren4j.annotations.Siren4JCondition;
import com.google.code.siren4j.annotations.Siren4JCondition.Is;

public class ConditionFactory {
    
    private static ConditionFactory instance;
    private Map<Is, Condition> conditionMap = new LinkedHashMap<Siren4JCondition.Is, Condition>();
    
    private ConditionFactory() {
        conditionMap.put(Is.EMPTY, new EmptyCondition());
        conditionMap.put(Is.FALSE, new FalseCondition());
        conditionMap.put(Is.NOTEMPTY, new NotEmptyCondition());
        conditionMap.put(Is.NOTNULL, new NotNullCondition());
        conditionMap.put(Is.NULL, new NullCondition());
        conditionMap.put(Is.TRUE, new TrueCondition());
    }
    
    public static ConditionFactory getInstance() {
        if(instance == null) {
            instance = new ConditionFactory();
        }
        return instance;
    }
    
    public Condition getCondition(final Is type) {
        return conditionMap.get(type);
        
    }

}
