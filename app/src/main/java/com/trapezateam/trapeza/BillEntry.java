package com.trapezateam.trapeza;

import android.os.Parcel;
import android.os.Parcelable;

import com.trapezateam.trapeza.database.Dish;

/**
 * Created by ilgiz on 6/18/16.
 */
public class BillEntry implements Parcelable {



    private Dish mDish;
    private int mQuantity;

    public BillEntry(Dish dish, int quantity) {
        mDish = dish;
        mQuantity = quantity;
    }

    private BillEntry(Parcel parcel) {
        mQuantity = parcel.readInt();
        mDish = parcel.readParcelable(Dish.class.getClassLoader());
    }

    public int getPrice() {
        return mQuantity * mDish.getPrice();
    }

    public void incrementQuantity() {
        mQuantity++;
    }

    public void decrementQuantity() {
        mQuantity--;
    }

    /**
     *
     * @return true if <code>{@link #mQuantity} = 0 </code>
     */
    public boolean isEmpty() {
        return mQuantity == 0;
    }

    public Dish getDish() {
        return mDish;
    }

    public int getQuantity() {
        return mQuantity;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mQuantity);
        parcel.writeValue(mDish);
    }

    public static final Parcelable.Creator<BillEntry> CREATOR
            = new Parcelable.Creator<BillEntry>() {

        @Override
        public BillEntry createFromParcel(Parcel parcel) {
            return new BillEntry(parcel);
        }

        @Override
        public BillEntry[] newArray(int i) {
            return new BillEntry[i];
        }
    };


}
