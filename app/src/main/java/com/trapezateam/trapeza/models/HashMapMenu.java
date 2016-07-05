package com.trapezateam.trapeza.models;

import android.util.Log;
import android.view.Menu;

import com.trapezateam.trapeza.api.models.CategoryResponse;
import com.trapezateam.trapeza.api.models.DishResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class HashMapMenu {

    public static final String TAG = "HashMapMenu";

    public static final int DISH_TYPE = 0;
    public static final int CATEGORY_TYPE = 1;
    public static final int BACK_TYPE = 2;
    public static final int ADD_TYPE = 3;

    HashMap<Integer, Category> mMenu = new HashMap<>();

    public HashMapMenu(List<DishResponse> dishResponses, List<CategoryResponse> categoryResponses) {
        Log.d(TAG, "Parsing list " + dishResponses);
        Log.d(TAG, "Parsing list " + categoryResponses);


        for (CategoryResponse c : categoryResponses) {
            Category category = new Category(c);
            mMenu.put(category.getId(), category);
            mMenu.get(category.getId()).addDish(new Dish("Back", "", 0));
        }
        for (DishResponse d : dishResponses) {
            mMenu.get(d.getFather()).addDish(new Dish(d));
        }

    }

    public ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        for (Map.Entry<Integer, Category> c : mMenu.entrySet()) {
            categories.add(c.getValue());
        }
        return categories;
    }

    public ArrayList<Dish> getDishes(int categoryId) {
        ArrayList<Dish> dishes = new ArrayList<>();
        dishes = mMenu.get(categoryId).getDishes();
        return dishes;
    }


    @Override
    public String toString() {
        String out = (String.valueOf(mMenu.size()));
        return out;
    }

}
