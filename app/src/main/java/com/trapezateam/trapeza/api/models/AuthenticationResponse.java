package com.trapezateam.trapeza.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ilgiz on 6/21/16.
 */
public class AuthenticationResponse {

    @SerializedName("success")
    private boolean mSuccess;

    @SerializedName("message")
    private String mMessage;

    @SerializedName("token")
    private String mToken;

    @SerializedName("id")
    private int mId;

    public boolean isSuccess() {
        return mSuccess;
    }

    public void setSuccess(boolean success) {
        mSuccess = success;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    @Override
    public String toString() {
        return "AuthenticationResponse{" +
                "mSuccess=" + mSuccess +
                ", mMessage='" + mMessage + '\'' +
                ", mToken='" + mToken + '\'' +
                ", mId=" + mId +
                '}';
    }
}
