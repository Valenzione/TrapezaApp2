package com.trapezateam.trapeza.api.models;

import com.google.gson.annotations.SerializedName;
import com.trapezateam.trapeza.models.Category;

/**
 * Created by Yuriy on 7/4/2016.
 */
public class CategoryResponse {

    @SerializedName("r_id")
    private int mId;

    @SerializedName("r_name")
    private String mName;

    public String getName() {
        return mName;
    }

    public int getId() {
        return mId;
    }

    public Category getCategory() {
        Category c = new Category(this);
        return c;
    }

    @Override
    public String toString() {
        return "CategoryResponse{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                '}';
    }
}
