package com.example.buyer_map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.buyer_map.R.id.listView_main_FarmName;
import static com.example.buyer_map.R.id.listView_main_listY;

//구매자 입장에서 처음 등장하는 Activity : 구매를 원하는 작물 선택하기
public class Buyer extends AppCompatActivity {

    //작물 리스트
    private ListView mList;
    String[] data = new String[]{ "가지","감자","고구마","고추",  "깻잎","당근","대파", "무","미나리",
            "방울토마토","부추","상추", "쑥갓","양파","열무","옥수수", "치커리",  "호박" };

    private TextView selected_crop2;

    //선택한 작물 기반 주소 출력
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON = "webnautes";
    private static final String TAG_x = "x";
    private static final String TAG_y = "y";
    private static final String TAG_FADDRESS ="f_address";
    private static final String TAG_FNAME ="f_name";

    ArrayList<HashMap<String, String>> mArrayList;
    ArrayList<HashMap<String, String>> mArrayList2;
    ArrayList<HashMap<String, String>> mArrayList3;
    ArrayList<HashMap<String, String>> mArrayList4;
    ListView mListViewList; //address
    ListView mListViewList2; // X
    ListView mListViewList3; // Y
    ListView mListViewList4; //listView_main_FarmName
    TextView mEditTextSearchKeyword1;
    String mJsonString1;
    String mJsonString2;
    String mJsonString3;
    String mJsonString4;
    String[] arr_X;
    String[] arr_Y;
    String[] arr_address;
    String[] arr_farmName;
    String selected_item2 ;

    public void nextgo(View v) { //MapsActivityCurrentPlace로 DB에서 받아온 x, y, f_address, f_name과
        //                          선택한 작물의 값을 intent를 사용해 전달
        Intent intent = new Intent(getApplicationContext(), MapsActivityCurrentPlace.class);
        intent.putExtra("arr_x", arr_X);
        intent.putExtra("arr_y", arr_Y);
        intent.putExtra("arr_address", arr_address);
        intent.putExtra("arr_farmName", arr_farmName);
        intent.putExtra("cropFromBuyer",selected_item2);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);

        //작물 리스트에서 선택한 값 textview에 출력
        this.getEditTextObject();

        mList = (ListView) findViewById(R.id.crop_list);
        selected_crop2 = (TextView) findViewById(R.id.selected_crop2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, data);
        mList.setAdapter(adapter);

        //리스트뷰의 아이템을 클릭시 해당 아이템의 문자열을 가져오기 위한 처리
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

