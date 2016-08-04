package com.trapezateam.trapeza.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yuriy on 8/4/2016.
 */
public class SaveCompleteResponse extends StatusResponse {
    @SerializedName("id")
    private int id;

    public int getId() {
        return id;
    }
}
