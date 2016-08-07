package com.trapezateam.trapeza;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.trapezateam.trapeza.database.Category;
import com.trapezateam.trapeza.database.Dish;

/**
 * Created by Yuriy on 7/5/2016.
 */
public class SquareButton extends Button implements Target {

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

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        Drawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
//        setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        setBackground(drawable);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
