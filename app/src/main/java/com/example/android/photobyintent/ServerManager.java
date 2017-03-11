package com.example.android.photobyintent;
import com.squareup.okhttp.ResponseBody;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;


/**
 * Created by DSalvador on 11/03/2017.
 */
public interface ServerManager {


    @Multipart
    @POST("/upload")
    void upload(@Part("myfile") TypedFile file,
                @Part("description") String description,
                Callback<String> cb);

    @GET("/test")
     void test(Callback<String> cb);

       // @GET("/download")
       // Call<ResponseBody> download();

}
