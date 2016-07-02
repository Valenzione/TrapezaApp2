package com.trapezateam.trapeza.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yuriy on 6/29/2016.
 */
public class SavedDishResponse {
    @SerializedName("adddish")
    private int mStatus;

    public int getStatus() {
        return mStatus;
    }
}
