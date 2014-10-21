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
package com.google.code.siren4j.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to for adding option data elements. This is for transporting some extra meta data that is useful.
 * 
 * <pre>
 * <code>
 * Usage:
 * 
 *      &#064;Siren4JMetaData(key = "someKey", value = "someValue")
 * 
 *     <table border="1">
 *       <thead>
 *          <tr><th>Property</th><th>Required</th><th>Description</th></tr>
 *       </thead>
 *       <tbody>
 *          <tr><td>key</td><td>yes</td><td>The key to be used for the property name of the data element.</td></tr>
 *          <tr><td>value</td><td>yes</td><td>The value of the data element</td></tr>
 *       </tbody>
 *     </table>     
 *     
 * </code>
 * </pre>
 *
 */
@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Siren4JAnnotation
public @interface Siren4JMetaData {
    String key();
    String value();
}
