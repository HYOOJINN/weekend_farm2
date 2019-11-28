package com.example.buyer_map;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import gr.net.maroulis.library.EasySplashScreen;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        EasySplashScreen config = new EasySplashScreen(Splash.this)
                .withFullScreen()
                .withTargetActivity(HomeActivity.class)
                .withSplashTimeOut(3000) //3초
                .withBackgroundColor(Color.rgb(249, 247, 233 ))
                .withHeaderText("")
                .withFooterText("Welcome!")
                .withBeforeLogoText("")
                .withAfterLogoText("WEEKEND FARM")
                .withLogo(R.drawable.home);

       // config.getHeaderTextView().setTextColor(Color.WHITE);
        config.getFooterTextView().setTextColor(Color.rgb(24, 155, 163 ));
       // config.getBeforeLogoTextView().setTextColor(Color.WHITE);
        config.getAfterLogoTextView().setTextColor(Color.rgb(24, 155, 163 ));

        View easySplashScreen = config.create();
        setContentView(easySplashScreen);



    }
}