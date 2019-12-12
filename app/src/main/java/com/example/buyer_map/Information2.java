package com.example.buyer_map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
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

// 판매자가 등록한 글을 삭제하기 위해 게시물의 정보를 확인하기 위한 Activity
public class Information2 extends AppCompatActivity {
    private static String TAG = "phpquerytest";

    //CheckTitle에서 textview내용 받아오기
    TextView textinfo;
    TextView textinfo2;
    TextView textinfo3;
    TextView textinfo4;
    TextView textinfo5;
    EditText inputpw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information2);

        Intent intent3 = getIntent();
        //intent를 사용해 CheckTitle Activity에서 게시물에 입력된 모든 정보를 읽어와서 TextView에 저장하기
        String selected_item2 = intent3.getStringExtra("s_name");
        textinfo = (TextView) findViewById(R.id.textinfo);
        textinfo.setText(selected_item2);

        String selected_item3 = intent3.getStringExtra("s_name2");
        textinfo2 = (TextView) findViewById(R.id.textinfo2);
        textinfo2.setText(selected_item3);

        String selected_item4 = intent3.getStringExtra("s_name3");
        textinfo3 = (TextView) findViewById(R.id.textinfo3);
        textinfo3.setText(selected_item4);

        String selected_item5 = intent3.getStringExtra("s_name4");
        textinfo4 = (TextView) findViewById(R.id.textinfo4);
        textinfo4.setText(selected_item5);

        String selected_item6 = intent3.getStringExtra("s_name5");
        textinfo5 = (TextView) findViewById(R.id.textinfo5);
        textinfo5.setText(selected_item6);

        inputpw = (EditText) findViewById(R.id.inputpw);
        PasswordTransformationMethod pass=new PasswordTransformationMethod();
        inputpw.setTransformationMethod(pass);

        this.SetListener();
    }


    public void SetListener() {
        //판매 정보 삭제
        //Home으로 화면을 전환시키고 게시물을 삭제하는 버튼
        Button btndelete;
        btndelete = (Button) findViewById(R.id.btndelete);
        btndelete.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             //edittext에 입력받은 비밀번호 string으로 변환
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

                                                     Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                                     startActivity(intent);
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
        protected void onPreExecute() {//DB와 연결하는 동안의 시간동안 화면에 출력되는 로딩 화면
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Information2.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
        }

        //delete2.php를 통해 Android Studio와 DB를 연결하여 seller2에 저장된 정보를
        // pw(비밀번호)과 s_name(게시물 제목)를 기준으로 select해오기
        //try-catch문을 사용해 에러를 잡는다
        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];

            String serverURL = "http://ec2-3-14-72-47.us-east-2.compute.amazonaws.com/delete2.php";
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
                errorString = e.toString();
                return null;
            }
        }
    }
}



