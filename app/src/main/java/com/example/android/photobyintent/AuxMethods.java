package com.example.android.photobyintent;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Fernando on 11/03/2017.
 */

public class AuxMethods {

    /**
     * Returns an array containing the words matched
     *
     * @return ArrayList<String> containing the recognized words from the phrase
     */
    protected ArrayList<String> getWords(ArrayList<String> heard){

        ArrayList<String> matches = new ArrayList<String>();
        for (String match: heard)
        {
            for (String word: match.split(" "))
            {
                String matched = getMatches(word);
                if(matched!=null){
                    matched.toString(); // CAMBIAR PARA PONER BONITO
                    matches.add(matched.toString());
                }

            }
        }

        return matches;
    }

    protected String getMatches(String possibility){
        String matched = "";

        switch(possibility){
            case "precio":
                matched = matched + " Se mostraria el precio";
                break;
            case "tamaño":
                matched = matched + " Se mostrarian los tamaños displonibles";
                break;
            case "colores":
                matched = matched + " Se mostrarian los colores disponibles";
                break;
            case "modelos":
                matched = matched + " Se mostrarian otros modelos";
                break;
            default:
//                matched = "No estoy pillando nada " + possibility ;
                matched = null;
                break;

        }
        return matched;
    }

    /*protected void callWS_test() {
        ServiceGenerator.getApiService().test(new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.e("test", "success");

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("test", "Fail " +error.getMessage());

            }
        });
    }

    protected void WSAndroid(){
    }
    */
    protected void callWS_upload(String photoPath){
        String API_BASE_URL = "http://192.168.2.3:8080";

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        ServerManager apiService = retrofit.create(ServerManager.class);
        MediaType MEDIA_TYPE = MediaType.parse("image/jpg");

        File file;
        Map<String, RequestBody> imgFiles = new HashMap<String,RequestBody>();
        //for(String imgName: imgFilePaths.keySet()){
            file = new File(photoPath);//imgFilePaths.get(imgName));
            imgFiles.put("fileilename=\""+"zapa",RequestBody.create(MEDIA_TYPE, file));
        //}

        Call<ResponseBody> call = apiService.upload("DummyValue", imgFiles);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("PostSnapResponse", response.message());
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("PostSnapResponse", t.getMessage());
            }
        });
    }

    protected void callWS_try(String photoPath,Bitmap bitmap){
        String API_BASE_URL = "http://192.168.2.3:8080";

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        ServerManager apiService = retrofit.create(ServerManager.class);

        InputStream inputStream = null;//You can get an inputStream using any IO API
        Log.d("debug",photoPath);
        try {
            inputStream = new FileInputStream(photoPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();

        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);

        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
        String str = null; // for UTF-8 encoding
        try {
            str = new String(b,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Call<ResponseBody> call = apiService.prueba(temp);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("PostSnapResponse", response.message());
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("PostSnapResponse", t.getMessage());
            }
        });
    }
    /*
    protected void callWS_upload(String photoPath){
        TypedFile typedFile = new TypedFile("multipart/form-data", new File(photoPath));
        String description = "hello, this is description speaking";

        ServiceGenerator.getApiService().upload(typedFile, description, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.e("Upload", "success");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Upload", "error");
            }
        });
    }*/


}
