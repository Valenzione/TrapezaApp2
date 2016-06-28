package com.trapezateam.trapeza.api;

import com.trapezateam.trapeza.api.models.AuthenticationResponse;
import com.trapezateam.trapeza.api.models.DishResponse;
import com.trapezateam.trapeza.api.models.UserResponse;

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

    @GET("/requests?func=userList")
    Call<List<UserResponse>> usersList(@Query("token") String token);

    @GET("/requests?func=userInfo")
    Call<UserResponse> userInfo(@Query("token") String token);

    @GET("/requests?func=dishInfo")
    Call<DishResponse> dishInfo(@Query("token") String token);

    @POST("/api/addDish")
    Call<AuthenticationResponse> addDish(@Query("name ")  String login,
                                              @Query("photo") String password, @Query("description") String description,
                                         @Query("price") int price);

}
