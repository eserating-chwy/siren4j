/**
 * 
 */
package com.google.code.siren4j.condition;

import com.google.code.siren4j.error.Siren4JRuntimeException;
import com.google.code.siren4j.util.ComponentUtils;

/**
 * @author Erik
 *
 */
public class FalseCondition implements Condition {

    /* (non-Javadoc)
     * @see com.google.code.siren4j.condition.Condition#evaluate(java.lang.Object)
     */
    public boolean evaluate(Object obj) {
        if(obj == null) {
            return true;
        }
        boolean result = false;
        if(obj instanceof String) {
            result = !Boolean.parseBoolean((String)obj);
        } else if(ComponentUtils.isNumeric(obj)) {
            result = ((Number)obj).intValue() == 0; 
        } else if(obj instanceof Boolean) {
            result = !((Boolean)obj).booleanValue();
        } else {
            throw new Siren4JRuntimeException(
                "Unsupported value type for 'FALSE' condition: " + obj.getClass().getName());  
        }
        return result;
    }

}
