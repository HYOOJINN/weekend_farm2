package com.example.buyer_map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Seller extends AppCompatActivity {

    private static String IP_ADDRESS = "ec2-3-134-104-28.us-east-2.compute.amazonaws.com";
    private static String TAG = "phptest";

    private EditText mEditTexttitle;
    private EditText mEditTextaddressinput;
    private EditText mEditTextcropinput;
    private EditText mEditTextcontent;
    private TextView mTextViewsqltext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        mEditTexttitle = (EditText)findViewById(R.id.title);
        mEditTextaddressinput = (EditText)findViewById(R.id.addressinput);
        mEditTextcropinput = (EditText)findViewById(R.id.cropinput);
        mEditTextcontent = (EditText)findViewById(R.id.content);
        mTextViewsqltext = (TextView)findViewById(R.id.sqltext);

        mTextViewsqltext.setMovementMethod(new ScrollingMovementMethod());

        Button buttonInsert = (Button)findViewById(R.id.btnComplete);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = mEditTexttitle.getText().toString();
                String addressinput = mEditTextaddressinput.getText().toString();
                String cropinput =  mEditTextcropinput.getText().toString();
                String content = mEditTextcontent.getText().toString();

                InsertData task = new InsertData();
                task.execute("http://" + "ec2-3-134-104-28.us-east-2.compute.amazonaws.com" + "/insert.php", title, addressinput, cropinput, content);


                mEditTexttitle.setText("");
                mEditTextcontent.setText("");

            }
        });

    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Seller.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewsqltext.setText(result);
            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {

            String title = (String)params[1];
            String addressinput = (String)params[2];
            String cropinput = (String)params[3];
            String content = (String)params[4];

            String serverURL = (String)params[0];
            String postParameters = "title=" + title + "&addressinput" +addressinput+"&cropinput"+cropinput+"&content=" + content;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

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
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }



    public void buttonAdd(View v){
        Intent intent=new Intent(getApplicationContext(),Farm.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"주소 검색 버튼을 눌렀습니다",Toast.LENGTH_LONG).show();
    }

    public void buttonCr(View v){
        Intent intent=new Intent(getApplicationContext(),Crop.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"작물 검색 버튼을 눌렀습니다",Toast.LENGTH_LONG).show();
    }
    public void btnHot(View v){
        Intent intent=new Intent(getApplicationContext(),hotItem.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"Hot 작물 확인 버튼을 눌렀습니다",Toast.LENGTH_LONG).show();
    }

    public void btnComplete(View v){
        Intent intent=new Intent(getApplicationContext(),Information.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"입력완료",Toast.LENGTH_LONG).show();


    }



}
