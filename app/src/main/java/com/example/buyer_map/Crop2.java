package com.example.buyer_map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class Crop2 extends AppCompatActivity  {

    private ListView mList2;
    private String[] data = { "고구마", "감자", "깻잎", "상추", "무", "고추", "호박", "가지", "대파", "양파", "당근", "쑥갓",
            "열무", "방울토마토", "부추", "옥수수", "치커리", "가지", "미나리"};

    private TextView selected_crop2;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop2);

        this.getEditTextObject();

        mList2 = (ListView) findViewById(R.id.listView2);
        selected_crop2 = (TextView)findViewById(R.id.selected_crop2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
               this,android.R.layout.simple_list_item_1, data);
        mList2.setAdapter(adapter);

        //리스트뷰의 아이템을 클릭시 해당 아이템의 문자열을 가져오기 위한 처리
        mList2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

                //클릭한 아이템의 문자열을 가져옴
                String selected_item2 = (String)adapterView.getItemAtPosition(position);

                //텍스트뷰에 출력
                selected_crop2.setText(selected_item2);
            }
        });
    }

    public void getEditTextObject(){ selected_crop2 = (TextView)findViewById(R.id.selected_crop2);
    }

    public void ClickButton5(View v) {

        Intent resultIntent = new Intent();
        resultIntent.putExtra("crop", selected_crop2.getText().toString());

        setResult(Code3.resultCode3, resultIntent);
        finish();
        Toast.makeText(getApplicationContext(), "농작물 선택을 완료하였습니다.", Toast.LENGTH_LONG).show();
    }


}