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
