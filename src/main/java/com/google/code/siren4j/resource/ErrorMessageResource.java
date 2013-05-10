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
