package com.trapezateam.trapeza;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.trapezateam.trapeza.database.Category;

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
        String mCategoryName = mCategory.getName();
        this.setText(mCategoryName);
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