package com.example.android.photobyintent;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by DSalvador on 11/03/2017.
 */
public interface ServerManager {

        @Multipart
        @POST("upload")
        Call<ResponseBody> upload(
                @Part("description") RequestBody description,
                @Part MultipartBody.Part file
        );

        @GET("download")
        Call<ResponseBody> download();

}
