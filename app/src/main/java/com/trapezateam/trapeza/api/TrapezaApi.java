package com.trapezateam.trapeza.api;

import android.media.Image;

import com.trapezateam.trapeza.api.models.AuthenticationResponse;
import com.trapezateam.trapeza.api.models.CategoryResponse;
import com.trapezateam.trapeza.api.models.CompanyDataResponse;
import com.trapezateam.trapeza.api.models.DishResponse;
import com.trapezateam.trapeza.api.models.SaveCompleteResponse;
import com.trapezateam.trapeza.api.models.StatusResponse;
import com.trapezateam.trapeza.api.models.UserResponse;

import java.util.List;

import retrofit2.Call;
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
    Call<List<DishResponse>> dishesList(@Query("company") int id, @Query("token") String token);

    @GET("/requests?func=categoriesList")
    Call<List<CategoryResponse>> categoriesList(@Query("company") int id, @Query("token") String token);

    @GET("/requests?func=userList")
    Call<List<UserResponse>> usersList( @Query("company") int companyId,@Query("token") String token);

    @GET("/requests?func=company.getData")
    Call<CompanyDataResponse> getData(@Query("company") int companyId, @Query("token") String token);




    @GET("/requests?func=user.get")
    Call<List<UserResponse>> userInfo(@Query("token") String token, @Query("userId") int id);




    @GET("/requests?func=dish.get")
    Call<DishResponse> dishInfo(@Query("token") String token, @Query("dishId") int id);

    @POST("/requests?func=dish.create")
    Call<SaveCompleteResponse> addDish(@Query("name") String name,
                                          @Query("photo") Image photo, @Query("description") String description,
                                          @Query("price") int price, @Query("categoryId") int father, @Query("token") String token, @Query("companyId") int companyId);

    @POST("/requests?func=dish.update")
    Call<StatusResponse> modifyDish(@Query("name") String name,
                                                @Query("photo") Image photo, @Query("description") String description,
                                                @Query("dishId") int dishId, @Query("categoryId") int categoryId, @Query("token") String token);

    @POST("/requests?func=dish.delete")
    Call<StatusResponse> deleteDish(@Query("dishId") int dishId, @Query("token") String token);



    @POST("/requests?func=category.create")
    Call<SaveCompleteResponse> addCategory(@Query("name") String name, @Query("photo") String photo, @Query("token") String token);

    @POST("/requests?func=category.update")
    Call<StatusResponse> modifyCategory(@Query("categoryId") int id, @Query("name") String name, @Query("photo") String photo, @Query("token") String token);

    @POST("/requests?func=category.delete")
    Call<StatusResponse> deleteCategory(@Query("categoryId") int id, @Query("token") String token);

    @GET("/requests?func=category.get")
    Call<CategoryResponse> getCategory(@Query("categoryId") int id, @Query("token") String token);

}
