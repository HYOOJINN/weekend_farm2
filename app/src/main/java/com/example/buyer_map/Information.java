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

public class Information extends AppCompatActivity {

    private TextView mTextViewaddress;
    private TextView mTextViewcrop;
    String receive_address;
    String receive_crop;
    ListView mlistViewtitle; //textView_title
    ListView mlistViewcontent; //textView_content
    ArrayList<HashMap<String, String>> mArrayList_c;
    ArrayList<HashMap<String, String>> mArrayList_a;
    String mJsonString_c;
    String mJsonString_a;
    private static String TAG = "phpqueryplease";
    private static final String TAG_JSON = "webnautes";
    private static final String TAG_title = "s_name";
    private static final String TAG_content = "content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        ///////주소, 작물 값 읽어와서 저장하기기
        Intent intent3 = getIntent();
        receive_address = intent3.getStringExtra("addressFromMain");
        mTextViewaddress = (TextView)findViewById(R.id.textView_address);
        mTextViewaddress.setText(receive_address);

        receive_crop = intent3.getStringExtra("cropFromMain");
        mTextViewcrop = (TextView) findViewById(R.id.textView_crop);
        mTextViewcrop.setText(receive_crop);

        ////////////////////////////////

        mlistViewtitle = (ListView) findViewById(R.id.listView_title);
        mlistViewcontent = (ListView) findViewById(R.id.listView_content);

        Button button_search = (Button) findViewById(R.id.button_main_search2);
        button_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mArrayList_c.clear();
                mArrayList_a.clear();

                GetData2 task = new GetData2();
                task.execute(receive_crop,receive_address);
                Log.v("rrrrrrrrrrrrrrrrr",receive_crop);
                Log.v("aaaaaaaaaaaaaaaaaaaaa",receive_address);

            }
        });
        mArrayList_c = new ArrayList<>();
        mArrayList_a = new ArrayList<>();
    }


    public class GetData2 extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Information.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            mJsonString_c= result;
            mJsonString_a = result;
            showResult2();
        }


        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];


            String serverURL2 = "http://ec2-3-134-104-28.us-east-2.compute.amazonaws.com/queryforinfo2.php";
            String postParameters = "c_name=" + searchKeyword1 + "&f_address" + searchKeyword2;

            try {

                URL url = new URL(serverURL2);
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


    private void showResult2() {

        //주소찍기
        try {
            JSONObject jsonObject = new JSONObject(mJsonString_c);
            JSONArray jsonArray1 = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray1.length(); i++) {


                JSONObject item = jsonArray1.getJSONObject(i);

                String s_name = item.getString(TAG_title);

                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put(TAG_title, s_name);

                mArrayList_c.add(hashMap);
            }
            ListAdapter adapter = new SimpleAdapter(
                    Information.this, mArrayList_c, R.layout.list_test,
                    new String[]{TAG_title},
                    new int[]{R.id.textView_list_title}
            );
            mlistViewtitle.setAdapter(adapter);
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }


        //x좌표 찍기
        try {
            JSONObject jsonObject = new JSONObject(mJsonString_a);
            JSONArray jsonArray2 = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray2.length(); i++) {

                JSONObject item = jsonArray2.getJSONObject(i);

                String content = item.getString(TAG_content);

                HashMap<String, String> hashMap1 = new HashMap<>();

                hashMap1.put(TAG_content, content);

                mArrayList_a.add(hashMap1);
            }
            ListAdapter adapter = new SimpleAdapter(
                    Information.this, mArrayList_a, R.layout.list_test2,
                    new String[]{TAG_content},
                    new int[]{R.id.textView_list_content}
            );
            mlistViewcontent.setAdapter(adapter);
        } catch (JSONException e) {
         Log.d(TAG, "showResult : ", e);
        }


    }
}
