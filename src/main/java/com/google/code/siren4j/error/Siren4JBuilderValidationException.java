/*******************************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Erik R Serating
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *********************************************************************************************/
package com.google.code.siren4j.error;

import org.apache.commons.lang3.StringUtils;

public class Siren4JBuilderValidationException extends Siren4JRuntimeException {

    private static final long serialVersionUID = -5007142697946166657L;

    private String propertyName;

    @SuppressWarnings("rawtypes")
    private Class clazz;

    @SuppressWarnings("rawtypes")
    public Siren4JBuilderValidationException(String propertyName, Class clazz, String message) {
        super(message);
        this.propertyName = propertyName;
        this.clazz = clazz;
    }

    public Siren4JBuilderValidationException(String message) {
        super(message);
    }

    public String getPropertyName() {
        return propertyName;
    }

    @SuppressWarnings("rawtypes")
    public Class getClazz() {
        return clazz;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(propertyName) && clazz != null) {
            sb.append("Property: <");
            sb.append(propertyName);
            sb.append("> Class: <");
            sb.append(clazz.getName());
            sb.append(">: \n");
        }
        sb.append(super.getMessage());
        return sb.toString();
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }

}
