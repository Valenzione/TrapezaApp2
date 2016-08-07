package com.trapezateam.trapeza.api;

import com.trapezateam.trapeza.api.models.AuthenticationResponse;
import com.trapezateam.trapeza.api.models.UploadResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created com.trapezateam.trapeza.api in Trapeza
 * Created by Yuriy on 8/5/2016.
 */
public interface TrapezaUploadApi {

    @Multipart
    @POST("/api/upload")
    Call<UploadResponse> upload(@Part("file") RequestBody file);
}
