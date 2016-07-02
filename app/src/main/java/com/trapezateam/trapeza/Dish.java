package com.trapezateam.trapeza;

import android.media.Image;

/**
 * Created by Yuriy on 7/2/2016.
 */
public class Dish {
    private int mPrice;
    private String mName;
    private String mDescription;
    private Image mImage;

    public Dish(String name, String description, int price) {
        mPrice = price;
        mDescription = description;
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int mPrice) {
        this.mPrice = mPrice;
    }

    public Image getImage() {
        return mImage;
    }

    @Override
    public String toString() {
        String out = getName() + " " + getDescription() + " " + getPrice();
        return out;
    }
}
