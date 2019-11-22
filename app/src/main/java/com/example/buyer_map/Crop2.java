package com.example.buyer_map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class Crop2 extends AppCompatActivity  {

    private ListView mList;
    private Button mBtnGet;
    private String[] data = { "고구마", "감자", "깻잎", "상추", "무", "고추", "호박", "가지", "대파", "양파", "당근", "쑥갓",
            "열무", "방울토마토", "부추", "옥수수", "치커리", "가지", "미나리"};

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);


        mList = findViewById(R.id.listView);
        mList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        //mList.setBackgroundColor(Color.);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_multiple_choice,
                data);
        mList.setAdapter(adapter);
    }

    public void ClickButton(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "농작물 선택을 완료하였습니다.", Toast.LENGTH_LONG).show();
    }

//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btnGet:
//                SparseBooleanArray booleans = mList.getCheckedItemPositions();
//                StringBuilder sb = new StringBuilder();
//                for (int i = 0; i < data.length; i++) {
//                    if (booleans.get(i)) {
//                        sb.append(data[i]);
//                    }
//                }
//                Toast.makeText(getApplicationContext(), sb.toString(),
//                        Toast.LENGTH_SHORT).show();
//                break;
//
//            default:
//                break;
//        }
//    }


}






