package com.example.android.photobyintent;

import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;


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

    protected void callWS_test() {
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
    }


}
