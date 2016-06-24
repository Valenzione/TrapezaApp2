package com.trapezateam.trapeza.api;

import com.trapezateam.trapeza.api.models.AuthenticationResponse;
import com.trapezateam.trapeza.api.models.DishResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ilgiz on 6/21/16.
 */
public interface TrapezaApi {

    @POST("/api/authenticate")
    Call<AuthenticationResponse> authenticate(@Query("login")  String login,
                                              @Query("password") String password);

    @GET("/requests?func=dishesList")
    Call<List<DishResponse>> dishesList(@Query("token") String token);
}
