package com.example.buyer_map;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class request extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
    }


    public void ClickButton1(View v){
        Toast.makeText(getApplicationContext(),"요청을 완료했습니다.",Toast.LENGTH_LONG).show();
    }




}
