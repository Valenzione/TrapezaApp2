package com.trapezateam.trapeza;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import com.trapezateam.trapeza.database.MenuEntry;

import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Yuriy on 7/22/2016.
 */
public class MenuAdapter
        extends RealmRecyclerViewAdapter<MenuEntry, MenuAdapter.ViewHolder> {

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
