package com.trapezateam.trapeza.api;

import android.media.Image;

import com.trapezateam.trapeza.api.models.AuthenticationResponse;
import com.trapezateam.trapeza.api.models.CategoryResponse;
import com.trapezateam.trapeza.api.models.DeletetedDishResponse;
import com.trapezateam.trapeza.api.models.DishResponse;
import com.trapezateam.trapeza.api.models.ModifiedCategoryResponse;
import com.trapezateam.trapeza.api.models.ModifiedDishResponse;
import com.trapezateam.trapeza.api.models.SavedCategoryResponse;
import com.trapezateam.trapeza.api.models.SavedDishResponse;
import com.trapezateam.trapeza.api.models.StatusResponse;
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




    @GET("/requests?func=user.get")
    Call<List<UserResponse>> userInfo(@Query("token") String token, @Query("userId") int id);




    @GET("/requests?func=dish.get")
    Call<DishResponse> dishInfo(@Query("token") String token, @Query("dishId") int id);

    @POST("/requests?func=dish.create")
    Call<List<StatusResponse>> addDish(@Query("name") String name,
                                          @Query("photo") Image photo, @Query("description") String description,
                                          @Query("price") int price, @Query("father") int father, @Query("token") String token);

    @POST("/requests?func=dish.update")
    Call<List<StatusResponse>> modifyDish(@Query("name") String name,
                                                @Query("photo") Image photo, @Query("description") String description,
                                                @Query("dishId") int dishId, @Query("categoryId") int categoryId, @Query("token") String token);

    @POST("/requests?func=dish.delete")
    Call<List<StatusResponse>> deleteDish(@Query("dishId") int dishId, @Query("token") String token);



    @POST("/requests?func=category.create")
    Call<List<StatusResponse>> addCategory(@Query("name") String name, @Query("token") String token);

    @POST("/requests?func=category.update")
    Call<List<StatusResponse>> modifyCategory(@Query("categoryId") int id, @Query("name") String name, @Query("token") String token);

    @POST("/requests?func=category.delete")
    Call<List<StatusResponse>> deleteCategory(@Query("categoryId") int id, @Query("token") String token);

    @GET("/requests?func=category.get")
    Call<CategoryResponse> getCategory(@Query("categoryId") int id, @Query("token") String token);

}
