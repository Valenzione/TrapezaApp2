package com.trapezateam.trapeza;

/**
 * Created by ilgiz on 6/18/16.
 */
public class BillEntry {

    private Dish mDish;
    private int mQuantity;

    public BillEntry(Dish dish, int quantity) {
        mDish = dish;
        mQuantity = quantity;
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



}
