package com.trapezateam.trapeza.models;

import com.trapezateam.trapeza.api.models.CategoryResponse;

import java.util.ArrayList;

/**
 * Created by Yuriy on 6/21/2016.
 */
public class Category {
    private String mName;
    private int mId;
    private ArrayList<Dish> mDishes;

    public ArrayList<Dish> getDishes() {
        return mDishes;
    }

    private Category(){
        mDishes=new ArrayList<>();
    }


    public Category(CategoryResponse c) {
        this();
        mName = c.getName();
        mId = c.getId();
    }

    public String getName() {
        return mName;
    }

    public int getId() {
        return mId;
    }

    public void addDish(Dish dish) {
        mDishes.add(dish);
    }
}
