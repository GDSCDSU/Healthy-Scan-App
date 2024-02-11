package com.healthyscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splashScreen extends AppCompatActivity {
    preferenceManager prefManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        prefManger = new preferenceManager(this, "Handle_onboarding");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (prefManger.checkFirstTimeLaunch()==0){
                    startActivity(new Intent(splashScreen.this, onBoardingScreen.class));
                    finish();
                }else {
                    startActivity(new Intent(splashScreen.this, Login.class));

                }

            }
        },3000);

    }
}