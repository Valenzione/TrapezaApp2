package com.trapezateam.trapeza;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trapezateam.trapeza.database.Category;
import com.trapezateam.trapeza.database.Dish;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Yuriy on 7/22/2016.
 */
public class CategoriesAdapter
        extends RealmBaseAdapter<Category> {


    private OnCategoryClickedListener mOnCategoryClickedListener;
    private MenuAdapter mMenuAdapter;

    public CategoriesAdapter(@NonNull Context context,
                             @Nullable OrderedRealmCollection<Category> data) {
        super(context, data);
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.square_button, viewGroup, false);
        }

        SquareButton button = (SquareButton) view;
        button.setText(getItem(i).getName());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnCategoryClickedListener != null) {
                    mOnCategoryClickedListener.onCategoryClicked(getItem(i));
                }
                if(mMenuAdapter != null) {
                    mMenuAdapter.onCategoryClicked(getItem(i));
                }
            }
        });
        return button;
    }

    public void setOnCategoryClickedListener(OnCategoryClickedListener listener) {
        mOnCategoryClickedListener = listener;
    }

    public void removeOnCategoryClickedListener() {
        mOnCategoryClickedListener = null;
    }

    public void setMenuAdapter(MenuAdapter menuAdapter) {
        mMenuAdapter = menuAdapter;
    }

    public void removeMenuAdapter() {
        mMenuAdapter = null;
    }
}
