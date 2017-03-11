package com.example.android.photobyintent;

import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;

/**
 * Created by DaniSalva on 11/03/2017.
 */
class ServiceGenerator {

    private static final String BASE_URL = "http://192.168.2.3:8080";

        private static final RestAdapter REST_ADAPTER = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        private static final ServerManager API_SERVICE = REST_ADAPTER.create(ServerManager.class);

        public static ServerManager getApiService() {
            return API_SERVICE;
        }


}