package com.google.code.siren4j.condition;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.google.code.siren4j.error.Siren4JRuntimeException;
import com.google.code.siren4j.util.ComponentUtils;

public class EmptyCondition implements Condition {

    

    public boolean evaluate(Object obj) {
        if(obj == null) {
            return true;
        }
        boolean result = false;
        if(obj instanceof String) {
            result = StringUtils.isEmpty((String)obj);
        } else if (obj instanceof Collection<?>) {
            result = ((Collection<?>) obj).isEmpty();
        } else if (obj instanceof Map<?, ?>) {
            result = ((Map<?,?>)obj).isEmpty();
        } else if(obj instanceof Object[]) {
            result = ArrayUtils.isEmpty((Object[])obj);
        } else if(ComponentUtils.isNumeric(obj)) {
            result = ((Number)obj).intValue() == 0; 
        } else if(obj instanceof Boolean) {
            throw new Siren4JRuntimeException(
                "Unsupported value type for 'EMPTY' condition: Boolean");
        }
        return result;
    }

}
