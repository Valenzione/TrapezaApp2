package com.trapezateam.trapeza.adapters;

import android.view.View;

import com.trapezateam.trapeza.database.Category;

/**
 * Created by ilgiz on 7/25/16.
 */
public interface CategoryEventsListener {

    /**
     *
     * @param category
     * @param view the button which was clicked
     */
    void onCategoryClicked(Category category, View view);

    /**
     *
     * @param category
     * @param view the button which was long-clicked
     * @return <code>true</code> if the long-click was consumed
     */
    boolean onCategoryLongClicked(Category category, View view);
}
