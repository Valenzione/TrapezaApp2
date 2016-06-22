package com.trapezateam.trapeza.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ilgiz on 6/22/16.
 */
public class TrapezaRestClient {

    private static final String BASE_URL = "http://10.90.104.141:8080";

    private static TrapezaApi mApi;


    public static TrapezaApi getApiInstance() {
        if (mApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mApi = retrofit.create(TrapezaApi.class);
        }
        return mApi;
    }
}
