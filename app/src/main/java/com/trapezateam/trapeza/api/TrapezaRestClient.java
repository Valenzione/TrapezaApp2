package com.trapezateam.trapeza.api;

import android.media.Image;

import com.trapezateam.trapeza.api.models.CategoryResponse;
import com.trapezateam.trapeza.api.models.AuthenticationResponse;
import com.trapezateam.trapeza.api.models.DeletetedDishResponse;
import com.trapezateam.trapeza.api.models.DishResponse;
import com.trapezateam.trapeza.api.models.ModifiedCategoryResponse;
import com.trapezateam.trapeza.api.models.ModifiedDishResponse;
import com.trapezateam.trapeza.api.models.SavedCategoryResponse;
import com.trapezateam.trapeza.api.models.SavedDishResponse;
import com.trapezateam.trapeza.api.models.StatusResponse;
import com.trapezateam.trapeza.api.models.UserResponse;
import com.trapezateam.trapeza.database.Category;
import com.trapezateam.trapeza.database.Dish;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ilgiz on 6/22/16.
 */
public class TrapezaRestClient {

    private static final String BASE_URL = "http://10.90.104.141:8080";

    private static TrapezaApi mApi;

    private static String mToken;

    private static TrapezaApi getApiInstance() {
        if (mApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mApi = retrofit.create(TrapezaApi.class);
        }
        return mApi;
    }

    public static void setToken(String token) {
        mToken = token;
    }

    public static String getToken() {
        if (mToken == null) {
            throw new IllegalStateException("Token is not set yet");
        }
        return mToken;
    }

    public static void authenticate(String login, String password,
                                    Callback<AuthenticationResponse> callback) {
        getApiInstance().authenticate(login, password).enqueue(callback);
    }

    public static class UserMethods{
        public static void get(int id, Callback<List<UserResponse>> callback) {
            getApiInstance().userInfo(getToken(), id).enqueue(callback);
        }

        public static void getList(int companyId, Callback<List<UserResponse>> callback) {
            getApiInstance().usersList(getToken(), companyId).enqueue(callback);
        }

    }


    public static class DishMethods{

        public static void getList(Callback<List<DishResponse>> callback) {
            getApiInstance().dishesList(getToken()).enqueue(callback);
        }

        public static void create(Dish dish, Callback<List<StatusResponse>> callback) {
            getApiInstance().addDish(dish.getName(), null, dish.getDescription(), dish.getPrice(), dish.getCategoryId(), getToken()).enqueue(callback);
        }

        public static void update(Dish dish, Callback<List<StatusResponse>> callback) {
            getApiInstance().modifyDish(dish.getName(), null, dish.getDescription(), dish.getDishId(), dish.getCategoryId(), getToken()).enqueue(callback);
        }
        public static void delete(Dish dish, Callback<List<StatusResponse>> callback) {
            getApiInstance().deleteDish(dish.getDishId(), getToken()).enqueue(callback);
        }
    }


    public static class CategoryMethods {

        public static void create(Category category, Callback<List<StatusResponse>> callback) {
            getApiInstance().addCategory(category.getName(), getToken()).enqueue(callback);
        }

        public static void delete(Category category, Callback<List<StatusResponse>> callback) {
            getApiInstance().deleteCategory(category.getCategoryId(), getToken()).enqueue(callback);
        }

        public static void update(Category category, Callback<List<StatusResponse>> callback) {
            getApiInstance().modifyCategory(category.getCategoryId(), category.getName(), getToken()).enqueue(callback);
        }

        public static void getList(Callback<List<CategoryResponse>> callback) {
            getApiInstance().categoriesList(getToken()).enqueue(callback);
        }
    }
}
