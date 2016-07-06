package com.trapezateam.trapeza.database;


import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Yuriy on 7/5/2016.
 */
public class RealmClient {

    static Realm realm = Realm.getDefaultInstance();

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
        final Dish dish = realm.createObject(Dish.class);

        dish.setName(name);
        dish.setDescription(description);
        dish.setCreatorId(categoryId);
        dish.setPrice(price);

        int nextDishId = realm.where(Dish.class).findAll().size() + 1;
        dish.setDishId(nextDishId);
        RealmResults<Category> categories = realm.where(Category.class).equalTo("categoryId", categoryId).findAll();
        Category c = realm.copyFromRealm(categories.first());
        c.addToDishes(dish);

        realm.commitTransaction();
    }

    public static void createCategory(String name) {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final Category category = realm.createObject(Category.class);

        category.setName(name);

        int nextCategoryId = realm.where(Category.class).findAll().size() + 1;
        category.setCategoryId(nextCategoryId);

        realm.commitTransaction();
    }

    public static RealmResults<Dish> getDishes() {
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
