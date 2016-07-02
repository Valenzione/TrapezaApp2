package com.trapezateam.trapeza.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yuriy on 6/27/2016.
 */
public class UserResponse {

    @SerializedName("r_name")
    private String mName;

    @SerializedName("r_surname")
    private String mSurname;

    @SerializedName("r_role")
    private int mRole;

    @SerializedName("r_company")
    private int mCompany;


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
}
