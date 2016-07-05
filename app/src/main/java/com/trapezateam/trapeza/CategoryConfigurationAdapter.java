package com.trapezateam.trapeza;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.trapezateam.trapeza.api.models.CategoryResponse;
import com.trapezateam.trapeza.models.Category;
import com.trapezateam.trapeza.models.HashMapMenu;

import java.util.List;

/**
 * Created by Yuriy on 7/5/2016.
 */
public class CategoryConfigurationAdapter extends CategoryAdapter {

    public CategoryConfigurationAdapter(List<CategoryResponse> categories) {
        super(categories);
    }

    public CategoryConfigurationAdapter(HashMapMenu menuTree, GridView gv, Bill b) {
        super(menuTree, gv, b);
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
                DishConfigurationAdapter dishAdapter = new DishConfigurationAdapter(mMenuTree, mBill, mMenu,position);
                mMenu.setAdapter(dishAdapter);
            }
        });
        return button;
    }
}
