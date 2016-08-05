package com.trapezateam.trapeza.adapters;

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
    CategoryAdapter mCategoryAdapter;

    RealmBaseAdapter mCurrentAdapter;

    Category mCurrentCategory;


    private OnAddDishClickListener mOnAddDishClickListener;
    private OnAddCategoryClickListener mOnAddCategoryClickListener;

    /**
     * The first adapter to be used is the <code>CategoryAdapter</code>
     *
     * @param dishAdapter
     * @param categoryAdapter
     */
    public MenuAdapter(DishAdapter dishAdapter, CategoryAdapter categoryAdapter) {
        mDishAdapter = dishAdapter;
        mCategoryAdapter = categoryAdapter;

        mDishAdapter.setMenuAdapter(this);
        mCategoryAdapter.setMenuAdapter(this);

        mCurrentAdapter = mCategoryAdapter;
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


    void onDishClicked(Dish dish, View view) {

    }

    void onCategoryClicked(Category category) {
        mCurrentCategory = category;
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
        mCurrentAdapter = mCategoryAdapter;
        notifyDataSetChanged();
    }


    public void setOnAddDishClickListener(OnAddDishClickListener listener) {
        mOnAddDishClickListener = listener;
    }

    public void removeOnAddDishClickListener() {
        mOnAddDishClickListener = null;
    }

    public void onAddDishClicked() {
        if (mOnAddDishClickListener != null) {
            mOnAddDishClickListener.onAddDishClicked(mCurrentCategory);
        }
    }

    public void onAddCategoryClicked(View view) {
        if (mOnAddCategoryClickListener != null) {
            mOnAddCategoryClickListener.onAddCategoryClicked(view);
        }
    }

    public void setOnAddCategoryClickListener(OnAddCategoryClickListener listener) {
        mOnAddCategoryClickListener = listener;
    }

    public void removeOnAddCategoryClickListener() {
        mOnAddCategoryClickListener = null;
    }

    /**
     * @return true if actually went back
     */
    public boolean goBack() {
        if (mCurrentAdapter == mDishAdapter) {
            switchToCategories();
            return true;
        }
        return false;
    }
}
