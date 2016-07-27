package com.trapezateam.trapeza.database;


import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.WindowManager;

import com.trapezateam.trapeza.CashierActivity;
import com.trapezateam.trapeza.DatabaseActivity;
import com.trapezateam.trapeza.TrapezaApplication;
import com.trapezateam.trapeza.api.TrapezaRestClient;
import com.trapezateam.trapeza.api.models.CategoryResponse;
import com.trapezateam.trapeza.api.models.DishResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yuriy on 7/5/2016.
 */
public class RealmClient {

    static Realm realm = Realm.getDefaultInstance();


    /**
     * TODO add user update
     */
    public static void updateDatabase() {

        //TODO ensure connection with server to prevent deleting all records and retrivieng nothing

        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();

        TrapezaRestClient.categoriesList(new Callback<List<CategoryResponse>>() {
            @Override
            public void onResponse(Call<List<CategoryResponse>> call, Response<List<CategoryResponse>> response) {
                Log.d("DatabaseUpdate", String.valueOf(response.body().size()) + " categories to update");
                for (CategoryResponse c : response.body()) {
                    addCategory(c);
                }
            }

            @Override
            public void onFailure(Call<List<CategoryResponse>> call, Throwable t) {
                Log.e("DatabaseUpdate", "Error on getting categories");
            }
        });
        TrapezaRestClient.dishesList(new Callback<List<DishResponse>>() {
            @Override
            public void onResponse(Call<List<DishResponse>> call, Response<List<DishResponse>> response) {

                Log.d("DatabaseUpdate", String.valueOf(response.body().size()) + " dishes to update");
                for (DishResponse dish : response.body()) {
                    addDish(dish);
                }

            }

            @Override
            public void onFailure(Call<List<DishResponse>> call, Throwable t) {
                Log.e("DatabaseUpdate", "Error on getting dishes");
            }
        });

        Log.d("DatabaseUpdate", "Database update completed " + (new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())));
    }


    private static void addDish(DishResponse d) {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        Dish dish = realm.createObject(Dish.class);
        dish.setName(d.getName());
        dish.setDescription(d.getDescription());
        dish.setCategoryId(d.getFather());
        dish.setPrice(Integer.parseInt(d.getPrice()));
        dish.setDishId(d.getId());

        RealmResults<Category> categories = realm.where(Category.class).equalTo("categoryId", d.getFather()).findAll();
        Category c = realm.copyFromRealm(categories.first());
        c.addToDishes(dish);

        realm.commitTransaction();
    }

    private static void addCategory(CategoryResponse c) {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        final Category category = realm.createObject(Category.class);
        category.setName(c.getName());
        category.setCategoryId(c.getId());

        realm.commitTransaction();
    }

    public static void addUser(String name, String email, int companyId) {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final User u = realm.createObject(User.class);

        u.setName(name);
        u.setEmail(email);

        int nextUserId = realm.where(User.class).findAll().size() + 1;
        u.setId(nextUserId);
        u.setCompanyId(companyId);


        RealmResults<Company> companies = realm.where(Company.class).equalTo("companyId", companyId).findAll();
        Company c = realm.copyFromRealm(companies.first());
        c.addToStuff(u);

        realm.commitTransaction();
    }

    public static void createCompany(String name) {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final Company company = realm.createObject(Company.class);

        company.setCompanyName(name);

        int nextUserId = realm.where(Company.class).findAll().size() + 1;
        company.setCompanyId(nextUserId);

        realm.commitTransaction();
    }

    public static void addDish(String name, String description, int price, int categoryId) {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        Dish dish = realm.createObject(Dish.class);
        dish.setName(name);
        dish.setDescription(description);
        dish.setCategoryId(categoryId);
        dish.setPrice(price);
        int nextDishId = realm.where(Dish.class).findAll().size() + 1;
        dish.setDishId(nextDishId);

        RealmResults<Category> categories = realm.where(Category.class).equalTo("categoryId", categoryId).findAll();
        categories.load();
        Category c = realm.copyFromRealm(categories.first());
        c.addToDishes(dish);


        realm.commitTransaction();
    }

    public static void addCategory(String name) {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final Category category = realm.createObject(Category.class);

        category.setName(name);

        int nextCategoryId = realm.where(Category.class).findAll().size() + 1;
        category.setCategoryId(nextCategoryId);

        realm.commitTransaction();
    }

    public static RealmResults<Dish> getDishes() {
        realm = Realm.getDefaultInstance();
        return realm.where(Dish.class).findAll();
    }

    public static RealmResults<Category> getCategories() {
        return realm.where(Category.class).findAll();
    }

    public static RealmResults<User> getUsers() {
        return realm.where(User.class).findAll();
    }

    public static RealmResults<Company> getCompanies() {
        return realm.where(Company.class).findAll();
    }


}
