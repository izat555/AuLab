package com.example.laboratorytwoau.ui.splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.laboratorytwoau.R;
import com.example.laboratorytwoau.ui.main.MainActivity;
import com.victor.loading.rotate.RotateLoading;

public class SplashActivity extends AppCompatActivity {
    private RotateLoading mRotateLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mRotateLoading = findViewById(R.id.rotate_loading);
        mRotateLoading.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               startActivity(new Intent(SplashActivity.this, MainActivity.class));
               mRotateLoading.stop();
               finish();
            }
        }, 3000);
    }
}
