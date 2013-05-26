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

import com.google.code.siren4j.component.impl.ActionImpl.Method;
/**
 * Used with {@link Siren4JAction} and {@link Siren4JLink} to allow logic to include/exclude an
 * action or link based on the value in a field or returned from a method.
 * Only no argument methods can be called.
 * <code>
 * <pre>
 *    condition = &#064;Siren4JCondition(name = "somefield", logic=Is.TRUE) 
 *    
 *    <table border="1">
 *       <thead>
 *          <tr><th>Property</th><th>Required</th><th>Description</th></tr>
 *       </thead>
 *       <tbody>
 *          <tr><td>name</td><td>yes</td><td>Name of the field or method to be eveluated.</td></tr>
 *          <tr><td>logic</td><td>no</td><td>The logic type see {@link Is}, defaults to {@link Is.TRUE}</td></tr>
 *          <tr><td>type</td><td>no</td><td>The condition type see {@link Type}, defaults to {@link Type.FIELD}</td></tr>
 *          </tbody>
 *     </table>
 * </pre>
 * </code>
 * 
 * 
 * @since 1.0.4
 *
 */
@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@com.google.code.siren4j.annotations.Siren4JAnnotation
public @interface Siren4JCondition {
    Is logic() default Is.TRUE;
    Type type() default Type.FIELD;
    String name();    
    
    /**
     * The condition logic type.
     */
    public enum Is {
        EMPTY,
        FALSE,
        TRUE,
        NULL,
        NOTEMPTY,
        NOTNULL
    }
    
    /**
     * The condition type enum to indicate from where the value to be eveluated
     * comes from.
     */
    public enum Type {
        FIELD,
        METHOD
    }
}
