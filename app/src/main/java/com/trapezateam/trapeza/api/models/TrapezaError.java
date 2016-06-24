package com.trapezateam.trapeza.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ilgiz on 6/22/16.
 */
public class TrapezaError {

    /*
     * For
     * http://blog.robinchutaux.com/blog/a-smart-way-to-use-retrofit/
     * error handling
     */

    @SerializedName("code")
    String mCode;

    @SerializedName("message")
    String mMessage;

    public TrapezaError(String message)
    {
        mMessage = message;
    }


    public String getCode() {
        return mCode;
    }

    public void setCode(String mCode) {
        this.mCode = mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }
}
