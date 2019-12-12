package com.example.buyer_map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

//판매자 버튼을 누르면 나타나는 첫 Activity
public class Check extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
    }
    //'판매정보입력 버튼' - seller Activity로 화면 전환
    public void putinfo(View v) {
        Intent intent = new Intent(getApplicationContext(), Seller.class);
        startActivity(intent);
    }
    //'판매정보확인 버튼' - Check2 Activity로 화면 전환
    public void getinfo(View v) {
        Intent intent = new Intent(getApplicationContext(), Check2.class);
        startActivity(intent);
    }
}