package com.trapezateam.trapeza.api;

import android.graphics.Bitmap;
import android.util.Log;

import com.trapezateam.trapeza.TrapezaApplication;
import com.trapezateam.trapeza.api.models.AuthenticationResponse;
import com.trapezateam.trapeza.api.models.CategoryResponse;
import com.trapezateam.trapeza.api.models.CompanyDataResponse;
import com.trapezateam.trapeza.api.models.DishResponse;
import com.trapezateam.trapeza.api.models.SaveCompleteResponse;
import com.trapezateam.trapeza.api.models.StatisticsResponse;
import com.trapezateam.trapeza.api.models.StatusResponse;
import com.trapezateam.trapeza.api.models.UploadResponse;
import com.trapezateam.trapeza.api.models.UserResponse;
import com.trapezateam.trapeza.database.Category;
import com.trapezateam.trapeza.database.Company;
import com.trapezateam.trapeza.database.Dish;
import com.trapezateam.trapeza.database.User;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ilgiz on 6/22/16.
 */
public class TrapezaRestClient {

    private static final String SERVER_ADDRESS = "http://10.90.104.141";
    private static final String API_BASE_URL = SERVER_ADDRESS + ":8080";
    private static final String FILES_BASE_URL = SERVER_ADDRESS + ":3000";

    public static final String getFileUrl(String fileName) {
        return FILES_BASE_URL + "/" + fileName;
    }

    private static TrapezaApi sApi;
    private static TrapezaUploadApi sUploadApi;

    private static String sToken;

    private static TrapezaApi getApiInstance() {
        if (sApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            sApi = retrofit.create(TrapezaApi.class);
        }
        return sApi;
    }

    private static TrapezaUploadApi getUploadApiInstance() {
        if (sUploadApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(FILES_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            sUploadApi = retrofit.create(TrapezaUploadApi.class);
        }
        return sUploadApi;
    }


    public static void setToken(String token) {
        sToken = token;
    }

    public static String getToken() {
        if (sToken == null) {
            throw new IllegalStateException("Token is not set yet");
        }
        return sToken;
    }

    public static void authenticate(String login, String password,
                                    Callback<AuthenticationResponse> callback) {
        getApiInstance().authenticate(login, password).enqueue(callback);
    }

    public static void logout(Callback<StatusResponse> callback) {
        getApiInstance().logout(getToken()).enqueue(callback);
    }


    public static class UserMethods {
        public static void get(int id, Callback<List<UserResponse>> callback) {
            getApiInstance().userInfo(getToken(), id).enqueue(callback);
        }

        public static void create(User user, String login, String password, Callback<SaveCompleteResponse> callback) {
            Log.d("RestClient", "Company " + TrapezaApplication.getCompany());
            getApiInstance().addUser(login, password, user.getPhone(), user.getFullName(),
                    user.getSurname(), TrapezaApplication.getCompany(), user.getRole(), TrapezaRestClient.getToken()).enqueue(callback);
        }

        public static void update(User user, Callback<StatusResponse> callback) {
            getApiInstance().modifyUser(user.getPhone(), user.getName(), user.getSurname(), user.getRole(), TrapezaRestClient.getToken()).enqueue(callback);
        }

        public static void delete(User user, Callback<StatusResponse> callback) {
            getApiInstance().deleteUser(user.getId(), getToken()).enqueue(callback);
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
            getApiInstance().addDish(dish.getName(), dish.getPhotoUrl(), dish.getDescription(), dish.getPrice(), dish.getCategoryId(), getToken(), TrapezaApplication.getCompany()).enqueue(callback);
        }

        public static void update(Dish dish, Callback<StatusResponse> callback) {
            getApiInstance().modifyDish(dish.getName(), dish.getPhotoUrl(), dish.getDescription(), dish.getDishId(), dish.getPrice(), getToken()).enqueue(callback);
        }

        public static void delete(Dish dish, Callback<StatusResponse> callback) {
            getApiInstance().deleteDish(dish.getDishId(), getToken()).enqueue(callback);
        }
    }


    public static class CategoryMethods {

        public static void create(Category category, Callback<SaveCompleteResponse> callback) {
            getApiInstance().addCategory(category.getName(), category.getPhotoUrl(), getToken()).enqueue(callback);
        }

        public static void delete(Category category, Callback<StatusResponse> callback) {
            getApiInstance().deleteCategory(category.getCategoryId(), getToken()).enqueue(callback);
        }

        public static void update(Category category, Callback<StatusResponse> callback) {
            getApiInstance().modifyCategory(category.getCategoryId(), category.getName(), category.getPhotoUrl(), getToken()).enqueue(callback);
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

        public static void create(Company company, User user, String login, String pass, Callback<SaveCompleteResponse> callback) {
            getApiInstance().addCompany(company.getCompanyName(), company.getPhone(), company.getAddress(),
                    login, pass, user.getPhone(), user.getName(), user.getSurname()).enqueue(callback);
        }
    }

    public static class UploadMethods {
        public static void uploadImage(Bitmap bitmap, Callback<UploadResponse> callback) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            RequestBody requestBody = RequestBody
                    .create(MediaType.parse("image/*"),
                            byteArrayOutputStream.toByteArray());
            getUploadApiInstance().upload(requestBody).enqueue(callback);
        }

        public static String uploadImage(Bitmap bitmap) throws IOException {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            RequestBody requestBody = RequestBody
                    .create(MediaType.parse("image/*"),
                            byteArrayOutputStream.toByteArray());
            Response<UploadResponse> call = getUploadApiInstance().upload(requestBody).execute();
            return call.body().getPath();
        }
    }

    public static class StatisticsMethods {
        public static void boughtDay(Callback<StatisticsResponse> callback) {
            getApiInstance().boughtDay(getToken()).enqueue(callback);
        }

        public static void boughtMonth(Callback<StatisticsResponse> callback) {
            getApiInstance().boughtMonth(getToken()).enqueue(callback);
        }

        public static void boughtWeek(Callback<StatisticsResponse> callback) {
            getApiInstance().boughtWeek(getToken()).enqueue(callback);
        }

        public static void boughtInterval(String from, String to, Callback<StatisticsResponse> callback) {
            getApiInstance().boughtInterval(from, to).enqueue(callback);
        }
    }

    public static class PaymentMethods {
        /**
         * @param prices      prices ids of dishes bought
         * @param paymentType <code>0</code> - pays in cash. <code>1</code> - pays by card
         */
        public static void buyDish(JSONArray prices,
                                   int paymentType,
                                   double sale,
                                   Callback<StatusResponse> callback) {
            getApiInstance().buyDish(prices.toString(), paymentType, sale, getToken()).enqueue(callback);
        }
    }
}
