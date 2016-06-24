package com.trapezateam.trapeza.models;

import com.trapezateam.trapeza.api.models.DishResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ilgiz on 6/22/16.
 */
public class DishGraph {

    private Vertex mRoot;

    class Vertex {
        Dish mDish;
        boolean mHasParents;

        Dish getDish() {
            return mDish;
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
            vertex.setHasParents(true);
        }

        public ArrayList<Vertex> getChildren() {
            return mChildren;
        }

        public void setHasParents(boolean hasParents) {
            mHasParents = hasParents;
        }

        public boolean hasParents() {
            return mHasParents;
        }
    }


    class Edge {
        final Vertex mTo;

        Edge(Vertex to) {
            mTo = to;
        }
    }

    Vertex getRoot() {
        return mRoot;
    }

    public DishGraph(List<DishResponse> dishesList) {
        mRoot = new Vertex();
        Map<Integer, Vertex> vertices = new TreeMap<>();
        for (DishResponse d : dishesList) {
            vertices.put(d.getId(), new Vertex(new Dish(d)));
        }
        for(DishResponse d : dishesList) {
            vertices.get(d.getFather()).addChild(vertices.get(d.getId()));
        }
        for(Vertex v : vertices.values()) {
            if(!v.hasParents()) {
                mRoot.addChild(v);
            }
        }
    }

}
