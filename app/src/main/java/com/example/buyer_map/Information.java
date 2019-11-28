package com.example.buyer_map;
// buyer가 확인하는 information


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;


public class Information extends AppCompatActivity {

    private TextView mTextViewaddress;
    private TextView mTextViewcrop;
    ListView mlistViewtitle; //textView_title
    ListView mlistViewcontnet; //textView_content
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON = "webnautes";
    private static final String TAG_title = "s_name";
    private static final String TAG_content = "content";

    ArrayList<HashMap<String, String>> mArrayList;
    ArrayList<HashMap<String, String>> mArrayList2;


    String mJsonString;
    String mJsonString1;

    String receive_address;
    String receive_crop;

    TextView mEditTextSearchKeyword1;
    TextView mEditTextSearchKeyword2;

    public void btnOkay2(View v) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }

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
        mlistViewcontnet= (ListView) findViewById(R.id.listView_content);

        mEditTextSearchKeyword1 = (TextView) findViewById(R.id.textView_crop);
        mEditTextSearchKeyword2 = (TextView) findViewById(R.id.textView_address);


        Button button_search = (Button) findViewById(R.id.button_main_search2);
        button_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                mArrayList.clear();
                mArrayList2.clear();

                GetData task = new GetData();
                task.execute(mEditTextSearchKeyword1.getText().toString(), mEditTextSearchKeyword2.getText().toString());
                Log.v("cccccccc",mEditTextSearchKeyword1.getText().toString());
                Log.v("aaaaaaaa",mEditTextSearchKeyword2.getText().toString());
            }
        });
            mArrayList = new ArrayList<>();
            mArrayList2 = new ArrayList<>();

    }

        public class GetData extends AsyncTask<String, Void, String> {

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

                mJsonString = result;
                mJsonString1 = result;
                showResult();

            }


            @Override
            protected String doInBackground(String... params) {

                String searchKeyword1 = params[0];
                String searchKeyword2 = params[1];


                String serverURL = "http://ec2-3-134-104-28.us-east-2.compute.amazonaws.com/queryforinfo.php";
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

                    Log.d(TAG, "InsertData: Error ", e);
                    errorString = e.toString();

                    return null;
                }
            }

        }

        private void showResult() {
            //주소찍기
            try {
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                Log.v("01121331565rrrrr", mJsonString);
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
        }

}






