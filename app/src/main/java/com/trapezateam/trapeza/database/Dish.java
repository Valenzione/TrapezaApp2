package com.trapezateam.trapeza.database;

import com.trapezateam.trapeza.api.models.DishResponse;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Yuriy on 7/5/2016.
 */
public class Dish extends RealmObject {
    @PrimaryKey
    private int dishId;
    private String name;
    private String description;
    private int categoryId;
    private int price;

    public Dish() {
    }

    public Dish(DishResponse response) {
        dishId = response.getId();
        name = response.getName();
        description = response.getDescription();
        categoryId = response.getFather();
        price = Integer.parseInt(response.getPrice());
    }


    public Dish(String name, String description, int price) {
        // TODO: add category ID
        this.name = name;
        this.description = description;
        this.price = price;
    }


    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name + " " + description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Dish) {
            Dish d = (Dish) obj;
            return d.getDishId() == getDishId();
        }
        return super.equals(obj);
    }
}
