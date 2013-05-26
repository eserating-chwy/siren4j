/**
 * 
 */
package com.google.code.siren4j.condition;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.google.code.siren4j.error.Siren4JRuntimeException;
import com.google.code.siren4j.util.ComponentUtils;

/**
 * @author Erik
 *
 */
public class NotEmptyCondition implements Condition {

    /* (non-Javadoc)
     * @see com.google.code.siren4j.condition.Condition#evaluate(java.lang.Object)
     */
    public boolean evaluate(Object obj) {
        if(obj == null) {
            return false;
        }
        boolean result = true;
        if(obj instanceof String) {
            result = StringUtils.isNotEmpty((String)obj);
        } else if (obj instanceof Collection<?>) {
            result = !((Collection<?>) obj).isEmpty();
        } else if (obj instanceof Map<?, ?>) {
            result = !((Map<?,?>)obj).isEmpty();
        } else if(obj instanceof Object[]) {
            result = ArrayUtils.isNotEmpty((Object[])obj);
        } else if(ComponentUtils.isNumeric(obj)) {
            result = ((Number)obj).intValue() != 0; 
        } else if(obj instanceof Boolean) {
            throw new Siren4JRuntimeException(
                "Unsupported value type for 'NOTEMPTY' condition: Boolean");
        }
        return result;
    }

}
