package com.trapezateam.trapeza.api.models;

/**
 * Created by ilgiz on 6/22/16.
 */
public class TrapezaError {

    /*
     * For
     * http://blog.robinchutaux.com/blog/a-smart-way-to-use-retrofit/
     * error handling
     */

    String code;
    String message;

    public TrapezaError(String message)
    {
        this.message = message;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
