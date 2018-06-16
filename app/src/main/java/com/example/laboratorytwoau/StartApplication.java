package com.example.laboratorytwoau;

import android.app.Application;
import android.content.Context;

import com.example.laboratorytwoau.data.network.NetworkBuilder;
import com.example.laboratorytwoau.data.network.RetrofitService;

public class StartApplication extends Application {
    private RetrofitService mRetrofitService;

    @Override
    public void onCreate() {
        super.onCreate();
        mRetrofitService = NetworkBuilder.initRetrofitService();
    }

    public static StartApplication get(Context context) {
        return (StartApplication) context.getApplicationContext();
    }

    public RetrofitService getRetrofitService() {
        return mRetrofitService;
    }
}
