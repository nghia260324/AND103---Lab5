package com.ph41626.and103_lab5.Services;

import com.ph41626.and103_lab5.Model.Distributor;
import com.ph41626.and103_lab5.Model.Fruit;
import com.ph41626.and103_lab5.Model.Response;
import com.ph41626.and103_lab5.Model.User;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServices {
    public static final String BASE_URL = "http://192.168.1.10:3000/";
    @GET("/get-list-distributors")
    Call<Response<ArrayList<Distributor>>> getListDistributors();
    @POST("/add-distributor")
    Call<Response<Distributor>> addDistributor(@Body Distributor distributor);
    @DELETE("/delete-distributor/{id}")
    Call<Response<Distributor>> deleteDistributor(@Path("id")String id);
    @PUT("/update-distributor/{id}")
    Call<Response<Distributor>> updateDistributor(@Path("id")String id,@Body Distributor distributor);
    @GET("/check-distributor/{id}")
    Call<Response<Distributor>> checkDistributor(@Path("id")String id);

    @Multipart
    @POST("/add-fruit")
    Call<Response<Fruit>> addFruit(@Part("name")RequestBody name,
                                     @Part("quantity")RequestBody quantity,
                                     @Part("price")RequestBody price,
                                     @Part("status")RequestBody status,
                                     @Part MultipartBody.Part thumbnail,
                                     @Part("description")RequestBody description,
                                     @Part("id_distributor")RequestBody id_distributor);

    @GET("/get-list-fruits")
    Call<Response<ArrayList<Fruit>>> getListFruits();
    @DELETE("/delete-fruit/{id_fruit}")
    Call<Response<Fruit>> deleteFruit(@Path("id_fruit")String id);
    @Multipart
    @PUT("/update-fruit/{id_fruit}")
    Call<Response<Fruit>> updateFruit(@Path("id_fruit")String id_fruit,
                                          @Part("name") RequestBody name,
                                          @Part("quantity") RequestBody quantity,
                                          @Part("price") RequestBody price,
                                          @Part("status") RequestBody status,
                                          @Part MultipartBody.Part thumbnail,
                                          @Part("description") RequestBody description,
                                          @Part("id_distributor") RequestBody id_distributor);

    @POST("/update-fruit-without-thumbnail/{id_fruit}")
    Call<Response<Fruit>> updateFruitWithoutThumbnail(@Path("id_fruit")String id,@Body Fruit fruit);
    @GET("/check-email")
    Call<Response<Boolean>> checkEmailExists(@Query("email") String email);
    @Multipart
    @POST("/register-account")
    Call<Response<User>> registerAccount(@Part("email")RequestBody email,
                                         @Part("password")RequestBody password,
                                         @Part("name")RequestBody name,
                                         @Part MultipartBody.Part avatar);
}
