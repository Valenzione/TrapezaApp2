package com.trapezateam.trapeza;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.trapezateam.trapeza.models.DishGraph;

import java.util.List;

/**
 * Created by ilgiz on 6/24/16.
 */
public class DishGraphAdapter extends BaseAdapter {

    DishGraph.Vertex mRoot;

    DishGraph.Vertex mCurrentVertex;

    public static final int ID_UP = -1;

    public DishGraphAdapter(DishGraph graph) {
        mRoot = graph.getRoot();
        mCurrentVertex = mRoot;
    }


    @Override
    public int getCount() {
        return mCurrentVertex.getChildren().size() + 1;
    }

    @Override
    public Object getItem(int i) {
        if(i == 0) {
            return null;
        }
        return mCurrentVertex.getChildren().get(i + 1);
    }

    @Override
    public long getItemId(int i) {
        if(i == 0) {
            return ID_UP;
        }
        return mCurrentVertex.getChildren().get(i+ 1).getDish().getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(getItemViewType(i) == TYPE_UP) {
            return getUpButton(view, viewGroup);
        }

    }

    View getUpButton(View view, ViewGroup viewGroup) {


    }

    private static final int TYPES_COUNT = 2;
    private static final int TYPE_UP = 0;
    private static final int TYPE_NORMAL = 1;

    @Override
    public int getViewTypeCount() {
        return TYPES_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return TYPE_UP;
        }
        return TYPE_NORMAL;
    }
}
