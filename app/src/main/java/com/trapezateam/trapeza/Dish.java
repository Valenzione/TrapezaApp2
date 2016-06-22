package com.trapezateam.trapeza;

import android.net.Uri;

/**
 * Created by ilgiz on 6/18/16.
 */
public class Dish {

    private String mName;
    private int mId;
    private Uri mPhoto;
    private int mPrice;

    public Dish(String name, int price) {
        mName = name;
        mPrice = price;
    }



    public int getPrice() {
        return mPrice;
    }


    public String getName() {
        return mName;
    }

}
