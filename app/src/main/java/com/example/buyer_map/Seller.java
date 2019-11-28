package com.example.buyer_map;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

class Code {
    public static int requestCode = 100;
    public static int resultCode = 1;
}

class Code2 {
    public static int requestCode2 = 200;
    public static int resultCode2 = 2;
}

public class Seller extends AppCompatActivity {

    private static String IP_ADDRESS = "ec2-3-134-104-28.us-east-2.compute.amazonaws.com";
    private static String TAG = "phptest";

    private EditText mEditTexttitle;
    private TextView mEditTextaddressinput; //intent
    private TextView mEditTextcropinput;
    private EditText mEditTextcontent;
    private TextView mTextViewsqltext;

    private EditText mEditTextpw;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        mEditTexttitle = findViewById(R.id.title);
        mEditTextaddressinput = (TextView)findViewById(R.id.addressInput);
        mEditTextcropinput = (TextView)findViewById(R.id.cropInput);
        mEditTextcontent = findViewById(R.id.content);
        mTextViewsqltext = findViewById(R.id.sqltext);
        mEditTextpw=(EditText)findViewById(R.id.pw);
        PasswordTransformationMethod pass=new PasswordTransformationMethod();
        mEditTextpw.setTransformationMethod(pass);

        mTextViewsqltext.setMovementMethod(new ScrollingMovementMethod());


        Button buttonInsert = findViewById(R.id.btnComplete);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = mEditTexttitle.getText().toString();
                String addressInput = mEditTextaddressinput.getText().toString();
                String cropInput =  mEditTextcropinput.getText().toString();
                String content = mEditTextcontent.getText().toString();
                String pw = mEditTextpw.getText().toString();

                InsertData task = new InsertData();
                task.execute("http://" + "ec2-3-134-104-28.us-east-2.compute.amazonaws.com" + "/insert.php", title, addressInput, cropInput, content,pw);


                mEditTexttitle.setText("");
                mEditTextaddressinput.setText("");
                mEditTextcropinput.setText("");
                mEditTextcontent.setText("");
                mEditTextpw.setText("");


                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("입력한 정보를 저장하시겠습니까?");

                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id){

                            Intent intent_h = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent_h);
                            Toast.makeText(getApplicationContext(),"입력완료",Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.show();

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

            String title = params[1];
            String addressInput = params[2];
            String cropInput = params[3];
            String content = params[4];
            String pw=params[5];

            String serverURL = params[0];
            String postParameters = "title=" + title +"&content=" + content+ "&addressInput=" +addressInput+"&cropInput="+cropInput
                    + "&pw=" + pw;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes(StandardCharsets.UTF_8));
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


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
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

                return "Error: " + e.getMessage();
            }

        }
    }



    public void buttonAdd(View v){
        Intent intent=new Intent(getApplicationContext(),Farm.class);
        startActivityForResult(intent, Code.requestCode);
        Toast.makeText(getApplicationContext(),"주소를 등록하세요",Toast.LENGTH_LONG).show();
    }

    public void buttonCr(View v){
        Intent intent2=new Intent(getApplicationContext(),Crop.class);
        startActivityForResult(intent2, Code2.requestCode2);
        Toast.makeText(getApplicationContext(),"농작물을 등록하세요",Toast.LENGTH_LONG).show();
    }
//    public void btnHot(View v){
//        Intent intent=new Intent(getApplicationContext(),hotItem.class);
//        startActivity(intent);
//        Toast.makeText(getApplicationContext(),"Hot 작물 확인 버튼을 눌렀습니다",Toast.LENGTH_LONG).show();
//    }



    @Override


    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {

        super.onActivityResult(requestCode, resultCode, resultIntent);

        if (requestCode == Code.requestCode && resultCode == Code.resultCode) {
            mEditTextaddressinput.setText(resultIntent.getStringExtra("address"));
            // mEditTextcropinput.setText(mEditTextcropinput.getText());
        }
        else if (requestCode == Code2.requestCode2 && resultCode == Code2.resultCode2) {
            // mEditTextaddressinput.setText(mEditTextaddressinput.getText());
            mEditTextcropinput.setText(resultIntent.getStringExtra("crop"));
        }
    }



}