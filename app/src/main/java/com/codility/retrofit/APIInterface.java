package com.codility.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;

public interface APIInterface {

    @GET("backup/db")
    @Streaming
    Call<ResponseBody> getData(@Header("authorization") String header);

}
