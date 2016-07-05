package com.trapezateam.trapeza;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.trapezateam.trapeza.api.models.CategoryResponse;
import com.trapezateam.trapeza.models.Category;
import com.trapezateam.trapeza.models.HashMapMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuriy on 7/4/2016.
 */
public class CategoryAdapter extends BaseAdapter {

    ArrayList<Category> mCategoriesList = new ArrayList<>();
    Bill mBill;
    GridView mMenu;
    HashMapMenu mMenuTree;

    public CategoryAdapter(List<CategoryResponse> categories) {
        for (CategoryResponse c : categories) {
            mCategoriesList.add(new Category(c));
        }
    }

    public CategoryAdapter(HashMapMenu menuTree, GridView gv, Bill b) {
        mCategoriesList = menuTree.getCategories();
        mBill = b;
        mMenu = gv;
        mMenuTree=menuTree;
    }

    @Override
    public int getCount() {
        return mCategoriesList.size();
    }

    @Override
    public Object getItem(int i) {
        return mCategoriesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mCategoriesList.get(i).getId();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            Context context = viewGroup.getContext();
            view = new CategoryButton(context);
        }
        CategoryButton button = (CategoryButton) view;
        button.setText(((Category) getItem(position)).getName());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DishAdapter dishAdapter = new DishAdapter(mMenuTree, mBill, mMenu,position);
                mMenu.setAdapter(dishAdapter);
            }
        });
        return button;
    }
}