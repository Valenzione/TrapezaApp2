package com.trapezateam.trapeza.api;

import android.media.Image;

import com.trapezateam.trapeza.api.models.AuthenticationResponse;
import com.trapezateam.trapeza.api.models.CategoryResponse;
import com.trapezateam.trapeza.api.models.DishResponse;
import com.trapezateam.trapeza.api.models.SavedDishResponse;
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

//    @POST("/reqauests?func=addUser")
//    Call<AuthenticationResponse> addUser(@Query("name") String name,
//                                         @Query("surname") String surname,
//                                         @Query("company") int companyId,
//                                         @Query("role") int role);

    @POST("/api/authenticate")
    Call<AuthenticationResponse> authenticate(@Query("login") String login,
                                              @Query("password") String password);


    @GET("/requests?func=dishesList")
    Call<List<DishResponse>> dishesList(@Query("token") String token);

    @GET("/requests?func=categoriesList")
    Call<List<CategoryResponse>> categoriesList(@Query("token") String token);

    @GET("/requests?func=userList")
    Call<List<UserResponse>> usersList(@Query("token") String token, @Query("company") int companyId);

    @GET("/requests?func=userInfo")
    Call<List<UserResponse>> userInfo(@Query("token") String token, @Query("user") int id);

    @GET("/requests?func=dishInfo")
    Call<DishResponse> dishInfo(@Query("token") String token, @Query("dish") int id);

    @POST("/requests?func=addDish")
    Call<List<SavedDishResponse>> addDish(@Query("name") String name,
                                          @Query("photo") Image photo, @Query("description") String description,
                                          @Query("price") int price, @Query("father") int father, @Query("token") String token);


}
