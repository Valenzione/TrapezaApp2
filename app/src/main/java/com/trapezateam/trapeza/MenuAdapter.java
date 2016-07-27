package com.trapezateam.trapeza;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.trapezateam.trapeza.database.Category;
import com.trapezateam.trapeza.database.Dish;

import io.realm.RealmBaseAdapter;

/**
 * Created by Yuriy on 7/22/2016.
 */
public class MenuAdapter extends BaseAdapter {

    DishAdapter mDishAdapter;
    CategoriesAdapter mCategoriesAdapter;

    RealmBaseAdapter mCurrentAdapter;

    /**
     * The first adapter to be used is the <code>CategoriesAdapter</code>
     *
     * @param dishAdapter
     * @param categoriesAdapter
     */
    public MenuAdapter(DishAdapter dishAdapter, CategoriesAdapter categoriesAdapter) {
        mDishAdapter = dishAdapter;
        mCategoriesAdapter = categoriesAdapter;

        mDishAdapter.setMenuAdapter(this);
        mCategoriesAdapter.setMenuAdapter(this);

        mCurrentAdapter = mCategoriesAdapter;
    }

    @Override
    public int getCount() {
        return mCurrentAdapter.getCount();
    }

    @Override
    public Object getItem(int i) {
        return mCurrentAdapter.getItem(i);
    }

    @Override
    public long getItemId(int i) {
        return mCurrentAdapter.getItemId(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return mCurrentAdapter.getView(i, view, viewGroup);
    }

    void onDishClicked(Dish dish) {

    }

    void onCategoryClicked(Category category) {
        switchToDishesOfCategory(category);
    }

    void onBackClicked() {
        switchToCategories();
    }

    void switchToDishesOfCategory(Category category) {
        mDishAdapter.updateData(category.getDishes());
        mCurrentAdapter = mDishAdapter;
        notifyDataSetChanged();
    }

    void switchToCategories() {
        mCurrentAdapter = mCategoriesAdapter;
        notifyDataSetChanged();
    }
}
