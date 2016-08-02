package com.trapezateam.trapeza.database;

import com.trapezateam.trapeza.api.models.CategoryResponse;

import io.realm.Realm;
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
    private RealmList<Dish> dishes = new RealmList<>();


    public Category() {

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
        Realm realm = Realm.getDefaultInstance();
        realm.insertOrUpdate(this);
    }

    public void addToDishes(Dish dish) {
        if (!dishes.contains(dish)) {
            dishes.add(dish);
            Realm realm = Realm.getDefaultInstance();
            realm.insertOrUpdate(this);
        }
    }

    @Override
    public String toString() {
        return name + System.lineSeparator().toString() + String.valueOf(dishes.size());
    }

    public void setData(CategoryResponse categoryResponse) {
        categoryId = categoryResponse.getId();
        name = categoryResponse.getName();
    }
}
