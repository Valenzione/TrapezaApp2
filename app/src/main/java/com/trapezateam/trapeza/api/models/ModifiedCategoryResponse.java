package com.trapezateam.trapeza.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yuriy on 7/28/2016.
 */
public class ModifiedCategoryResponse {

    @SerializedName("modifycategory")
    private int mStatus;

    public int getStatus() {
        return mStatus;
    }
}
