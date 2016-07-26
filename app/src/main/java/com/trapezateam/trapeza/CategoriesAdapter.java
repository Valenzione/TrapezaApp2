package com.trapezateam.trapeza;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.trapezateam.trapeza.database.Dish;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Yuriy on 7/22/2016.
 */
public class CategoriesAdapter
        extends RealmRecyclerViewAdapter<Dish, CategoriesAdapter.ViewHolder> {

    public CategoriesAdapter(@NonNull Context context,
                             @Nullable OrderedRealmCollection<Dish> data,
                             boolean autoUpdate) {
        super(context, data, autoUpdate);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.square_button, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MenuEntry e = getData().get(position);
        holder.mSquareButton.setText(e.getName());
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        SquareButton mSquareButton;

        public ViewHolder(View view) {
            super(view);
            mSquareButton = (SquareButton) view;
        }
    }


}
