package com.trapezateam.trapeza;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.trapezateam.trapeza.database.Category;
import com.trapezateam.trapeza.models.Dish;
import com.trapezateam.trapeza.models.DishTree;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import io.realm.RealmObject;

/**
 * Created by Yuriy on 7/22/2016.
 */
public class SimpleRealmAdapter extends RealmBaseAdapter implements ListAdapter {

    private static class ViewHolder {
        TextView name;
    }

    public SimpleRealmAdapter(@NonNull Context context, @Nullable OrderedRealmCollection data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            Context context = viewGroup.getContext();
            view = new CategoryButton(context);
        }
        CategoryButton button = (CategoryButton) view;
        final Category item = (Category) adapterData.get(position);
        button.setText(item.toString());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Change to dishes item.getDishes()

            }
        });
        return button;
    }


}
