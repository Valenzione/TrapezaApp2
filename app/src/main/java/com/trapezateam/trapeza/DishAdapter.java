package com.trapezateam.trapeza;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.trapezateam.trapeza.api.models.DishResponse;
import com.trapezateam.trapeza.models.Dish;
import com.trapezateam.trapeza.models.HashMapMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuriy on 7/4/2016.
 */
public class DishAdapter extends BaseAdapter {
    Bill mBill;
    ArrayList<Dish> mDishesList = new ArrayList<>();
    GridView mMenu;
    HashMapMenu hashMapMenu;

    public DishAdapter(List<DishResponse> dishes, Bill bill) {
        mBill = bill;
        for (DishResponse d : dishes) {
            mDishesList.add(new Dish(d));
        }
    }

    public DishAdapter(HashMapMenu menu, Bill bill, GridView gv, int position) {
        mBill = bill;
        mDishesList = menu.getCategories().get(position).getDishes();
        mMenu = gv;
        hashMapMenu = menu;
    }

    @Override
    public int getCount() {
        return mDishesList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDishesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mDishesList.get(i).getId();
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
                    mMenu.setAdapter(new CategoryAdapter(hashMapMenu, mMenu, mBill));
                }
            });
        } else {

            if (mBill != null) {
                button.setText((Dish) getItem(position));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mBill.addEntry(button.getDish());
                    }
                });
            } else {
                button.setText((Dish) getItem(position));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

            }
        }
        return button;
    }
}