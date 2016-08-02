package com.trapezateam.trapeza.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yuriy on 8/2/2016.
 */
public class DeletetedDishResponse {

    public int getStatus() {
        return mStatus;
    }

    @SerializedName("deletedish")
    int mStatus;
}
