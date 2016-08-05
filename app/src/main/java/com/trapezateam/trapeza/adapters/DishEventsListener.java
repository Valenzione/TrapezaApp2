package com.trapezateam.trapeza.adapters;

import android.view.View;

import com.trapezateam.trapeza.database.Dish;

/**
 * Created by ilgiz on 7/25/16.
 */
public interface DishEventsListener {

    /**
     * @param dish
     * @param view button which was clicked
     */
    void onDishClicked(Dish dish, View view);

    /**
     * @param dish
     * @param view the button which was long-clicked
     * @return <code>true</code> if the long-click was consumed
     */
    boolean onDishLongClicked(Dish dish, View view);

}
