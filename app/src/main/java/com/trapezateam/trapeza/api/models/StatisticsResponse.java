package com.trapezateam.trapeza.api.models;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by ilgiz on 8/9/16.
 */
public class StatisticsResponse {

    @SerializedName("file")
    private String mFile;

    @SerializedName("message")
    private String mMessage;

    public String getFile() {
        return mFile;
    }

    public String getMessage() {
        return mMessage;
    }
}
