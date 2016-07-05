package com.trapezateam.trapeza;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trapezateam.trapeza.models.Dish;

import butterknife.Bind;

public class DishButton extends Button {

    final String TAG = "DishButton";

    private Dish mDish;

    public DishButton(Context context) {
        super(context);
    }

    public DishButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setText(Dish d) {
        mDish = d;
        String dishText = mDish.getName() + System.getProperty("line.separator") + mDish.getPrice() + " руб";
        this.setText(dishText);
    }

    public DishButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    public Dish getDish() {
        return mDish;
    }
}