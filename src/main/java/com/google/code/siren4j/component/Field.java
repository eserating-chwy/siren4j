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
package com.google.code.siren4j.component;

import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.code.siren4j.component.impl.FieldImpl;
import com.google.code.siren4j.meta.FieldOption;
import com.google.code.siren4j.meta.FieldType;
/**
 * Fields represent controls inside of actions.
 */
@JsonDeserialize(as = FieldImpl.class)
public interface Field extends Component {
    
    /**
     * A name describing the control. (Required).
     * @return never <code>null</code> or empty.
     */
    public String getName();
    
    /**
     * The field type. This may include any of the input types specified in HTML5.
     * default value is {@link FieldType.TEXT}. see {@link FieldType} for other allowed values (Optional).
     * @return never <code>null</code>. 
     */
    public FieldType getType();
    
    /**
     * A value assigned to the field. (Optional).
     * @return may be <code>null</code> or empty.
     */
    public String getValue();
    
    /**
     * The pattern specifies a regular expression which needs to match the fields value
     * in order for validation to be considered as passed.
     * @return may be <code>null</code> or empty.
     */
    public String getPattern();
    
    /**
     * Indicates if the field is required upon action submission.
     * @return <code>true</code> if required.
     */
    public boolean isRequired();
    
    /**
     * Specifies the minimum value for the following field types:
     * number, range, date, datetime, datetime-local, month, time and week.
     * @return -1 is considered as a not set state.
     */
    public int getMin();
     
    /**
     * Specifies the maximum value for the following field types:
     * number, range, date, datetime, datetime-local, month, time and week.
     * @return -1 is considered as a not set state.
     */
    public int getMax();
    
    /**
     * The maxlength attribute limits the number of characters that text field can accept.
     * @return -1 is considered as a not set state and means no limit on length.
     */
    public int getMaxLength();
    
    /**
     * The step attribute specifies the legal number intervals for an <input> element.
     *
     * Example: if step="3", legal numbers could be -3, 0, 3, 6, etc.
     * 
     * Used for the following field types:
     * number, range, date, datetime, datetime-local, month, time and week.
     * @return may be <code>null</code> if not set.
     */
    public Integer getStep();
    
    /**
     * The placeholder attribute specifies a short hint that describes the expected value
     * of an input field (e.g. a sample value or a short description of the expected format).
     *
     * The hint is displayed in the input field when it is empty, and disappears when the field 
     * gets focus.
     * 
     * Used for the following field types:
     * text, search, url, tel, email, and password
     * @return may be <code>null</code> or empty.
     */
    public String getPlaceholder();

    /**
     * Option values for the fields.
     * @return may be <code>null</code> or empty.
     */
    public List<FieldOption> getOptions();

    /**
     *  A url that points to a web resource that will return a list of options.
     * @return  may be <code>null</code> or empty.
     */
    public String getOptionsURL();

}