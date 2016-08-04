package com.trapezateam.trapeza.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yuriy on 8/2/2016.
 */
public class StatusResponse {
    @SerializedName("success")
    private boolean mSuccess;

    public boolean isSuccess() {
        return mSuccess;
    }


}
