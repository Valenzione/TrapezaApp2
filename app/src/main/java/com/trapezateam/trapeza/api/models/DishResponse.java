package com.trapezateam.trapeza.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ilgiz on 6/22/16.
 */
public class DishResponse {

    @SerializedName("r_id")
    private int mId;

    @SerializedName("r_id_price")
    private int mPriceId;

    @SerializedName("r_name")
    private String mName;

    @SerializedName("r_price")
    private String mPrice;

    @SerializedName("r_photo")
    private String mPhoto;

    @SerializedName("r_description")
    private String mDescription;

    @SerializedName("r_category_id")
    private Integer mFather;

    @SerializedName("r_category_name")
    private String mFatherName;


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getPriceId() {
        return mPriceId;
    }

    public void setPriceId(int priceId) {
        mPriceId = priceId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public void setPhoto(String photo) {
        mPhoto = photo;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Integer getFather() {
        return mFather;
    }

    public void setFather(Integer father) {
        mFather = father;
    }

    public String getFatherName() {
        return mFatherName;
    }

    @Override
    public String toString() {
        return "DishResponse{" +
                "mId=" + mId +
                ", mPriceId=" + mPriceId +
                ", mName='" + mName + '\'' +
                ", mPrice='" + mPrice + '\'' +
                ", mPhoto='" + mPhoto + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mFather=" + mFather +
                ", mFatherName='" + mFatherName + '\'' +
                '}';
    }

    public void setFatherName(String fatherName) {
        mFatherName = fatherName;
    }
}
