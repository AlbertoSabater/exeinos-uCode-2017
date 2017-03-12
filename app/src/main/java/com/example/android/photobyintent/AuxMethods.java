package com.example.android.photobyintent;

import android.content.pm.PermissionGroupInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.attr.name;


/**
 * Created by Fernando on 11/03/2017.
 */

public class AuxMethods {
    ResultActivity result;
    PhotoIntentActivity photo;
    private static final String TAG = "SOCKET";

    public AuxMethods(ResultActivity res, PhotoIntentActivity inphoto){
        result=res;
        photo=inphoto;
    }
    /**
     * Returns an array containing the words matched
     *
     * @return ArrayList<String> containing the recognized words from the phrase
     */
    protected ArrayList<String> getWords(ArrayList<String> heard, ResultModel model){

        ArrayList<String> matches = new ArrayList<String>();
        Set<String> matchedWords = new HashSet<String>();

        Set<String> heardSet = new HashSet<String>(heard);
        for (String match: heardSet)
        {
            for (String word: match.split(" "))
            {
                String result = getMatches(word,model);
                if(result!=null && !matchedWords.contains(word) ){
//                    result.toString(); // CAMBIAR PARA PONER BONITO
                    matchedWords.add(word);
                    matches.add(result.toString());
                }

            }
        }

        return matches;
    }



    protected String getMatches(String possibility,ResultModel resmodel){
        String matched = "";

        switch(possibility){
            case "price":
                matched = matched + "Precio: "+resmodel.getPrice();
                result.speakWords("El precio del producto es de "+resmodel.getPrice());
                break;
            case "precio":
                matched = matched + "Precio: "+resmodel.getPrice();
                result.speakWords("El precio del producto es de "+resmodel.getPrice());
                break;
            case "ratings":
                matched = matched + "Puntuación: "+resmodel.getRatings()+ " estrellas";
                result.speakWords("La puntuación es de "+resmodel.getRatings()+" estrellas.");
                break;
            case "valoracion":
                matched = matched + "Puntuación: "+resmodel.getRatings()+ " estrellas";
                result.speakWords("La puntuación es de "+resmodel.getRatings()+" estrellas.");
                break;
            case "valoraciones":
                matched = matched + "Puntuación: "+resmodel.getRatings()+ " estrellas";
                result.speakWords("La puntuación es de "+resmodel.getRatings()+" estrellas.");
                break;
            case "comments":
                matched = matched + "Comentarios: "+(resmodel.getSentymental_analysis()*100)+" %Positividad";
                result.speakWords("Mas del " + (resmodel.getSentymental_analysis()*100) + "% de los comentarios son positivos");
                break;
            case "comentarios":
                matched = matched + "Comentarios: "+(resmodel.getSentymental_analysis()*100)+" %Positividad";
                result.speakWords("Los comentarios son positivos");
                break;
            case "color":
                matched = matched + " Se mostrarian los colores disponibles";
                break;
            case "modelos":
                matched = matched + "Modelo: "+resmodel.getModel();
                result.speakWords("El modelo es el "+resmodel.getModel());
                break;
            case "opciones":
                matched = matched + " Se mostrarian otros modelos";
                result.speakWords("El modelo es el Adidas Gazelle");
                break;
            case "otras":
                matched = matched + " Se mostrarian otros modelos";
                break;
            case "others":
                matched = matched + " Se mostrarian otros modelos";
                break;
            case "zapatillas":
                matched = matched + " Se mostrarian otros modelos";
                break;
            case "snickers":
                matched = matched + " Se mostrarian otros modelos";
                break;
            default:
//                matched = "No estoy pillando nada " + possibility ;
                matched = null;
                break;

        }
        return matched;
    }


    private String encodeImage(String path)
    {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(imagefile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        return encImage;

    }

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

        /*Call<ResponseBody> call = apiService.upload("DummyValue", imgFiles);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("PostSnapResponse", response.message());
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("PostSnapResponse", t.getMessage());
            }
        });*/
    }

    protected void callWS_try(String photoPath,Bitmap bitmap){
        String API_BASE_URL = "http://192.168.2.5:8080";

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    // add your other interceptors …

    // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

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

        ServerManager service = retrofit.create(ServerManager.class);
        // create upload service client

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = new File(photoPath);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);

        // finally, execute the request
        Call<ResponseBody> call = service.upload(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");

                try {
                    photo.startNewActivity(response.body().string());
                } catch (IOException e) {


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
/*
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
        });*/
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
