package com.trapezateam.trapeza;

import com.trapezateam.trapeza.database.Dish;

/**
 * Created by ilgiz on 7/25/16.
 */
public interface OnDishClickedListener extends CategoryClickedListener {

    void onDishClicked(Dish dish);

}
