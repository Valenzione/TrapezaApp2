package com.trapezateam.trapeza;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trapezateam.trapeza.database.Dish;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by Yuriy on 7/22/2016.
 */
public class DishAdapter
        extends RealmBaseAdapter<Dish> {

    private OnDishClickedListener mOnDishClickedListener;
    private MenuAdapter mMenuAdapter;
    private boolean mShowAddButton;

    public DishAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Dish> data) {
        super(context, data);
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.square_button, viewGroup, false);
        }
        SquareButton button = (SquareButton) view;

        if (i == 0) {
            button.setText("<---");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mMenuAdapter != null) {
                        mMenuAdapter.onBackClicked();
                    }
                }
            });
            return button;
        }

        if (mShowAddButton && i == getCount() - 1) {
            button.setText("+");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMenuAdapter.onAddDishClicked();
                }
            });
            return button;
        }

        final Dish dish = getItem(i - 1);
        button.setText(dish.getName());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnDishClickedListener != null) {
                    mOnDishClickedListener.onDishClicked(dish);
                }
                if (mMenuAdapter != null) {
                    mMenuAdapter.onDishClicked(dish);
                }
            }
        });

        return button;

    }

    public void setOnDishClickedListener(OnDishClickedListener listener) {
        mOnDishClickedListener = listener;
    }

    public void removeOnDishClickedListener() {
        mOnDishClickedListener = null;
    }

    public void setMenuAdapter(MenuAdapter menuAdapter) {
        mMenuAdapter = menuAdapter;
    }

    public void removeMenuAdapter() {
        mMenuAdapter = null;
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        count++; // back button
        if (mShowAddButton) {
            count++; // add button
        }
        return count;
    }

    public void setShowAddButton(boolean showAddButton) {
        if (showAddButton != mShowAddButton) {
            mShowAddButton = showAddButton;
            notifyDataSetChanged();
        }
    }

    public boolean getShowAddButton() {
        return mShowAddButton;
    }
}