                //클릭한 아이템의 문자열을 가져옴
                selected_item2 = (String) adapterView.getItemAtPosition(position);
                //텍스트뷰에 출력
                selected_crop2.setText(selected_item2);
            }
        });


        //선택한 작물의 농장 주소,이름,X,Y좌표를 list 형식으로 저장하기
        mListViewList = (ListView) findViewById(R.id.listView_address_list);
        mListViewList2 = (ListView) findViewById(R.id.listView_main_listX);
        mListViewList3 = (ListView) findViewById(listView_main_listY);
        mListViewList4 = (ListView) findViewById(listView_main_FarmName);

        mEditTextSearchKeyword1 = (TextView) findViewById(R.id.selected_crop2);

        //검색 버튼
        //버튼을 누르면 선택한 작물 기반으로 농장 주소,이름,X,Y좌표의 값 가져오기
        Button button_search = (Button) findViewById(R.id.button_main_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mArrayList.clear();
                mArrayList2.clear();
                mArrayList3.clear();
                mArrayList4.clear();

                GetData task = new GetData();
                task.execute(mEditTextSearchKeyword1.getText().toString());
            }
        });
        mArrayList = new ArrayList<>();
        mArrayList2 = new ArrayList<>();
        mArrayList3 = new ArrayList<>();
        mArrayList4 = new ArrayList<>();
    }

    //작물 리스트에서 선택한 값 textview에 출력
    public void getEditTextObject() {
        selected_crop2 = (TextView) findViewById(R.id.selected_crop2);
    }


    public class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;
        @Override
        protected void onPreExecute() {//DB와 연결하는 동안의 시간동안 화면에 출력되는 로딩 화면
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Buyer.this,
                    "Please Wait", null, true, true);
        }
        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            mJsonString1 = result;
            mJsonString2 = result;
            mJsonString3 = result;
            mJsonString4 = result;
            showResult();
        }

        @Override
        //queryForBuyer.php를 통해 Android Studio와 DB를 연결하여 seller2에 저장된 정보를
        // c_name(농작물 이름)을 기준으로 select해오기
        //try-catch문을 사용해 에러를 잡는다
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];

            String serverURL = "http://ec2-3-14-72-47.us-east-2.compute.amazonaws.com/queryForBuyer.php";
            String postParameters = "c_name=" + searchKeyword1;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }
        }
    }

    private void showResult() { // DB에서 select해온 f_address, f_name, x, y를 HashMap형태로 구현
        //HashMap으로 받아온 f_address(농장 주소)를 Listview 형태로 저장
        try {
            JSONObject jsonObject = new JSONObject(mJsonString1);
            JSONArray jsonArray1 = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray1.length();i++){

                JSONObject item = jsonArray1.getJSONObject(i);
                String f_address = item.getString(TAG_FADDRESS);
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(TAG_FADDRESS, f_address);
                mArrayList.add(hashMap);
            }
            ListAdapter adapter = new SimpleAdapter(
                    Buyer.this, mArrayList, R.layout.item_list,
                    new String[]{TAG_FADDRESS},
                    new int[]{R.id.textView_list_address}
            );
            mListViewList.setAdapter(adapter);
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }

        //HashMap으로 받아온 x좌표를 Listview 형태로 저장
        try {
            JSONObject jsonObject = new JSONObject(mJsonString2);
            JSONArray jsonArray2 = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray2.length(); i++) {

                JSONObject item = jsonArray2.getJSONObject(i);
                String x = item.getString(TAG_x);
                HashMap<String, String> hashMap1 = new HashMap<>();
                hashMap1.put(TAG_x, x);
                mArrayList2.add(hashMap1);
            }
            ListAdapter adapter = new SimpleAdapter(
                    Buyer.this, mArrayList2, R.layout.list_xy,
                    new String[]{TAG_x},
                    new int[]{R.id.x_coordinates}
            );
            mListViewList2.setAdapter(adapter);
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }

        //HashMap으로 받아온 y좌표를 Listview 형태로 저장
        try {
            JSONObject jsonObject = new JSONObject(mJsonString3);
            JSONArray jsonArray3 = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray3.length(); i++) {

                JSONObject item = jsonArray3.getJSONObject(i);
                String y = item.getString(TAG_y);
                HashMap<String, String> hashMap2 = new HashMap<>();
                hashMap2.put(TAG_y, y);
                mArrayList3.add(hashMap2);
            }
            ListAdapter adapter = new SimpleAdapter(
                    Buyer.this, mArrayList3, R.layout.list_y,
                    new String[]{TAG_y},
                    new int[]{R.id.y_coordinates}
            );
            mListViewList3.setAdapter(adapter);
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }

        //HashMap으로 받아온 f_name(농장 이름)를 Listview 형태로 저장
        try {
            JSONObject jsonObject = new JSONObject(mJsonString4);
            JSONArray jsonArray4 = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray4.length(); i++) {

                JSONObject item = jsonArray4.getJSONObject(i);
                String f_name = item.getString(TAG_FNAME);
                HashMap<String, String> hashMap3 = new HashMap<>();
                hashMap3.put(TAG_FNAME, f_name);
                mArrayList4.add(hashMap3);
            }
            ListAdapter adapter = new SimpleAdapter(
                    Buyer.this, mArrayList4, R.layout.list_fname,
                    new String[]{TAG_FNAME},
                    new int[]{R.id.textview_f_name}
            );
            mListViewList4.setAdapter(adapter);
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }

        //String형의 X좌표 받아와서 #을 기준으로 분할하여 String형 배열에 저장
        String sum = "";
        for (HashMap<String, String> hash : mArrayList2) {
            for (String current : hash.values()) {
                sum = sum + current + "<#>";
            }
        }
        arr_X = sum.split("<#>");

        //String형의 Y좌표 받아와서 #을 기준으로 분할하여 String형 배열에 저장
        String sum2 = "";
        for (HashMap<String, String> hash : mArrayList3) {
            for (String current : hash.values()) {
                sum2 = sum2 + current + "<#>";
            }
        }
        arr_Y = sum2.split("<#>");

        //String형의 F_address 받아와서 #을 기준으로 분할하여 String형 배열에 저장
        String sum3 = "";
        for (HashMap<String, String> hash : mArrayList) {
            for (String current : hash.values()) {
                sum3 = sum3 + current + "<#>";
            }
        }
        arr_address = sum3.split("<#>");

        //String형의 F_name좌표 받아와서 #을 기준으로 분할하여 String형 배열에 저장
        String sum4 = "";
        for (HashMap<String, String> hash : mArrayList4) {
            for (String current : hash.values()) {
                sum4 = sum4 + current + "<#>";
            }
        }
        arr_farmName = sum4.split("<#>");

    }
}





