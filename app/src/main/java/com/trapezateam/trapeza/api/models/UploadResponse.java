package com.trapezateam.trapeza.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created com.trapezateam.trapeza.api.models in Trapeza
 * Created by Yuriy on 8/5/2016.
 */
public class UploadResponse extends StatusResponse {
   @SerializedName("path")
    private String path;

    public String getPath() {
        return path;
    }
}
