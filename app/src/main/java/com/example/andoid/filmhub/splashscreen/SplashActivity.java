package com.example.andoid.filmhub.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.andoid.filmhub.R;

public class SplashActivity extends AppCompatActivity {


    public static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent MainActivity = new Intent(SplashActivity.this, com.example.andoid.filmhub.MainActivity.class);
                startActivity(MainActivity);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
