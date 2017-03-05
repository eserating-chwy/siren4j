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
 * Annotation that defines an action to be added to the entity.
 * 
 * <pre>
 * <code>
 * 
 *      &#064;Siren4JAction(
 *         name = "addReview",
 *         title = "Add a Review",
 *         method = Method.POST,
 *         href = "/courseReviews/course/{courseid}",
 *         type = "application/json",
 *         fields = {
 *       	    &#064;Siren4JActionField(name = "userid", type = "text", required = true ),
 *       	    &#064;Siren4JActionField(name = "body", type = "text", required = true, maxLength = 250)
 *         }
 *         condition = &#064;Siren4JCondition(name = "somefield", logic=Is.TRUE) 
 *      )
 *      
 *      <table border="1">
 *       <thead>
 *          <tr><th>Property</th><th>Required</th><th>Description</th></tr>
 *       </thead>
 *       <tbody>
 *          <tr><td>name</td><td>yes</td><td>Unique name for the action.</td></tr>
 *          <tr><td>href</td><td>yes</td><td>URI of the action.</td></tr>
 *          <tr><td>method</td><td>no</td><td>Method for the action {@link Method}, defaults to Method.GET</td></tr>
 *          <tr><td>actionClass</td><td>no</td><td>Array of string to classify the action.</td></tr>
 *          <tr><td>title</td><td>no</td><td>Display title for the action.</td></tr>
 *          <tr><td>fields</td><td>no</td><td>One or more {@link Siren4JActionField} annotation</td></tr>
 *          <tr><td>urlParams</td><td>no</td><td>One or more {@link Siren4JActionField} annotation</td></tr>
 *          <tr><td>headers</td><td>no</td><td>One or more {@link Siren4JActionField} annotation</td></tr>
 *          <tr><td>condition</td><td>no</td><td>A condition must evaluate to <code>true</code> for the action to be rendered.</td></tr>
 *          <tr><td>metaData</td><td>no</td><td>One or more {@link Siren4JMetaData} annotation</td></tr>
 *       </tbody>
 *     </table>
 *      
 *      This annotation is used within a {@link Siren4JEntity} or {@link Siren4JSubEntity}. *         
 *  
 * </code>
 * </pre>
 *
 */
@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@com.google.code.siren4j.annotations.Siren4JAnnotation
public @interface Siren4JAction {
    String name();

    String[] actionClass() default {};

    Method method() default Method.GET;

    String href();

    String title() default "";

    String type() default "";

    Siren4JActionField[] fields() default {};

    Siren4JActionField[] urlParams() default {};

    Siren4JActionField[] headers() default {};
    
    Siren4JCondition condition() default @Siren4JCondition(name="null");

    Siren4JMetaData[] metaData() default {};
}
