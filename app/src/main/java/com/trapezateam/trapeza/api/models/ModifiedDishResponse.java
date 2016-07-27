package com.trapezateam.trapeza.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yuriy on 7/27/2016.
 */
public class ModifiedDishResponse {
    @SerializedName("modifydish")
    private int mStatus;

    public int getStatus() {
        return mStatus;
    }
}
