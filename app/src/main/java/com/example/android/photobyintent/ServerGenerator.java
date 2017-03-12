package com.example.android.photobyintent;

import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DaniSalva on 11/03/2017.
 */
class ServiceGenerator {

    private static final String BASE_URL = "http://192.168.2.3:8080";


        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );
        /*private static final RestAdapter REST_ADAPTER = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        private static final ServerManager API_SERVICE = REST_ADAPTER.create(ServerManager.class);

        public static ServerManager getApiService() {
            return API_SERVICE;
        }*/


}