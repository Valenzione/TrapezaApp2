package com.trapezateam.trapeza.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.trapezateam.trapeza.R;
import com.trapezateam.trapeza.SquareButton;
import com.trapezateam.trapeza.api.TrapezaRestClient;
import com.trapezateam.trapeza.database.Category;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import jp.wasabeef.picasso.transformations.ColorFilterTransformation;

/**
 * Created by Yuriy on 7/22/2016.
 */
public class CategoryAdapter
        extends RealmBaseAdapter<Category> {


    private CategoryEventsListener mCategoryEventsListener;
    private MenuAdapter mMenuAdapter;
    private boolean mShowAddButton;

    public CategoryAdapter(@NonNull Context context,
                           @Nullable OrderedRealmCollection<Category> data) {
        super(context, data);
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
//        Too hard to code properly :-)
//        if (view == null) {
        view = LayoutInflater.from(context).inflate(R.layout.square_button, viewGroup, false);
//        }

        SquareButton button = (SquareButton) view.findViewById(R.id.squareButton);

        if (mShowAddButton && i == getCount() - 1) {
            button.setText("+");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMenuAdapter.onAddCategoryClicked(view);
                }
            });
            button.setOnLongClickListener(null);
            return button;
        }


        final Category category = getItem(i);

        button.setText(category.getName());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCategoryEventsListener != null) {
                    mCategoryEventsListener.onCategoryClicked(category, view);
                }
                if (mMenuAdapter != null) {
                    mMenuAdapter.onCategoryClicked(category);
                }
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mCategoryEventsListener != null) {
                    return mCategoryEventsListener.onCategoryLongClicked(category, view);
                }
                return false;
            }
        });


        Picasso.with(context).load(TrapezaRestClient.getFileUrl(category.getPhotoUrl()))
                .resize(300, 300)
                .centerCrop()
                .transform(new ColorFilterTransformation(
                        ContextCompat.getColor(context, R.color.photo_color_filter)))
                .into(button);

        return button;
    }

    public void setCategoryEventsListener(CategoryEventsListener listener) {
        mCategoryEventsListener = listener;
    }

    public void removeOnCategoryClickedListener() {
        mCategoryEventsListener = null;
    }

    public void setMenuAdapter(MenuAdapter menuAdapter) {
        mMenuAdapter = menuAdapter;
    }

    public void removeMenuAdapter() {
        mMenuAdapter = null;
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        if (mShowAddButton) {
            count++; // add button
        }
        return count;
    }

    public void setShowAddButton(boolean showAddButton) {
        if (showAddButton != mShowAddButton) {
            mShowAddButton = showAddButton;
            notifyDataSetChanged();
        }
    }

    public boolean getShowAddButton() {
        return mShowAddButton;
    }
}
