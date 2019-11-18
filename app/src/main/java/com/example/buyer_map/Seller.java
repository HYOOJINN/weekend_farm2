package com.example.buyer_map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Seller extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
    }


    public void buttonAdd(View v){
        Intent intent=new Intent(getApplicationContext(),Farm.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"주소 검색 버튼을 눌렀습니다",Toast.LENGTH_LONG).show();
    }

    public void buttonCr(View v){
        Intent intent=new Intent(getApplicationContext(),Crop.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"작물 검색 버튼을 눌렀습니다",Toast.LENGTH_LONG).show();
    }
    public void btnHot(View v){
        Intent intent=new Intent(getApplicationContext(),hotItem.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"Hot 작물 확인 버튼을 눌렀습니다",Toast.LENGTH_LONG).show();
    }

    public void btnComplete(View v){
        Intent intent=new Intent(getApplicationContext(),Information.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"입력완료",Toast.LENGTH_LONG).show();
    }



}
