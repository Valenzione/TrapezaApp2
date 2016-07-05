//package com.trapezateam.trapeza;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//
//import com.trapezateam.trapeza.models.Category;
//import com.trapezateam.trapeza.models.HashMapMenu;
//
//import java.util.HashMap;
//
///**
// * Created by Yuriy on 7/5/2016.
// */
//public class MenuTreeAdapter extends BaseAdapter {
//
//    HashMap<Integer, Category> mMenu;
//
//    Bill mBill;
//
//    public static final int ID_UP = -1;
//
//    public MenuTreeAdapter(HashMap<Integer, Category> graph, Bill bill) {
//        mMenu = graph;
//        mBill = bill;
//    }
//
//
//    @Override
//    public int getCount() {
//        return mMenu.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        if (view == null) {
//            Context context = viewGroup.getContext();
//            view = new SquareButton(context);
//        }
//        SquareButton button = (SquareButton) view;
//
//
//        button.setEnabled(true);
//        final HashMapMenu.Vertex v = (HashMapMenu.Vertex) mCurrentVertex.getChildren().get(i - 1);
//
//        button.setText(v.getElement());
//        if (v.isLeaf())
//
//        {
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mBill.addEntry(dish);
//                }
//            });
//        } else
//
//        {
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    changeVertexTo(v);
//                }
//            });
//        }
//
//        return button;
//    }
//
//    void changeVertexTo(HashMapMenu.Vertex v) {
//        mCurrentVertex = v;
//        notifyDataSetChanged();
//    }
//
//
//    private static final int TYPES_COUNT = 2;
//    private static final int TYPE_UP = 0;
//    private static final int TYPE_NORMAL = 1;
//
//    @Override
//    public int getViewTypeCount() {
//        return TYPES_COUNT;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (position == 0) {
//            return TYPE_UP;
//        }
//        return TYPE_NORMAL;
//    }
//}
