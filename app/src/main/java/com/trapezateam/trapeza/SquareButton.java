package com.trapezateam.trapeza;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.trapezateam.trapeza.database.Category;
import com.trapezateam.trapeza.database.Dish;

/**
 * Created by Yuriy on 7/5/2016.
 */
public class SquareButton extends Button {

    public SquareButton(Context context) {
        super(context);
    }

    public SquareButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setText(Dish d) {
        String dishText = d.getName() + System.getProperty("line.separator") + d.getPrice() + " руб";
        this.setText(dishText);
    }


    public void setText(Category c) {
        String dishText = c.getName();
        this.setText(dishText);
    }


    public SquareButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
