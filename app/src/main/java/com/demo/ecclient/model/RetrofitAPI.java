package com.demo.ecclient.model;

import java.math.BigInteger;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

public interface RetrofitAPI {

    @GET("start")
    Call<TaskModel> start();

    @POST("delegate")
    @Streaming
    Call<ResponseBody> delegate(@Body RequestBody delegateModel);


    @GET("discover")
    Call<List<EdgeInfo>> discover();
}
