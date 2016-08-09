package com.trapezateam.trapeza.api;

import com.trapezateam.trapeza.api.models.AuthenticationResponse;
import com.trapezateam.trapeza.api.models.CategoryResponse;
import com.trapezateam.trapeza.api.models.CompanyDataResponse;
import com.trapezateam.trapeza.api.models.DishResponse;
import com.trapezateam.trapeza.api.models.SaveCompleteResponse;
import com.trapezateam.trapeza.api.models.StatisticsResponse;
import com.trapezateam.trapeza.api.models.StatusResponse;
import com.trapezateam.trapeza.api.models.UserResponse;

import java.io.File;
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
    Call<List<UserResponse>> usersList(@Query("company") int companyId, @Query("token") String token);


    @GET("/requests?func=company.getData")
    Call<CompanyDataResponse> getData(@Query("company") int companyId, @Query("token") String token);

    @POST("/api/register")
    Call<SaveCompleteResponse> addCompany(@Query("name") String name, @Query("phone") String phone, @Query("addr") String addres,
                                          @Query("login") String login, @Query("pass") String pass, @Query("userPhone") String userPhone,
                                          @Query("userName") String userName, @Query("userSurname") String userSurname);


    @GET("/requests?func=user.get")
    Call<List<UserResponse>> userInfo(@Query("token") String token, @Query("userId") int id);


    @POST("/requests?func=user.create")
    Call<SaveCompleteResponse> addUser(@Query("login") String login, @Query("pass") String pass,
                                       @Query("phone") String phone, @Query("name") String name,
                                       @Query("surname") String surname, @Query("companyId") int companyId,
                                       @Query("role") int role, @Query("token") String token);

    @POST("/requests?func=user.update")
    Call<StatusResponse> modifyUser(@Query("phone") String phone, @Query("name") String name,
                                    @Query("surname") String surname, @Query("role") int role,
                                    @Query("token") String token);

    @POST("/requests?func=user.delete")
    Call<StatusResponse> deleteUser(@Query("userId") int userId, @Query("token") String token);

    @POST("/requests?func=user.resetPass")
    Call<StatusResponse> resetPassword(@Query("oldPass") String oldPass, @Query("newPass") String newPass,
                                       @Query("userId") int userID, @Query("token") String token);


    @GET("/requests?func=dish.get")
    Call<DishResponse> dishInfo(@Query("token") String token, @Query("dishId") int id);

    @POST("/requests?func=dish.create")
    Call<SaveCompleteResponse> addDish(@Query("name") String name,
                                       @Query("photo") String photo, @Query("description") String description,
                                       @Query("price") int price, @Query("categoryId") int father, @Query("token") String token, @Query("companyId") int companyId);

    @POST("/requests?func=dish.update")
    Call<StatusResponse> modifyDish(@Query("name") String name,
                                    @Query("photo") String photo, @Query("description") String description,
                                    @Query("dish") int dishId, @Query("price") int price, @Query("token") String token);

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


    @GET("/requests?func=boughtMonth")
    Call<File> boughtMonth(@Query("token") String token);

    @GET("/requests?func=boughtDay")
    Call<StatisticsResponse> boughtDay(@Query("token") String token);

    /**
     * @param prices      JSON array [{"id":49, "num":10}]
     * @param paymentType <code>0</code> - pays in cash. <code>1</code> - pays by card
     * @param sale
     * @param token
     * @return
     */
    @POST("/requests?func=buyDish")
    Call<StatusResponse> buyDish(@Query("prices") String prices, @Query("payment") int paymentType,
                                 @Query("sale") double sale,
                                 @Query("token") String token);


}
