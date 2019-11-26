package com.example.buyer_map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class Information extends AppCompatActivity {

    private TextView mTextViewaddress;
    private TextView mTextViewcrop;
    TextView mTextViewtitle; //textView_title
    TextView mTextViewcontnet; //textView_content
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON = "webnautes";
    private static final String TAG_title = "title";
    private static final String TAG_content = "content";
    ArrayList<HashMap<String, String>> mArrayList;
    ListView mListViewList;
    String mJsonString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

    ///////주소, 작물 값 읽어와서 저장하기기
       Intent intent3 = getIntent();
        String receive_address = intent3.getStringExtra("addressFromMain");
        mTextViewaddress = (TextView) findViewById(R.id.textView_address);
        mTextViewaddress.setText(receive_address);

        String receive_crop = intent3.getStringExtra("cropFromMain");
        mTextViewcrop = (TextView) findViewById(R.id.textView_crop);
        mTextViewcrop.setText(receive_crop);

        mTextViewtitle = (TextView) findViewById(R.id.textView_title);
        mTextViewcontnet= (TextView) findViewById(R.id.textView_content);


        GetData2 task = new GetData2();
        task.execute(receive_crop ,receive_address);
    }

    private class GetData2 extends AsyncTask<String, Void, String>{

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
           // mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null){
               // mTextViewResult.setText(errorString);
            }
            else {
                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String receive_crop = params[0];
            String receive_address = params[1];

            String serverURL = "http://서버IP/query.php";
            String postParameters = "c_name=" + receive_crop + "&f_address=" + receive_address;


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



    private void showResult(){

        String s_name;
        String content;
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                s_name = item.getString(TAG_title);
                content = item.getString(TAG_content);

                mTextViewtitle.setText(s_name);
                mTextViewcontnet.setText(content);

            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }





    }






    public void btnOkay(View v) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "판매 정보가 정상적으로 입력되었습니다", Toast.LENGTH_LONG).show();
    }

}