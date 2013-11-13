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
package com.google.code.siren4j.meta;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Field option is used to provide a list of choices available for a field that the client can  use to set the
 * field value.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldOption {

    /**
     * The title is the display string that can be displayed. (Optional)
     */
    private String title;

    /**
     * The value for this option. (Optional if title or data are not null)
     */
    private String value;

    /**
     * Extra meta data that may be useful for the client to use or display to make the right choice for this field.
     * (Optional)
     */
    private Map<String, String> data;
    @JsonProperty(value = "default")
    private boolean optionDefault;

    public FieldOption() {
    }

    public FieldOption(String title, String value, boolean optionDefault) {
        this(title, value, optionDefault, null);
    }

    public FieldOption(String title, String value, boolean optionDefault, Map<String, String> data) {
        this.title = title;
        this.value = value;
        this.optionDefault = optionDefault;
        this.data = data;
    }

    public FieldOption(String title, String value) {
       this(title, value, false);
    }

    public FieldOption(String title, String value, Map<String, String> data) {
        this(title, value, false, data);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isOptionDefault() {
        return optionDefault;
    }

    public void setOptionDefault(boolean optionDefault) {
        this.optionDefault = optionDefault;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public void putData(String key, String value) {
        if(data == null) {
            data = new HashMap<String, String>();
        }
        data.put(key, value);
    }
}
