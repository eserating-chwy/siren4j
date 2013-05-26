package com.google.code.siren4j.condition;

public class NullCondition implements Condition {

    
    public boolean evaluate(Object obj) {
        return obj == null;
    }

}
