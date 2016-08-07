package com.trapezateam.trapeza.api;

import android.graphics.Bitmap;

import com.trapezateam.trapeza.TrapezaApplication;
import com.trapezateam.trapeza.api.models.CategoryResponse;
import com.trapezateam.trapeza.api.models.AuthenticationResponse;
import com.trapezateam.trapeza.api.models.CompanyDataResponse;
import com.trapezateam.trapeza.api.models.DishResponse;
import com.trapezateam.trapeza.api.models.SaveCompleteResponse;
import com.trapezateam.trapeza.api.models.StatusResponse;
import com.trapezateam.trapeza.api.models.UploadResponse;
import com.trapezateam.trapeza.api.models.UserResponse;
import com.trapezateam.trapeza.database.Category;
import com.trapezateam.trapeza.database.Dish;

import java.io.ByteArrayOutputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ilgiz on 6/22/16.
 */
public class TrapezaRestClient {

    private static final String BASE_URL = "http://10.90.104.141:8080";

    private static final String BASE_UPLOAD_URL = "http://10.90.104.141:3000";

    private static TrapezaApi mApi;
    private static TrapezaUploadApi mUploadApi;

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

    private static TrapezaUploadApi getUploadApiInstance() {
        if (mUploadApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_UPLOAD_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mUploadApi = retrofit.create(TrapezaUploadApi.class);
        }
        return mUploadApi;
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

    public static class UserMethods {
        public static void get(int id, Callback<List<UserResponse>> callback) {
            getApiInstance().userInfo(getToken(), id).enqueue(callback);
        }

        @Deprecated
        public static void getList(int companyId, Callback<List<UserResponse>> callback) {
            getApiInstance().usersList(companyId, getToken()).enqueue(callback);
        }

    }


    public static class DishMethods {
        @Deprecated
        public static void getList(int companyId, Callback<List<DishResponse>> callback) {
            getApiInstance().dishesList(companyId, getToken()).enqueue(callback);
        }

        public static void create(Dish dish, Callback<SaveCompleteResponse> callback) {
            getApiInstance().addDish(dish.getName(), null, dish.getDescription(), dish.getPrice(), dish.getCategoryId(), getToken(), TrapezaApplication.getCompany()).enqueue(callback);
        }

        public static void update(Dish dish, Callback<StatusResponse> callback) {
            getApiInstance().modifyDish(dish.getName(), null, dish.getDescription(), dish.getDishId(), dish.getPrice(), getToken()).enqueue(callback);
        }

        public static void delete(Dish dish, Callback<StatusResponse> callback) {
            getApiInstance().deleteDish(dish.getDishId(), getToken()).enqueue(callback);
        }
    }


    public static class CategoryMethods {

        public static void create(Category category, Callback<SaveCompleteResponse> callback) {
            getApiInstance().addCategory(category.getName(), "null", getToken()).enqueue(callback);
        }

        public static void delete(Category category, Callback<StatusResponse> callback) {
            getApiInstance().deleteCategory(category.getCategoryId(), getToken()).enqueue(callback);
        }

        public static void update(Category category, Callback<StatusResponse> callback) {
            getApiInstance().modifyCategory(category.getCategoryId(), category.getName(), "null", getToken()).enqueue(callback);
        }

        @Deprecated
        public static void getList(int companyId, Callback<List<CategoryResponse>> callback) {
            getApiInstance().categoriesList(companyId, getToken()).enqueue(callback);
        }
    }

    public static class CompanyMethods {
        public static void getData(int companyId, Callback<CompanyDataResponse> callback) {
            getApiInstance().getData(companyId, getToken()).enqueue(callback);
        }
    }

    public static class UploadMethods {
        public static void uploadImage(Bitmap bitmap, Callback<UploadResponse> callback) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), byteArrayOutputStream.toByteArray());
            getUploadApiInstance().upload(requestBody).enqueue(callback);
        }
    }
}
