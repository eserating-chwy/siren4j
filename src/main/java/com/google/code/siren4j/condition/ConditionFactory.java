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
