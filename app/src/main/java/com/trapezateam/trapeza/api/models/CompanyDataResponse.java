package com.trapezateam.trapeza.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuriy on 8/3/2016.
 */
public class CompanyDataResponse {
    @SerializedName("companyId")
    int companyId;

    public ArrayList<UserResponse> getUsers() {
        return users;
    }

    public ArrayList<DishResponse> getDishes() {
        return dishes;
    }

    public ArrayList<CategoryResponse> getCategories() {
        return categories;
    }

    public int getCompanyId() {
        return companyId;
    }

    @SerializedName("users")
    ArrayList<UserResponse> users;

    @SerializedName("categories")
    ArrayList<CategoryResponse> categories;

    @SerializedName("dishes")
    ArrayList<DishResponse> dishes;
}
