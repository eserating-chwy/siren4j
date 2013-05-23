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
package com.google.code.siren4j.resource;

import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.annotations.Siren4JInclude;
import com.google.code.siren4j.annotations.Siren4JInclude.Include;

/**
 * Error message resource to send detailed error messages back in the Siren format.
 */
@Siren4JInclude(Include.NON_EMPTY)
@Siren4JEntity(name = "errorMessage")
public class ErrorMessageResource extends BaseResource{

    private int status = 500;
    private int errorCode;
    private String message = "An unknown server error has occurred.";
    private String[] developerMessage;
    private String moreInfoUrl;
    
    public ErrorMessageResource() {
        
    }    

    public ErrorMessageResource(int status, int errorCode, String message) {
        this(status, errorCode, message, null);
    }

    public ErrorMessageResource(int status, int errorCode, String message, String[] developerMessage) {
        this(status, errorCode, message, developerMessage, null);
    }

    public ErrorMessageResource(int status, int errorCode, String message, String[] developerMessage, String moreInfoUrl) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.developerMessage = developerMessage;
        this.moreInfoUrl = moreInfoUrl;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String[] developerMessage) {
        this.developerMessage = developerMessage;
    }

    public String getMoreInfoUrl() {
        return moreInfoUrl;
    }

    public void setMoreInfoUrl(String moreInfoUrl) {
        this.moreInfoUrl = moreInfoUrl;
    }
    
    

}
