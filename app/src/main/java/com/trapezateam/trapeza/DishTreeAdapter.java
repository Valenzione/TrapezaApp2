package com.trapezateam.trapeza;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.trapezateam.trapeza.models.Dish;
import com.trapezateam.trapeza.models.DishTree;

/**
 * Created by ilgiz on 6/24/16.
 */
public class DishTreeAdapter extends BaseAdapter {

    DishTree.Vertex mRoot;
    DishTree.Vertex mCurrentVertex;

    Bill mBill;

    public static final int ID_UP = -1;

    public DishTreeAdapter(DishTree graph, Bill bill) {
        mRoot = graph.getRoot();
        mCurrentVertex = mRoot;
        mBill = bill;
    }


    @Override
    public int getCount() {
        return mCurrentVertex.getChildren().size() + 1;
    }

    @Override
    public Object getItem(int i) {
        if (i == 0) {
            return null;
        }
        return mCurrentVertex.getChildren().get(i - 1);
    }

    @Override
    public long getItemId(int i) {
        if (i == 0) {
            return ID_UP;
        }
        return mCurrentVertex.getChildren().get(i - 1).getDish().getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            Context context = viewGroup.getContext();
            view = new DishButton(context);
        }
        DishButton button = (DishButton) view;
        if (i == 0) {
            button.setText(new Dish("Back", "<-", 24));
            if (mCurrentVertex.hasParents()) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeVertexTo(mCurrentVertex.getParent());
                    }
                });
                button.setEnabled(true);
            } else {
                button.setEnabled(false);
            }
            return button;
        }
        button.setEnabled(true);
        final DishTree.Vertex v = mCurrentVertex.getChildren().get(i - 1);
        final Dish dish = v.getDish();
        button.setText(dish);
        if (v.isLeaf()) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBill.addEntry(dish);
                }
            });
        } else {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeVertexTo(v);
                }
            });
        }
        return button;
    }

    void changeVertexTo(DishTree.Vertex v) {
        mCurrentVertex = v;
        notifyDataSetChanged();
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
        if (position == 0) {
            return TYPE_UP;
        }
        return TYPE_NORMAL;
    }
}
