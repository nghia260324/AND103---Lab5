package com.ph41626.and103_lab5.Services;

import com.ph41626.and103_lab5.Model.Distributor;
import com.ph41626.and103_lab5.Model.Response;

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
    public static final String BASE_URL = "http://192.168.171.223:3000/";
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
}
