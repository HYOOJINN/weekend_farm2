package com.example.buyer_map;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Information2 extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information2);

        //이전 화면에서 textview내용 받아오기
        TextView textinfo;      //inform2 텍뷰 이름:textinfo
        TextView textinfo2;
        TextView textinfo3;
        TextView textinfo4;
        //pw TextView textinfo2;

        Intent intent3 = getIntent();
        //1
        String selected_item2 = intent3.getStringExtra("s_name");

        textinfo = (TextView) findViewById(R.id.textinfo);
        textinfo.setText(selected_item2);

        //2
        String selected_item3 = intent3.getStringExtra("s_name2");

        textinfo2 = (TextView) findViewById(R.id.textinfo2);
        textinfo2.setText(selected_item3);

        //3
        String selected_item4 = intent3.getStringExtra("s_name3");

        textinfo3 = (TextView) findViewById(R.id.textinfo3);
        textinfo3.setText(selected_item4);

        //4
        String selected_item5 = intent3.getStringExtra("s_name4");

        textinfo4 = (TextView) findViewById(R.id.textinfo4);
        textinfo4.setText(selected_item5);

        /*//5 pw
        String selected_item6 = intent3.getStringExtra("s_name5");

        textinfo5 = (TextView) findViewById(R.id.textinfo5);
        textinfo5.setText(selected_item6);
*/

    }

}




