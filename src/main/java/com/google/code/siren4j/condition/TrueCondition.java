package com.google.code.siren4j.condition;




import com.google.code.siren4j.error.Siren4JRuntimeException;
import com.google.code.siren4j.util.ComponentUtils;

public class TrueCondition implements Condition {

    public boolean evaluate(Object obj) {
        if(obj == null) {
            return false;
        }
        boolean result = true;
        if(obj instanceof String) {
            result = Boolean.parseBoolean((String)obj);
        } else if(ComponentUtils.isNumeric(obj)) {
            result = ((Number)obj).intValue() != 0; 
        } else if(obj instanceof Boolean) {
            result = ((Boolean)obj).booleanValue();
        } else {
            throw new Siren4JRuntimeException(
                "Unsupported value type for 'TRUE' condition: " + obj.getClass().getName());  
        }
        return result;
    }

}
