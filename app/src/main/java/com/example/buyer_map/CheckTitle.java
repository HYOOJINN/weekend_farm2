package com.example.buyer_map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class CheckTitle extends AppCompatActivity {

    //선택한 제목 기반 정보 출력
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON = "webnautes";
    private static final String TAG_SNAME = "s_name";
    private static final String TAG_CNAME = "c_name";
    private static final String TAG_FADDRESS = "f_address";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_PW = "pw";

    String mJsonString;
    String mJsonString2;
    String mJsonString3;
    String mJsonString4;
    String mJsonString5;

    ListView sListViewList;                         //위에 값을 담을 리스트뷰

    String[] arraylist_addr;    //이전 화면에서 받아온 게시글제목 배열

    private TextView selected_sname;    //제목을 선택하면 그대로 보여줄 텍스트뷰
    private TextView selected_sname2;
    private TextView selected_sname3;
    private TextView selected_sname4;
    private TextView selected_sname5;

    ArrayList<HashMap<String, String>> sArrayList;  //db결과값 담을 arraylist
    ArrayList<HashMap<String, String>> sArrayList2;
    ArrayList<HashMap<String, String>> sArrayList3;
    ArrayList<HashMap<String, String>> sArrayList4;
    ArrayList<HashMap<String, String>> sArrayList5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_title);

        //db 실행후에 select 값이 보여질 listview
        sListViewList = (ListView) findViewById(R.id.listView_main_list);

        //선택된 제목 나타날 textview
        selected_sname = (TextView) findViewById(R.id.selected_sname);
        selected_sname2 = (TextView) findViewById(R.id.selected_sname2);
        selected_sname3 = (TextView) findViewById(R.id.selected_sname3);
        selected_sname4 = (TextView) findViewById(R.id.selected_sname4);
        selected_sname5 = (TextView) findViewById(R.id.selected_sname5);

        //check2에서 주소를 통해 받아온 글제목 listview에 출력하기(아래 listview)
        ListView sname_listview = (ListView) findViewById(R.id.sname_listview);
        Intent intent2 = getIntent();
        arraylist_addr = intent2.getExtras().getStringArray("arr_addr");
        for (int j = 0; j < arraylist_addr.length; j++) {
            Log.d("###", arraylist_addr[j]);
            selected_sname = (TextView) findViewById(R.id.selected_sname);  //선택된 글제목 택뷰에 나타내기
        }

        //리스트속의 아이템 연결
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, arraylist_addr);
        sname_listview.setAdapter(adapter);


        //리스트뷰의 아이템을 클릭시 해당 아이템의 문자열을 가져오기 위한 처리(다음화면으로 넘기는 것과 관련)
        sname_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

                //클릭한 아이템의 문자열을 가져옴
                String selected_item2 = (String) adapterView.getItemAtPosition(position);

                selected_sname.setText("");

                sArrayList.clear();

                GetData task = new GetData();
                task.execute(selected_item2);

                selected_sname2.setText("");
                selected_sname3.setText("");
                selected_sname4.setText("");
                selected_sname5.setText("");
            }
        });
        sArrayList = new ArrayList<>();
        sArrayList2 = new ArrayList<>();
        sArrayList3 = new ArrayList<>();
        sArrayList4 = new ArrayList<>();
        sArrayList5 = new ArrayList<>();
    }

    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(CheckTitle.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            mJsonString = result;
            mJsonString2 = result;
            mJsonString3 = result;
            mJsonString4 = result;
            mJsonString5 = result;

            showResult();

            Intent intent3 = new Intent(getApplicationContext(), Information2.class);
            intent3.putExtra("s_name", selected_sname.getText().toString());
            intent3.putExtra("s_name2",selected_sname2.getText().toString());
            intent3.putExtra("s_name3",selected_sname3.getText().toString());
            intent3.putExtra("s_name4",selected_sname4.getText().toString());
            intent3.putExtra("s_name5",selected_sname5.getText().toString());
            startActivity(intent3);
        }


        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];


            String serverURL = "http://ec2-3-134-104-28.us-east-2.compute.amazonaws.com/content.php";;
            String postParameters = "s_name=" + searchKeyword1 ;

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

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }



        private void showResult(){
////////1
            try {
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for(int i=0;i<jsonArray.length();i++){

                    JSONObject item = jsonArray.getJSONObject(i);
                    String s_name = item.getString(TAG_SNAME);


                    HashMap<String,String> hashMap = new HashMap<>();

                    hashMap.put(TAG_SNAME, s_name);

                    sArrayList.add(hashMap);
                }

                for(int i=0;i<jsonArray.length();i++){

                    HashMap<String, String> outputHashMap = sArrayList.get(i);
                    String s_name = outputHashMap.get("s_name");

                    String str = String.format(getResources()

                            .getString(R.string.textview_message1), s_name);
                    selected_sname.append(str);
                }

            } catch (JSONException e) {

                Log.d(TAG, "showResult : ", e);
            }

////////2
            try {
                JSONObject jsonObject = new JSONObject(mJsonString2);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for(int i=0;i<jsonArray.length();i++){

                    JSONObject item = jsonArray.getJSONObject(i);
                    String c_name = item.getString(TAG_CNAME);

                    HashMap<String,String> hashMap = new HashMap<>();

                    hashMap.put(TAG_CNAME, c_name);

                    sArrayList2.add(hashMap);
                }

                for(int i=0;i<jsonArray.length();i++){

                    HashMap<String, String> outputHashMap = sArrayList2.get(i);
                    String c_name = outputHashMap.get("c_name");

                    String str2 = String.format(getResources()

                            .getString(R.string.textview_message2), c_name);
                    selected_sname2.append(str2);
                }

            } catch (JSONException e) {

                Log.d(TAG, "showResult : ", e);
            }

////////3
            try {
                JSONObject jsonObject = new JSONObject(mJsonString3);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for(int i=0;i<jsonArray.length();i++){

                    JSONObject item = jsonArray.getJSONObject(i);

                    String f_address = item.getString(TAG_FADDRESS);

                    HashMap<String,String> hashMap = new HashMap<>();

                    hashMap.put(TAG_FADDRESS, f_address);
                    sArrayList3.add(hashMap);
                }

                for(int i=0;i<jsonArray.length();i++){

                    HashMap<String, String> outputHashMap = sArrayList3.get(i);

                    String f_address = outputHashMap.get("f_address");

                    String str2 = String.format(getResources()

                            .getString(R.string.textview_message3), f_address);
                    selected_sname3.append(str2);

                    sArrayList3.clear();

                }

            } catch (JSONException e) {

                Log.d(TAG, "showResult : ", e);
            }

            try {
                JSONObject jsonObject = new JSONObject(mJsonString4);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for(int i=0;i<jsonArray.length();i++){

                    JSONObject item = jsonArray.getJSONObject(i);


                    String content = item.getString(TAG_CONTENT);

                    HashMap<String,String> hashMap = new HashMap<>();



                    hashMap.put(TAG_CONTENT, content);

                    sArrayList4.add(hashMap);
                }

                for(int i=0;i<jsonArray.length();i++){

                    HashMap<String, String> outputHashMap = sArrayList4.get(i);


                    String content = outputHashMap.get("content");

                    String str2 = String.format(getResources()

                            .getString(R.string.textview_message4), content);
                    selected_sname4.append(str2);

                    sArrayList4.clear();

                }

            } catch (JSONException e) {

                Log.d(TAG, "showResult : ", e);
            }

            try {
                JSONObject jsonObject = new JSONObject(mJsonString5);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for(int i=0;i<jsonArray.length();i++){

                    JSONObject item = jsonArray.getJSONObject(i);


                    String pw = item.getString(TAG_PW);

                    HashMap<String,String> hashMap = new HashMap<>();

                    hashMap.put(TAG_PW, pw);

                    sArrayList5.add(hashMap);
                }

                for(int i=0;i<jsonArray.length();i++){
                    HashMap<String, String> outputHashMap = sArrayList5.get(i);
                    String pw = outputHashMap.get("pw");
                    String str2 = String.format(getResources().getString(R.string.textview_message5), pw);
                    selected_sname5.append(str2);
                    sArrayList5.clear();
                }

            } catch (JSONException e) {

                Log.d(TAG, "showResult : ", e);
            }

        }

    }



}
