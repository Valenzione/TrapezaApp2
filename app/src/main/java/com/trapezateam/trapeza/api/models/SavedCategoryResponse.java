package com.trapezateam.trapeza.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yuriy on 6/29/2016.
 */
public class SavedCategoryResponse {
    @SerializedName("addcategory")
    private int mStatus;

    public int getStatus() {
        return mStatus;
    }
}
