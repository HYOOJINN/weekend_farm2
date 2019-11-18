package com.example.buyer_map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class hotItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_item);
    }
    public void btnOkay(View v){
        Intent intent=new Intent(getApplicationContext(),Seller.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"판매자 화면으로 돌아갑니다",Toast.LENGTH_LONG).show();
    }
}

