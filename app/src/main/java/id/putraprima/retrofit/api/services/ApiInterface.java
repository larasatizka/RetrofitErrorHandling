package id.putraprima.retrofit.api.services;


import android.provider.ContactsContract;

import java.util.List;

import id.putraprima.retrofit.api.models.AppVersion;
import id.putraprima.retrofit.api.models.Data;
import id.putraprima.retrofit.api.models.LoginRequest;
import id.putraprima.retrofit.api.models.LoginResponse;
import id.putraprima.retrofit.api.models.PasswordRequest;
import id.putraprima.retrofit.api.models.PasswordResponse;
import id.putraprima.retrofit.api.models.Profile;
import id.putraprima.retrofit.api.models.ProfileRequest;
import id.putraprima.retrofit.api.models.ProfileResponse;
import id.putraprima.retrofit.api.models.RegisterRequest;
import id.putraprima.retrofit.api.models.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface{
    @GET("/")
    Call<AppVersion> getAppVersion();

    @POST("/api/auth/login")
    Call<LoginResponse> doLogin(@Body LoginRequest loginRequest);

    @POST("/api/auth/register")
    Call<RegisterResponse> doRegister(@Body RegisterRequest registerRequest);

    @GET("/api/auth/me")
    Call<Data<Profile>> getProfile(@Header("Authorization") String token);

    @PATCH("/api/account/profile")
    Call<Data<Profile>> editProfile(@Header("Authorization") String token, @Body ProfileRequest Profile);

    @PATCH("/api/account/password")
    Call<Data<Profile>> editPassword(@Header("Authorization") String token, @Body PasswordRequest Profile);




//    @POST("/api/auth/register")
//    Call<LoginResponse> doLogin(@Body LoginRequest loginRequest);

}
