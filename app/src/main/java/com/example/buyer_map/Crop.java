package com.example.buyer_map;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Crop extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
    }

    //final String[] subject_select = { "고구마", "감자", "깻잎", "상추", "무", "고추", "호박", "가지", "대파", "양파", "당근", "쑥갓",
            //"열무", "방울토마토", "부추", "옥수수", "치커리", "가지", "미나리"};

    final CheckBox cb1 = (CheckBox)findViewById(R.id.checkBox1);
    final CheckBox cb2 = (CheckBox)findViewById(R.id.checkBox2);
    final CheckBox cb3 = (CheckBox)findViewById(R.id.checkBox3);
    final CheckBox cb4 = (CheckBox)findViewById(R.id.checkBox4);


    Button b = (Button)findViewById(R.id.button1);
    final TextView tv = (TextView)findViewById(R.id.textView2);

        b.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
            String result = "";  // 결과를 출력할 문자열  ,  항상 스트링은 빈문자열로 초기화 하는 습관을 가지자
            if(cb1.isChecked() == true) result += cb1.getText().toString();
            if(cb2.isChecked() == true) result += cb2.getText().toString();
            if(cb3.isChecked() == true) result += cb3.getText().toString();
            if(cb4.isChecked() == true) result += cb4.getText().toString();
            tv.setText("선택결과:" + result);

        } // end onClick
    }); // end setOnClickListener

} // end onCreate()





