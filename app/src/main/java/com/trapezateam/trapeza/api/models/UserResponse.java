package com.trapezateam.trapeza.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yuriy on 6/27/2016.
 */
public class UserResponse {

    @SerializedName("name")
    private String mName;

    @SerializedName("surname")
    private String mSurname;

    @SerializedName("role")
    private int mRole;

    @SerializedName("company")
    private int mCompany;

    @SerializedName("id")
    private int mId;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("photo")
    private String mPhotoUrl;

    @SerializedName("phone")
    private String phone;

    public int getRole() {
        return mRole;
    }

    public int getCompany() {
        return mCompany;
    }

    public String getSurname() {
        return mSurname;
    }

    public String getName() {
        return mName;
    }

    public int getId() {
        return mId;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }


    public String getPhone() {
        return phone;
    }
}
