package com.example.buyer_map;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Information2 extends AppCompatActivity {
    private static String TAG = "phpquerytest";

    //이전 화면에서 textview내용 받아오기
    TextView textinfo;      //inform2 텍뷰 이름:textinfo //게시글제목
    TextView textinfo2;
    TextView textinfo3;
    TextView textinfo4;
    TextView textinfo5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information2);



        Intent intent3 = getIntent();
        //1
        String selected_item2 = intent3.getStringExtra("s_name");

        textinfo = (TextView) findViewById(R.id.textinfo);
        textinfo.setText(selected_item2);

        //2
        String selected_item3 = intent3.getStringExtra("s_name2");

        textinfo2 = (TextView) findViewById(R.id.textinfo2);
        textinfo2.setText(selected_item3);

        //3
        String selected_item4 = intent3.getStringExtra("s_name3");

        textinfo3 = (TextView) findViewById(R.id.textinfo3);
        textinfo3.setText(selected_item4);

        //4
        String selected_item5 = intent3.getStringExtra("s_name4");

        textinfo4 = (TextView) findViewById(R.id.textinfo4);
        textinfo4.setText(selected_item5);

        //5 password
        String selected_item6 = intent3.getStringExtra("s_name5");

        textinfo5 = (TextView) findViewById(R.id.textinfo5);
        textinfo5.setText(selected_item6);

        this.SetListener();
    }



    public void SetListener() {
// 버튼누르고 화면전환 / 게시글삭제됬다는거 알려주기
        Button btndelete;
        btndelete = (Button) findViewById(R.id.btndelete);
        btndelete.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             //edittext에 입력받은 비밀번호 string으로 받기
                                             EditText inputpw = (EditText) findViewById(R.id.inputpw);
                                             inputpw.getText().toString();

                                             TextView textinfo5 = (TextView) findViewById(R.id.textinfo5);
                                             textinfo5.getText().toString();
                                             textinfo.getText().toString();

                                             if (inputpw.getText().toString().length() == 0) {
                                                 //공백일 때 처리할 내용
                                                 Toast.makeText(getApplicationContext(), "내용을 입력해주세요 ", Toast.LENGTH_LONG).show();

                                             } else {
                                                 //비밀번호 일치하면 내용 삭제
                                                 if (inputpw.getText().toString().equals(textinfo5.getText().toString())) {

                                                     GetData task = new GetData();
                                                     task.execute(inputpw.getText().toString(), textinfo.getText().toString());
                                                     Toast.makeText(getApplicationContext(), "게시글을 삭제합니다 ", Toast.LENGTH_LONG).show();

                                                 } else {
                                                     Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다 ", Toast.LENGTH_LONG).show();
                                                 }
                                             }
                                         }
                                     }

        );
    }

    public class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Information2.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
/*             Log.d(TAG, "response - " + result);
           mJsonString1 = result;
            showResult();*/
        }


        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];

            String serverURL = "http://ec2-3-134-104-28.us-east-2.compute.amazonaws.com/delete2.php";
            String postParameters = "pw=" + searchKeyword1 +"&s_name=" + searchKeyword2;

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
                //   Log.d(TAG, "response code - " + responseStatusCode);

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

                //     Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }

    }





}




