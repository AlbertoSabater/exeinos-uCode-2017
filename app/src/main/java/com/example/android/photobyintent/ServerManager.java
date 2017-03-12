package com.example.android.photobyintent;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;


/**
 * Created by DSalvador on 11/03/2017.
 */
public interface ServerManager {


    @Multipart
    @POST("/upload")
    Call<ResponseBody> prueba(@Part("image") String user);

    @Multipart
    @POST("/upload")
    Call<ResponseBody> upload(
            @Part MultipartBody.Part file
    );
    /*@Multipart
    @POST("/upload")
    void upload(@Part("myfile") TypedFile file,
                @Part("description") String description,
                Callback<String> cb);


    @GET("/test")
     void test(Callback<String> cb);*/

       // @GET("/download")
       // Call<ResponseBody> download();

}
