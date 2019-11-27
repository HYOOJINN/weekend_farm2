package com.example.buyer_map;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

public class Information2 extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information2);

        //이전 화면에서 textview내용 받아오기
        TextView selected_sname2;      //inform2 텍뷰 이름:textinfo
        Intent intent3 = getIntent();
        String selected_item2 = intent3.getStringExtra("s_name");
        selected_sname2 = (TextView) findViewById(R.id.textinfo);
        selected_sname2.setText(selected_item2);



    }

}





