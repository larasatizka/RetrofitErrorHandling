package id.putraprima.retrofit.api.services;


import android.provider.ContactsContract;

import java.util.List;

import id.putraprima.retrofit.api.models.AppVersion;
import id.putraprima.retrofit.api.models.Data;
import id.putraprima.retrofit.api.models.LoginRequest;
import id.putraprima.retrofit.api.models.LoginResponse;
import id.putraprima.retrofit.api.models.Profile;
import id.putraprima.retrofit.api.models.RegisterRequest;
import id.putraprima.retrofit.api.models.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface{
    @GET("/")
    Call<AppVersion> getAppVersion();

    @POST("/api/auth/login")
    Call<LoginResponse> doLogin(@Body LoginRequest loginRequest);

    @POST("/api/auth/register")
    Call<RegisterResponse> doRegister(@Body RegisterRequest registerRequest);

    @GET("/api/auth/me")
    Call<Data<Profile>> getProfile(@Header("Authorization") String token);

//    @POST("/api/auth/register")
//    Call<LoginResponse> doLogin(@Body LoginRequest loginRequest);

}
