package com.example.buyer_map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Farm extends AppCompatActivity {

    ListView listview = null;

    private String data[] ={"경기도 고양시 덕양구 고양동 430-1",
            "경기도 고양시 덕양구 내곡동 104-2",
            "경기도 고양시 덕양구 내곡동 134-3",
            "경기도 고양시 덕양구 대덕동 492-1",
            "경기도 고양시 덕양구 대장동 561-4",
            "경기도 고양시 덕양구 대장동 563-6",
            "경기도 고양시 덕양구 대장동 567-3",
            "경기도 고양시 덕양구 덕은동 520-72",  ////
            "경기도 고양시 덕양구 덕은동 569-3",
            "경기도 고양시 덕양구 도내동 822-4",
            "경기도 고양시 덕양구 벽제동 103",
            "경기도 고양시 덕양구 벽제동 219-1",
            "경기도 고양시 덕양구 선유동 103-1",
            "경기도 고양시 덕양구 성사동 471",
            "경기도 고양시 덕양구 용두동 544",
            "경기도 고양시 덕양구 원당동 201-55 ",
            "경기도 고양시 덕양구 원당동 497",
            "경기도 고양시 덕양구 원흥동 74",
            "경기도 고양시 덕양구 화전동 529",
            "경기도 고양시 일산동구 백석동 1127-2",
            "경기도 고양시 일산동구 산황동 844",
            "경기도 고양시 일산동구 성석동 1300",
            "경기도 고양시 일산동구 지영동 504",
            "경기도 고양시 일산서구 구산동 1386-2",
            "경기도 고양시 일산서구 구산동 1867 ",
            "경기도 고양시 일산서구 덕이동 538",
            "경기도 고양시 일산서구 덕이동 551",
            "경기도 고양시 일산서구 법곳동 1541",
            "경기도 고양시 일산서구 법곳동 1567",
            "경기도 고양시 행주내동 220 ",
            "경기도 남양주시 수동면 수산리 165-1",
            "경기도 남양주시 수동면 지둔리 335 ",
            "경기도 남양주시 오남읍 오남리 699 ",
            "경기도 남양주시 와부읍 덕소리 311-3",
            "경기도 남양주시 와부읍 도곡리 1034-2",
            "경기도 남양주시 와부읍 월문리 371-3",
            "경기도 남양주시 와부읍 율석리 683-2",
            "경기도 남양주시 조안면 능내리 26-3",
            "경기도 남양주시 조안면 조안리 산 149-1",
            "경기도 남양주시 진건읍 송능리 386-18",
            "경기도 남양주시 진건읍 신월리 113-1",
            "경기도 남양주시 진접읍 부평리 266",
            "경기도 남양주시 퇴계원면 퇴계원리 357",
            "경기도 남양주시 금남리 362-7",
            "경기도 시흥시 거모동 1282-5",
            "경기도 시흥시 거모동 71 ",
            "경기도 시흥시 계수동 120",
            "경기도 시흥시 계수동 425",
            "경기도 시흥시 과림동 661-4",
            "경기도 시흥시 능곡동 707",
            "경기도 시흥시 목감동 46",
            "경기도 시흥시 방산동 산37-10 ",
            "경기도 시흥시 신천동 192",
            "경기도 시흥시 신천동 650",
            "경기도 시흥시 월곶동 425-4",
            "경기도 시흥시 정왕동 107",
            "경기도 시흥시 정왕동 1774-1",
            "경기도 시흥시 정왕동 24 ",
            "경기도 시흥시 정왕동 3-3",
            "경기도 시흥시 정왕동 855 ",
            "경기도 시흥시 정왕동 871-12",
            "서울특별시 강남구 수서동 370",
            "서울특별시 강동구 강일동 138-17",
            "서울특별시 강동구 강일동 33-3",
            "서울특별시 강동구 고덕동 479",
            "서울특별시 강동구 둔촌동 114-3",
            "서울특별시 강동구 둔촌동 118-1",
            "서울특별시 강동구 둔촌동 125-1",
            "서울특별시 강동구 상일동 12",
            "서울특별시 강동구 상일동 145-6",
            "서울특별시 강동구 암사동 199-2",
            "서울특별시 강동구 암사동 253-3",
            "서울특별시 강동구 암사동 380-27",
            "서울특별시 강동구 암사동 381",
            "서울특별시 강동구 암사동 603-3",
            "서울특별시 강동구 암사동 603-5",
            "서울특별시 강서구 개화동 682-2",
            "서울특별시 강서구 과해동 22-2  ",
            "서울특별시 강서구 오곡동 417-2  ",
            "서울특별시 강서구 오곡동 518-2",
            "서울특별시 강서구 오쇠동 151-11",
            "서울특별시 관악구 낙성대동 231-2",
            "서울특별시 관악구 낙성대동 259-1",
            "서울특별시 관악구 서림동 143-1",
            "서울특별시 관악구 성현동 495-45",
            "서울특별시 관악구 청룡동 1529-1",
            "서울특별시 관악구 청룡동 556-90",
            "서울특별시 광진구 광장동 378",
            "서울특별시 광진구 광장동 582-3",
            "서울특별시 광진구 중곡동 503-28",
            "서울특별시 구로구 궁동 125 ",
            "서울특별시 구로구 궁동 4",
            "서울특별시 구로구 궁동 53-2",
            "서울특별시 구로구 궁동 59 ",
            "서울특별시 구로구 궁동 62 ",
            "서울특별시 구로구 궁동 70-1",
            "서울특별시 노원구 공릉동 272-2",
            "서울특별시 도봉구 도봉1동 437-5",
            "서울특별시 도봉구 도봉1동 469",
            "서울특별시 도봉구 도봉동 194-31",
            "서울특별시 도봉구 도봉동 380",
            "서울특별시 도봉구 쌍문동 442-1",
            "서울특별시 도봉구 창동 산154-1",
            "서울특별시 도봉구 창동 산157",
            "서울특별시 도봉구 창동 산177",
            "서울특별시 마포구 상암동 1691 ",
            "서울특별시 서초구 내곡동 1-247",
            "서울특별시 서초구 내곡동 287",
            "서울특별시 서초구 내곡동 305",
            "서울특별시 서초구 신원동 225",
            "서울특별시 성동구 행당동 76-3 ",
            "서울특별시 송파구 마천동 199-36",
            "서울특별시 송파구방이동 444-17",
            "서울특별시 영등포구 문래동3가 55-6",
            "서울특별시 은평구 불광동 457"
    };

    private TextView selected_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm);

        this.getEditTextObject();

        listview = (ListView) findViewById(R.id.listview1);
        selected_address = (TextView)findViewById(R.id.selected_address);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1,data);
        listview.setAdapter(adapter);

    //리스트뷰의 아이템을 클릭시 해당 아이템의 문자열을 가져오기 위한 처리
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

                //클릭한 아이템의 문자열을 가져옴
                String selected_item = (String)adapterView.getItemAtPosition(position);

                //텍스트뷰에 출력
                selected_address.setText(selected_item);
            }
        });

        EditText editTextFilter = (EditText) findViewById(R.id.editTextFilter);
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString() ;
                if (filterText.length() > 0) {
                    listview.setFilterText(filterText) ;
                } else {
                    listview.clearTextFilter() ;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        }) ;
    }

    //seller로 주소 출력하기
    public void getEditTextObject(){
        selected_address = (TextView)findViewById(R.id.selected_address);
    }

    public void OnClickHandle(View view)
    {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("address", selected_address.getText().toString());
        setResult(Code.resultCode, resultIntent);
        finish();
        Toast.makeText(getApplicationContext(), "농장 선택을 완료하였습니다.", Toast.LENGTH_LONG).show();
    }

}

