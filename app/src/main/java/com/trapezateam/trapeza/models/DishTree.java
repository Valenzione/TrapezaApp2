package com.trapezateam.trapeza.models;

import android.util.Log;

import com.trapezateam.trapeza.api.models.DishResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ilgiz on 6/22/16.
 */
public class DishTree {

    public static final String TAG = "DishTree";

    private Vertex mRoot;

    public class Vertex {
        Dish mDish;
        Vertex mParent;

        public Dish getDish() {
            return mDish;
        }

        public Vertex getParent() {
            return mParent;
        }

        @Override
        public boolean equals(Object obj) {

            return super.equals(obj);
        }

        ArrayList<Vertex> mChildren;

        Vertex() {
            mChildren = new ArrayList<>();
        }

        Vertex(Dish dish) {
            this();
            mDish = dish;
        }

        void addChild(Vertex vertex) {
            mChildren.add(vertex);
            vertex.setParent(this);
        }

        public ArrayList<Vertex> getChildren() {
            return mChildren;
        }

        public void setParent(Vertex v) {
            mParent = v;
        }

        public boolean hasParents() {
            return mParent != null;
        }

        public boolean isLeaf() {
            return mChildren.isEmpty();
        }
    }

    public Vertex getRoot() {
        return mRoot;
    }

    public DishTree(List<DishResponse> dishesList) {
        Log.d(TAG, "Parsing list " + dishesList);
        mRoot = new Vertex();
        Map<Integer, Vertex> vertices = new TreeMap<>();
        for (DishResponse d : dishesList) {
            vertices.put(d.getId(), new Vertex(new Dish(d)));
        }
        for (DishResponse d : dishesList) {
            if (d.getFather() == null) {
                continue;
            }
            Log.d(TAG, "Attaching " + vertices.get(d.getId()).getDish() + " to " +
                    vertices.get(d.getFather()).getDish());
            vertices.get(d.getFather()).addChild(vertices.get(d.getId()));
        }
        for (Vertex v : vertices.values()) {
            if (!v.hasParents()) {
                mRoot.addChild(v);
            }
        }
    }

}
