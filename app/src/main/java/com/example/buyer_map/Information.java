package com.example.buyer_map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Information extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
    }
    public void btnOkay(View v){
        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"판매 정보가 정상적으로 입력되었습니다",Toast.LENGTH_LONG).show();
    }
}