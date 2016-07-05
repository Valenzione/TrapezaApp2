package com.trapezateam.trapeza;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.trapezateam.trapeza.models.Category;
import com.trapezateam.trapeza.models.Dish;

public class CategoryButton extends Button {

    final String TAG = "CategoryButton";

    private Category mCategory;

    public CategoryButton(Context context) {
        super(context);
    }

    public CategoryButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setText(Category c) {
        mCategory = c;
        String dishText = mCategory.getName();
        this.setText(dishText);
    }

    public CategoryButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    public Category getCategory() {
        return mCategory;
    }
}