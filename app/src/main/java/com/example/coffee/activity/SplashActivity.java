package com.example.coffee.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.coffee.R;
import com.example.coffee.SharePreferenceCheckInstall;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    public static final String FIRST_INSTALL ="FIRST_INSTALL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final SharePreferenceCheckInstall preferenceCheckInstall = new SharePreferenceCheckInstall(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (preferenceCheckInstall.getBooleanValue(FIRST_INSTALL)){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user == null){
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    else {
                        startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    }

                }
                else {
                    startActivity(new Intent(SplashActivity.this, OnBoardingActivity.class));
                    preferenceCheckInstall.putBooleanInstall(FIRST_INSTALL,true);
                }
                finish();

            }
        }, 2200);
    }
}