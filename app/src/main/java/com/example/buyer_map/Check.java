package com.example.buyer_map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Check extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
    }
    public void putinfo(View v) {
        Intent intent = new Intent(getApplicationContext(), Seller.class);
        startActivity(intent);
//        Toast.makeText(getApplicationContext(), "판매정보입력 버튼을 눌렀습니다 ", Toast.LENGTH_LONG).show();
    }
    public void getinfo(View v) {
        Intent intent = new Intent(getApplicationContext(), Check2.class);
        startActivity(intent);
//        Toast.makeText(getApplicationContext(), "판매정보확인 버튼을 눌렀습니다 ", Toast.LENGTH_LONG).show();
    }


}