package com.trapezateam.trapeza.api;

import com.trapezateam.trapeza.api.models.AuthenticationResponse;
import com.trapezateam.trapeza.api.models.DishResponse;

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
        if(mToken == null) {
            throw new IllegalStateException("Token is not set yet");
        }
        return mToken;
    }

    public static void authenticate(String login, String password,
                      Callback<AuthenticationResponse> callback) {
        getApiInstance().authenticate(login, password).enqueue(callback);
    }

    static void dishesList(Callback<List<DishResponse>> callback) {
        getApiInstance().dishesList(getToken()).enqueue(callback);
    }



}
