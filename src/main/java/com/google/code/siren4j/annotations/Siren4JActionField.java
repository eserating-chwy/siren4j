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
 * Annotation to define a field for an action.
 * 
 * <pre>
 * <code>
 * Usage:
 * 
 *     &#064;Siren4JActionField(name = "body", type = "text", required = true, maxLength = 250)
 * 
 *     <table border="1">
 *       <thead>
 *          <tr><th>Property</th><th>Required</th><th>Description</th></tr>
 *       </thead>
 *       <tbody>
 *          <tr><td>name</td><td>yes</td><td>Unique name for the field.</td></tr>
 *          <tr><td>fieldClass</td><td>no</td><td>Array of string to classify the field.</td></tr>
 *          <tr><td>type</td><td>no</td><td>HTML5 form field type, see below for allowed types.</td></tr>
 *          <tr><td>value</td><td>no</td><td>Default value for the field.</td></tr>
 *          <tr><td>pattern</td><td>no</td><td>HTML5 validation pattern.</td></tr>
 *          <tr><td>required</td><td>no</td><td>Required validation, true or false, defaults to false</td></tr>
 *          <tr><td>min</td><td>no</td><td>Validation for minimum numerical value. Type must equal 'number'</td></tr>
 *          <tr><td>max</td><td>no</td><td>Validation for maximum numerical value. Type must equal 'number'</td></tr>
 *          <tr><td>maxLength</td><td>no</td><td>Validation for maximum string length.</td></tr>
 *          <tr><td>step</td><td>no</td><td>The step attribute indicates the granularity that is
 *           expected (and required) of the value, by limiting the allowed values. Type must equal 'number'</td></tr>
 *          <tr><td>options</td><td>no</td><td>One or more {@link Siren4JFieldOption} annotation</td></tr>
 *          <tr><td>optionsURL</td><td>no</td><td>URL that points to web resource that return options.</td></tr>
 *       </tbody>
 *     </table>
 *     
 *     <b>Allowed HTML5 field types:</b>
 *     hidden, text, search, tel, url, email, password, datetime, date, date, month, week, time, datetime-local,
 *     number, range, color, checkbox, radio, file, submit, image, reset, button
 *     
 *     This annotation is used within a {@link Siren4JAction}.
 *     
 * </code>
 * </pre>
 *
 */
@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@com.google.code.siren4j.annotations.Siren4JAnnotation
public @interface Siren4JActionField {
    String name();

    String[] fieldClass() default {};

    String type() default "";

    String value() default "";

    String pattern() default "";

    boolean required() default false;

    int min() default -1;

    int max() default -1;

    int maxLength() default -1;

    int step() default -1;

    Siren4JFieldOption[] options() default {};

    String optionsURL() default "";
}
