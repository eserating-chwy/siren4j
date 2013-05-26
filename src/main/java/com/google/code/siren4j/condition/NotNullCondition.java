/**
 * 
 */
package com.google.code.siren4j.condition;

/**
 * @author Erik
 *
 */
public class NotNullCondition implements Condition {

    /* (non-Javadoc)
     * @see com.google.code.siren4j.condition.Condition#evaluate(java.lang.Object)
     */
    public boolean evaluate(Object obj) {
        return obj != null;
    }

}
