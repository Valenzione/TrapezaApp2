package com.trapezateam.trapeza.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.trapezateam.trapeza.api.models.DishResponse;

/**
 * Created by ilgiz on 6/18/16.
 */
public class Dish implements Parcelable {

    private String mName;
    private int mId;
    private String mPhoto;
    private String mDescription;
    private int mPrice;

    public Dish(String name, int price, int id) {
        mName = name;
        mPrice = price;
        mId = id;
    }

    public Dish(String name, String description, int price) {
        mName = name;
        mDescription = description;
        mPrice = price;
    }


    public Dish(DishResponse response) {
        mName = response.getName();
        mId = response.getId();
        mPhoto = response.getPhoto();
        mPrice = Integer.parseInt(response.getPrice());
        mDescription = response.getDescription();
    }

    protected Dish(Parcel in) {
        mName = in.readString();
        mId = in.readInt();
        mPhoto = in.readString();
        mDescription = in.readString();
        mPrice = in.readInt();
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
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Dish)) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeInt(mId);
        parcel.writeString(mPhoto);
        parcel.writeString(mDescription);
        parcel.writeInt(mPrice);
    }

    public static final Creator<Dish> CREATOR = new Creator<Dish>() {
        @Override
        public Dish createFromParcel(Parcel in) {
            return new Dish(in);
        }

        @Override
        public Dish[] newArray(int size) {
            return new Dish[size];
        }
    };
}
