package com.example.buyer_map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

// 구매자가 선택한 작물, 농장을 기반으로 판매 게시물 정보를 출력하는 Activity
public class Information extends AppCompatActivity {

    private TextView mTextViewaddress;
    private TextView mTextViewcrop;
    ListView mlistViewtitle; //textView_title
    ListView mlistViewcontnet; //textView_content
    ListView mlistViewtime;

    private static String TAG = "phpquerytest";
    private static final String TAG_JSON = "webnautes";
    private static final String TAG_title = "s_name";
    private static final String TAG_content = "content";
    private static final String TAG_time = "time";

    ArrayList<HashMap<String, String>> mArrayList;
    ArrayList<HashMap<String, String>> mArrayList2;
    ArrayList<HashMap<String, String>> mArrayList3;

    String mJsonString;
    String mJsonString1;
    String mJsonString2;

    String receive_address;
    String receive_crop;

    TextView mEditTextSearchKeyword1;
    TextView mEditTextSearchKeyword2;

    public void btnOkay2(View v) { //home화면으로 돌아가기 위한 버튼
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
       startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        //MapsActivityCurrnetPlace Activity에서 주소, 작물 정보 읽어와서 TextView에 저장하기
        Intent intent3 = getIntent();
        receive_address = intent3.getStringExtra("addressFromMain");
        mTextViewaddress = (TextView)findViewById(R.id.textView_address);
        mTextViewaddress.setText(receive_address);

        receive_crop = intent3.getStringExtra("cropFromMain");
        mTextViewcrop = (TextView) findViewById(R.id.textView_crop);
        mTextViewcrop.setText(receive_crop);

        //농장 이름, 게시물의 내용, 게시글 등록 시간을 list 형식으로 저장하기
        mlistViewtitle = (ListView) findViewById(R.id.listView_title);
        mlistViewcontnet= (ListView) findViewById(R.id.listView_content);
        mlistViewtime= (ListView) findViewById(R.id.listView_time);

        mEditTextSearchKeyword1 = (TextView) findViewById(R.id.textView_crop);
        mEditTextSearchKeyword2 = (TextView) findViewById(R.id.textView_address);

        //검색 버튼
        //버튼을 누르면 선택한 작물 기반으로 농장이름, 게시물 내용, 등록 시간의 값 가져오기
        Button button_search = (Button) findViewById(R.id.button_main_search2);
        button_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mArrayList.clear();
                mArrayList2.clear();
                mArrayList3.clear();

                GetData task = new GetData();
                task.execute(mEditTextSearchKeyword1.getText().toString(), mEditTextSearchKeyword2.getText().toString());
                Log.v("cccccccc",mEditTextSearchKeyword1.getText().toString());
                Log.v("aaaaaaaa",mEditTextSearchKeyword2.getText().toString());
            }
        });
        mArrayList = new ArrayList<>();
        mArrayList2 = new ArrayList<>();
        mArrayList3 = new ArrayList<>();

    }

    public class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {//DB와 연결하는 동안의 시간동안 화면에 출력되는 로딩 화면
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Information.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            mJsonString = result;
            mJsonString1 = result;
            mJsonString2 = result;
            showResult();
        }

        //queryForinfo.php를 통해 Android Studio와 DB를 연결하여 seller2에 저장된 정보를
        // c_name(농작물 이름)과 f_address(농장 주소)를 기준으로 select해오기
        //try-catch문을 사용해 에러를 잡는다
        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];

            String serverURL = "http://ec2-3-14-72-47.us-east-2.compute.amazonaws.com/queryForinfo.php";
            String postParameters = "c_name=" + searchKeyword1 + "&f_address=" + searchKeyword2;

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
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                errorString = e.toString();
                return null;
            }
        }
    }

    private void showResult() { //DB에서 select해온 s_name, content, time를 HashMap형태로 구현
        //HashMap으로 받아온 s_name(게시물 제목)을 Listview 형태로 저장
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String title = item.getString(TAG_title);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(TAG_title, title);
                mArrayList.add(hashMap);
            }
            ListAdapter adapter = new SimpleAdapter(
                    Information.this, mArrayList, R.layout.list_test,
                    new String[]{TAG_title},
                    new int[]{R.id.textView_list_title}
            );
            mlistViewtitle.setAdapter(adapter);
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }

        //HashMap으로 받아온 content(게시물 내용)을 Listview 형태로 저장
        try {
            JSONObject jsonObject = new JSONObject (mJsonString1);
            JSONArray jsonArray1 = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject item = jsonArray1.getJSONObject(i);
                String content = item.getString(TAG_content);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(TAG_content, content);
                mArrayList2.add(hashMap);
            }
            ListAdapter adapter = new SimpleAdapter(
                    Information.this, mArrayList2, R.layout.list_test2,
                    new String[]{TAG_content},
                    new int[]{R.id.textView_list_content}
            );
            mlistViewcontnet.setAdapter(adapter);
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }

        //HashMap으로 받아온 time(게시물 등록 시간)을 Listview 형태로 저장
        try {
            JSONObject jsonObject = new JSONObject (mJsonString2);
            JSONArray jsonArray2 = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject item = jsonArray2.getJSONObject(i);
                String time = item.getString(TAG_time);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(TAG_time, time);
                mArrayList3.add(hashMap);
            }
            ListAdapter adapter = new SimpleAdapter(
                    Information.this, mArrayList3, R.layout.list_test3,
                    new String[]{TAG_time},
                    new int[]{R.id.textView_list_time}
            );
            mlistViewtime.setAdapter(adapter);
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}





