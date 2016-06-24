package com.trapezateam.trapeza.models;

import android.content.Intent;
import android.net.Uri;

import com.trapezateam.trapeza.api.models.DishResponse;

/**
 * Created by ilgiz on 6/18/16.
 */
public class Dish {

    private String mName;
    private int mId;
    private String mPhoto;
    String mDescription;
    private int mPrice;

    public Dish(String name, int price, int id) {
        mName = name;
        mPrice = price;
        mId = id;
    }

    public Dish(DishResponse response) {
        mName = response.getName();
        mId = response.getId();
        mPhoto = response.getPhoto();
        mPrice = Integer.parseInt(response.getPrice());
        mDescription = response.getDescription();
    }

    public int getPrice() {
        return mPrice;
    }

    public String getName() {
        return mName;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Dish)) {
            return false;
        }
        Dish d = (Dish) obj;

        return d.mId == mId;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "mName='" + mName + '\'' +
                ", mId=" + mId +
                ", mPhoto='" + mPhoto + '\'' +
                ", mPrice=" + mPrice +
                '}';
    }
}
