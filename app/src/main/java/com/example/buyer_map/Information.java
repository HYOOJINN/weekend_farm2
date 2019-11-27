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
import android.widget.Toast;

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
        Toast.makeText(getApplicationContext(), "판매 정보가 정상적으로 입력되었습니다", Toast.LENGTH_LONG).show();
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


                String serverURL = "http://ec2-3-134-104-28.us-east-2.compute.amazonaws.com/queryforinfo2.php";
                String postParameters = "c_name=" + searchKeyword1 + "&f_address" + searchKeyword2;
//                String postParameters = "c_name=" + searchKeyword1;

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
                Log.v("eeeeee",mJsonString);
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


            try {
                JSONObject jsonObject = new JSONObject (mJsonString1);
                Log.v("eeeee3333333e",mJsonString1);
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









//public class Information extends AppCompatActivity {
//
//
//    private TextView mTextViewaddress;
//    private TextView mTextViewcrop;
//    private String receive_address;
//    private String receive_crop;
//    private TextView textView_title;
//    private TextView textView_content;
//    phpdo task;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_information);
//
//        //        ///////주소, 작물 값 읽어와서 저장하기
//        Intent intent3 = getIntent();
//        receive_address = intent3.getStringExtra("addressFromMain");
//        mTextViewaddress = (TextView) findViewById(R.id.textView_address);
//        mTextViewaddress.setText(receive_address);
//
//        receive_crop = intent3.getStringExtra("cropFromMain");
//        mTextViewcrop = (TextView) findViewById(R.id.textView_crop);
//        mTextViewcrop.setText(receive_crop);
//
//        String f_address = receive_address;
//        String c_name = receive_crop;
//
//
//        textView_title = (TextView) findViewById(R.id.textView_content);/////////////////
////        task = new phpdo();
////        task.execute(mTextViewcrop.getText().toString(), mTextViewaddress.getText().toString());
//
//
//        Button button_search = (Button) findViewById(R.id.button_main_search2);
//        button_search.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                task = new phpdo();
//                task.execute(mTextViewcrop.getText().toString(), mTextViewaddress.getText().toString());
//            }
//        });
//
//
//    }
//
//    private class phpdo extends AsyncTask<String, Void, String> {
//
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//
//        @Override
//        protected String doInBackground(String... arg0) {
//
//
//            try {
//                String c_name = arg0[0];
//                String f_address = arg0[1];
//
////                String serverURL = "http://ec2-3-134-104-28.us-east-2.compute.amazonaws.com/queryforinfo3.php";
////                String postParameters = "c_name=" + c_name+ "&f_address="+f_address;
//
//                String link = "http://ec2-3-134-104-28.us-east-2.compute.amazonaws.com/queryforinfo3.php?c_name="
//                        + URLEncoder.encode(c_name,"UTF-8") + "&f_address=" + URLEncoder.encode(f_address, "UTF-8");
//
//                URL url = new URL(link);
//                HttpClient client = new DefaultHttpClient();
//                HttpGet request = new HttpGet();
//                request.setURI(new URI(link));
//                HttpResponse response = client.execute(request);
//                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//
//                StringBuffer sb = new StringBuffer("");
//                String line = "";
//
//                while ((line = in.readLine()) != null) {
//                    sb.append(line);
//                    break;
//                }
//                in.close();
//                return sb.toString();
//            } catch (Exception e) {
//                return new String("Exception: " + e.getMessage());
//            }
//
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            super.onPostExecute(result);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
//            try {
//                textView_title.setText(result);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            //txtview.setText("Login Successful");
//            //textView_title.setText(result);
//        }
//    }
//}





//public class Information extends AppCompatActivity {
//
//    TextView mTextViewaddress;
//    TextView mTextViewcrop;
//    String receive_address;
//    String receive_crop;
//
//    ListView listView;
//
//        public void btnOkay2(View v) {
//        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//        startActivity(intent);
//        Toast.makeText(getApplicationContext(), "판매 정보가 정상적으로 입력되었습니다", Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_information);
//
//        ///////주소, 작물 값 읽어와서 저장하기기
//       Intent intent3 = getIntent();
//        receive_address = intent3.getStringExtra("addressFromMain");
//        mTextViewaddress = (TextView)findViewById(R.id.textView_address);
//        mTextViewaddress.setText(receive_address);
//
//        receive_crop = intent3.getStringExtra("cropFromMain");
//        mTextViewcrop = (TextView) findViewById(R.id.textView_crop);
//        mTextViewcrop.setText(receive_crop);
//
//        listView = (ListView) findViewById(R.id.textView_content);
//        getJSON( "http://ec2-3-134-104-28.us-east-2.compute.amazonaws.com/queryforinfo3.php?c_name="+receive_crop+"&f_address="+receive_address);
//    }
//
//
//    private void getJSON(final String urlWebService) {
//
//        class GetJSON extends AsyncTask<Void, Void, String> {
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
//                try {
//                    loadIntoListView(s);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected String doInBackground(Void... voids) {
//                try {
//                    URL url = new URL(urlWebService);
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                    StringBuilder sb = new StringBuilder();
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                    String json;
//                    while ((json = bufferedReader.readLine()) != null) {
//                        sb.append(json + "\n");
//                    }
//                    return sb.toString().trim();
//                } catch (Exception e) {
//                    return null;
//                }
//            }
//        }
//        GetJSON getJSON = new GetJSON();
//        getJSON.execute();
//    }
//
//    private void loadIntoListView(String json) throws JSONException {
//        JSONArray jsonArray = new JSONArray(json);
//        String[] heroes = new String[jsonArray.length()];
//        for (int i = 0; i < jsonArray.length(); i++) {
//            JSONObject obj = jsonArray.getJSONObject(i);
//            heroes[i] = obj.getString("s_name");
//        }
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, heroes);
//        listView.setAdapter(arrayAdapter);
//    }
//}




