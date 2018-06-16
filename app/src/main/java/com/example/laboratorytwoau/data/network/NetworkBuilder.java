package com.example.laboratorytwoau.data.network;


import com.example.laboratorytwoau.config.AppConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class NetworkBuilder {
    private static RetrofitService sService;

    public static RetrofitService initRetrofitService() {
        if (sService == null) {
            sService = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClient())
                    .baseUrl(AppConstants.BASE_URL)
                    .build()
                    .create(RetrofitService.class);
        }

        return sService;
    }

    private static OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }
}
