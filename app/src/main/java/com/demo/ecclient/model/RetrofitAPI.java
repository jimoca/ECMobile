package com.demo.ecclient.model;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

public interface RetrofitAPI {
    @POST("delegate")
    @Streaming
    Call<ResponseBody> delegate(@Body RequestBody delegateModel);
}
