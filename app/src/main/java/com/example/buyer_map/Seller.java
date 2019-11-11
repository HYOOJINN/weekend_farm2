package com.example.buyer_map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Seller extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
    }


    public void SearchButton1(View v){
        Intent intent=new Intent(getApplicationContext(),Farm.class);
        startActivity(intent);
        //Toast.makeText(getApplicationContext(),"판매자 버튼을 눌렀습니다",Toast.LENGTH_LONG).show();
    }

    public void SearchButton2(View v){
        //test5
        Intent intent=new Intent(getApplicationContext(),Crop.class);
        startActivity(intent);

    }
    public void SearchButton3(View v){
        Intent intent=new Intent(getApplicationContext(),Crop.class);
        startActivity(intent);
        //Toast.makeText(getApplicationContext(),"판매자 버튼을 눌렀습니다",Toast.LENGTH_LONG).show();
    }
}
