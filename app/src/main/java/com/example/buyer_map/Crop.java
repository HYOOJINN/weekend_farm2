package com.example.buyer_map;

//seller에서 연결되는 crop

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class Crop extends AppCompatActivity  {

    private ListView mList;
    private Button mBtnGet;
    String[] data = new String[]{ "가지","감자","고구마","고추",  "깻잎","당근","대파", "무","미나리",
            "방울토마토","부추","상추", "쑥갓","양파","열무","옥수수", "치커리",  "호박" };

    private TextView selected_crop;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        this.getEditTextObject();

        mList = (ListView) findViewById(R.id.listView);
        selected_crop = (TextView)findViewById(R.id.selected_crop);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, data);
        mList.setAdapter(adapter);

        //리스트뷰의 아이템을 클릭시 해당 아이템의 문자열을 가져오기 위한 처리
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

                //클릭한 아이템의 문자열을 가져옴
                String selected_item = (String)adapterView.getItemAtPosition(position);

                //텍스트뷰에 출력
                selected_crop.setText(selected_item);
            }
        });
    }

    //seller로 작물 출력하기
    public void getEditTextObject(){
        selected_crop = (TextView)findViewById(R.id.selected_crop);
    }

    public void ClickButton(View v) {

        Intent resultIntent = new Intent();
        resultIntent.putExtra("crop", selected_crop.getText().toString());

        setResult(Code2.resultCode2, resultIntent);
        finish();
        Toast.makeText(getApplicationContext(), "농작물 선택을 완료하였습니다.", Toast.LENGTH_LONG).show();
    }


}