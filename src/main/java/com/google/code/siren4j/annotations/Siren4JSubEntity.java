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
 * Annotation to define a sub-entity. Typically used on a resource collection field, but can also be on
 * a single type entity field. Values set in this annotation override values set by the entities own
 * {@link Siren4JEntity} annotation.
 * 
 * <pre>
 * <code>
 * Usage:
 * 
 *      &#064;Siren4JSubEntity(rel = "authors", uri = "/authors?courseid={parent.courseid}")
 * 
 *     <table border="1">
 *       <thead>
 *          <tr><th>Property</th><th>Required</th><th>Description</th></tr>
 *       </thead>
 *       <tbody>
 *          <tr><td>rel</td><td>no</td><td>Array of strings that indicate the subentites relationship with its parent.</td></tr>
 *          <tr><td>uri</td><td>no</td><td>URI pattern to self</td></tr>
 *          <tr><td>type</td><td>no</td><td>Defines media type of the linked resource, per Web Linking (RFC5899). Used if embedded link.</td></tr>
 *          <tr><td>links</td><td>no</td><td>One or more {@link Siren4JLink} annotations.</td></tr>
 *          <tr><td>actions</td><td>no</td><td>One or more {@link Siren4JAction} annotations.</td></tr>
 *          <tr><td>embeddedLink</td><td>no</td><td>Marks the subentity as an embedded link, defaults to false.</td></tr>
 *          
 *       </tbody>
 *     </table>     
 *     
 * </code>
 * </pre>
 *
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@com.google.code.siren4j.annotations.Siren4JAnnotation
public @interface Siren4JSubEntity {
    String[] rel() default {};
    String uri() default "";
    String type() default "";
    boolean embeddedLink() default false;
    Siren4JLink[] links() default {};
    Siren4JAction[] actions() default {};
}
