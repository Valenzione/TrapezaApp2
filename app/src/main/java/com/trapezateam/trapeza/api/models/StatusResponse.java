package com.trapezateam.trapeza.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yuriy on 8/2/2016.
 */
public class StatusResponse {
    @SerializedName("success")
    private boolean mSuccess;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("err")
    private String mError;

    public boolean isSuccess() {
        return mSuccess;
    }

    public String getMessage() {
        return mMessage != null ? mMessage : mError;
    }


}
