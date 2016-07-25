package com.trapezateam.trapeza;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(android.R.id.text1);
            viewHolder.name.setBackgroundResource(R.color.item_background);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RealmObject item = (RealmObject) adapterData.get(position);
        viewHolder.name.setText(item.toString());
        return convertView;
    }


}
