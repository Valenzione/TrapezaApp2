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
    private int mPrice;

    public Dish(String name, int price) {
        mName = name;
        mPrice = price;
    }

    public Dish(DishResponse response) {
        mName = response.getName();
        mId = response.getId();
        mPhoto = response.getPhoto();
        mPrice = Integer.parseInt(response.getPrice());
    }

    public int getPrice() {
        return mPrice;
    }


    public String getName() {
        return mName;
    }

}
