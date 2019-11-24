package com.example.buyer_map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void ClickButton1(View v) {
        Intent intent = new Intent(getApplicationContext(), Seller.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "판매자 버튼을 눌렀습니다 ", Toast.LENGTH_LONG).show();
    }

    public void ClickButton2(View v) {
        Intent intent = new Intent(getApplicationContext(), Crop2.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "구매자 버튼을 눌렀습니다", Toast.LENGTH_LONG).show();
    }

    public void ClickButton3(View v) {
        Intent intent = new Intent(getApplicationContext(), request.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "요청 버튼을 눌렀습니다.", Toast.LENGTH_LONG).show();
    }
}



