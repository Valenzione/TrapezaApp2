package com.trapezateam.trapeza.database;

import com.trapezateam.trapeza.models.*;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Yuriy on 7/5/2016.
 */
public class Category extends RealmObject {
    @PrimaryKey
    private int categoryId;
    private String name;
    private RealmList<Dish> dishes;

    public Category(com.trapezateam.trapeza.models.Category category) {
        this.categoryId=category.getId();
        this.name=category.getName();
    }

    public Category(){

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }


    public RealmList<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(RealmList<Dish> dishes) {
        this.dishes = dishes;
    }

    public void addToDishes(Dish dish) {
        if (!dishes.contains(dish)) {
            dishes.add(dish);
        }
    }
}