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
import com.trapezateam.trapeza.api.models.CompanyDataResponse;
import com.trapezateam.trapeza.api.models.DishResponse;
import com.trapezateam.trapeza.api.models.UserResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
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
        TrapezaRestClient.CompanyMethods.getData(companyId, new Callback<CompanyDataResponse>() {
            @Override
            public void onResponse(Call<CompanyDataResponse> call, Response<CompanyDataResponse> response) {
                final CompanyDataResponse companyDataResponse = response.body();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.deleteAll();
                        for (UserResponse userResponse : companyDataResponse.getUsers()) {
                            User user = realm.createObject(User.class);
                            user.setData(userResponse);
                        }
                        for (CategoryResponse categoryResponse : companyDataResponse.getCategories()) {
                            Category category = realm.createObject(Category.class);
                            category.setData(categoryResponse);
                        }
                        for (DishResponse dishResponse : companyDataResponse.getDishes()) {
                            Dish dish = realm.createObject(Dish.class);
                            dish.setData(dishResponse);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<CompanyDataResponse> call, Throwable t) {
                Log.e(TAG, "ERROR UPDATING DATABASE");
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
        realm.commitTransaction();
    }

    private static void addCategory(CategoryResponse c) {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Category category = realm.createObject(Category.class);
        category.setName(c.getName());
        category.setCategoryId(c.getId());
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

    public static void updateModel(RealmObject ro) {
        realm.insertOrUpdate(ro);
    }

    public static void deleteModel(RealmObject realmObject) {
        realm.beginTransaction();
        realmObject.deleteFromRealm();
        realm.commitTransaction();
    }





}
