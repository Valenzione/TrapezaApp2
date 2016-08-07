package com.trapezateam.trapeza.api.models;

import com.google.gson.annotations.SerializedName;
import com.trapezateam.trapeza.database.Category;

/**
 * Created by Yuriy on 7/4/2016.
 */
public class CategoryResponse {

    @SerializedName("r_id")
    private int mId;

    @SerializedName("r_name")
    private String mName;

    @SerializedName("r_photo")
    private String mPhotoURL;

    public String getName() {
        return mName;
    }

    public int getId() {
        return mId;
    }



    @Override
    public String toString() {
        return "CategoryResponse{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                '}';
    }

    public String getPhotoURL() {
        return mPhotoURL;
    }
}
