package com.trapezateam.trapeza.database;

import android.os.Parcel;
import android.os.Parcelable;

import com.trapezateam.trapeza.api.models.CategoryResponse;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created ${PACKAGE_NAME} in Trapeza
 * Created by Yuriy on 7/5/2016.
 */
public class Category extends RealmObject implements Parcelable {
    @PrimaryKey
    private int categoryId;
    private String name;
    private String photoURL;


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
        this.categoryId = categoryId;
    }


    public RealmResults<Dish> getDishes() {
        RealmResults<Dish> dishes = Realm.getDefaultInstance().where(Dish.class).equalTo("categoryId", this.categoryId).findAll();
        return dishes;
    }


    @Override
    public String toString() {
        return name + System.lineSeparator() + String.valueOf(getDishes().size());
    }

    public void setData(CategoryResponse categoryResponse) {
        categoryId = categoryResponse.getId();
        name = categoryResponse.getName();
        photoURL = categoryResponse.getPhotoURL();
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

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
