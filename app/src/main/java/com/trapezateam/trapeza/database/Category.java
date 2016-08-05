package com.trapezateam.trapeza.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.trapezateam.trapeza.api.models.CategoryResponse;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Yuriy on 7/5/2016.
 */
public class Category extends RealmObject implements Parcelable {
    @PrimaryKey
    private int categoryId;
    private String name;
    private RealmList<Dish> dishes = new RealmList<>();


    public Category() {

    }


    protected Category(Parcel in) {
        categoryId = in.readInt();
        name = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

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
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        this.categoryId = categoryId;
        realm.commitTransaction();
        realm.insertOrUpdate(this);

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(categoryId);
        parcel.writeString(name);
    }
}
