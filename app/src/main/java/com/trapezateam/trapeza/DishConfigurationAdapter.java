package com.trapezateam.trapeza;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.trapezateam.trapeza.models.Dish;
import com.trapezateam.trapeza.models.HashMapMenu;

/**
 * Created by Yuriy on 7/5/2016.
 */
public class DishConfigurationAdapter extends DishAdapter {
    public DishConfigurationAdapter(HashMapMenu mMenuTree, Bill mBill, GridView mMenu, int position) {
        super(mMenuTree, mBill, mMenu, position);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            Context context = viewGroup.getContext();
            view = new DishButton(context);
        }

        final DishButton button = (DishButton) view;
        if (position == 0) {
            button.setText("Back");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMenu.setAdapter(new CategoryConfigurationAdapter(hashMapMenu, mMenu, mBill));
                }
            });
        } else {


            button.setText((Dish) getItem(position));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("DCA","DishButtonPressed");
                }
            });


        }
        return button;
    }
}
