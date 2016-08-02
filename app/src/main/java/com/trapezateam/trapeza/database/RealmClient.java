package com.trapezateam.trapeza.database;


import android.app.Activity;
import android.app.ProgressDialog;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.WindowManager;

import com.trapezateam.trapeza.CashierActivity;
import com.trapezateam.trapeza.TrapezaApplication;
import com.trapezateam.trapeza.api.TrapezaRestClient;
import com.trapezateam.trapeza.api.models.CategoryResponse;
import com.trapezateam.trapeza.api.models.DishResponse;
import com.trapezateam.trapeza.api.models.UserResponse;

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

    private static final String TAG = "DatabaseUpdate";
    static Realm realm = Realm.getDefaultInstance();


    public static void updateDatabase(int companyId) {

        final ArrayList<UserResponse> userList = new ArrayList<>();
        final ArrayList<CategoryResponse> categoriesList = new ArrayList<>();
        final ArrayList<DishResponse> dishesList = new ArrayList<>();

        TrapezaRestClient.UserMethods.getList(companyId, new Callback<List<UserResponse>>() {
            @Override
            public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> response) {
                Log.d("DatabaseUpdate", String.valueOf(response.body().size()) + " users to update");
                for (UserResponse u : response.body()) {
                    userList.add(u);
                }
            }

            @Override
            public void onFailure(Call<List<UserResponse>> call, Throwable t) {
                realm.cancelTransaction();
                Log.e("DatabaseUpdate", "Error on getting users");
            }
        });
        TrapezaRestClient.CategoryMethods.getList(new Callback<List<CategoryResponse>>() {
            @Override
            public void onResponse(Call<List<CategoryResponse>> call, Response<List<CategoryResponse>> response) {
                Log.d("DatabaseUpdate", String.valueOf(response.body().size()) + " categories to update");
                for (CategoryResponse c : response.body()) {
                    categoriesList.add(c);
                }
            }

            @Override
            public void onFailure(Call<List<CategoryResponse>> call, Throwable t) {
                t.printStackTrace();
                Log.e("DatabaseUpdate", "Error on getting categories" + t.toString());
            }
        });
        TrapezaRestClient.DishMethods.getList(new Callback<List<DishResponse>>() {
            @Override
            public void onResponse(Call<List<DishResponse>> call, Response<List<DishResponse>> response) {

                Log.d("DatabaseUpdate", String.valueOf(response.body().size()) + " dishes to update");
                for (DishResponse dish : response.body()) {
                    dishesList.add(dish);
                }

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.deleteAll();


                        for (UserResponse userResponse : userList) {
                            User user = realm.createObject(User.class);
                            user.setData(userResponse);

                        }
                        for (CategoryResponse categoryResponse : categoriesList) {
                            Category category = realm.createObject(Category.class);
                            category.setData(categoryResponse);

                        }
                        for (DishResponse dishResponse : dishesList) {
                            Dish dish = realm.createObject(Dish.class);
                            dish.setData(dishResponse);
                            RealmResults<Category> categories = realm.where(Category.class).equalTo("categoryId", dish.getCategoryId()).findAll();
                            Category c = realm.copyFromRealm(categories.first());
                            c.addToDishes(dish);
                        }

                    }
                });

            }

            @Override
            public void onFailure(Call<List<DishResponse>> call, Throwable t) {
                t.printStackTrace();
                Log.e("DatabaseUpdate", "Error on getting dishes " + t.toString());
            }
        });


    }

    private static void addUser(UserResponse userResponse) {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final User u = realm.createObject(User.class);

        u.setName(userResponse.getSurname() + " " + userResponse.getName());
        u.setEmail("noEmailInDataBase@for.now");
        int nextUserId = realm.where(User.class).findAll().size() + 1;
        u.setId(nextUserId);
        u.setCompanyId(userResponse.getCompany());

        realm.commitTransaction();
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


    public static void updateDish(Dish dish) {
        realm.insertOrUpdate(dish);
    }

    public static void updateCategory(Category category) {
        realm.insertOrUpdate(category);
    }

    public static void deleteDish(Dish dish) {
        realm.beginTransaction();
        dish.deleteFromRealm();
        realm.commitTransaction();
    }

    public static void deleteCategory(Category category) {
        realm.beginTransaction();
        category.deleteFromRealm();
        realm.commitTransaction();
    }
}
