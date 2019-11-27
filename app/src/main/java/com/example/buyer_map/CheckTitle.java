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

    String mJsonString;
    ListView sListViewList;                         //위에 값을 담을 리스트뷰

//    String[] s_list;  //결과나온 sArraylist를 string 배열로 풀어주려고

    String[] arraylist_addr;    //이전 화면에서 받아온 게시글제목 배열

    private TextView selected_sname;    //제목을 선택하면 그대로 보여줄 텍스트뷰

    ArrayList<HashMap<String, String>> sArrayList;  //db결과값 담을 arraylist


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_title);

        // final TextView textview79 = (TextView) findViewById(R.id.textview79);


        //db 실행후에 select 값이 보여질 listview
        sListViewList = (ListView) findViewById(R.id.listView_main_list);
        selected_sname = (TextView) findViewById(R.id.selected_sname);

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

                //클릭한 게시물 제목을 텍스트뷰에 출력
                selected_sname.setText("");

                //다음 화면에 텍스트뷰 그대로 넘기기
//                Intent intent3 = new Intent(getApplicationContext(), Information2.class);
//                intent3.putExtra("addressFromMain", position);
//                intent3.putExtra("s_name", selected_item2);
//                intent3.putExtra("name",selected_sname.getText().toString());
//                startActivity(intent3);



                //리스트뷰 클릭하면 벌어지는 일
                sArrayList.clear();

                GetData task = new GetData();
//                    task.execute( selected_sname.getText().toString());
                task.execute(selected_item2);






            }
        });

        sArrayList = new ArrayList<>();

/*        //db 실행후에 select 값이 보여질 listview
        sListViewList = (ListView) findViewById(R.id.listView_main_list);
        selected_sname = (TextView) findViewById(R.id.ccc);

        Button button_search = (Button) findViewById(R.id.button_main_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                sArrayList.clear();

                GetData task = new GetData();
                task.execute( selected_sname.getText().toString());
            }
        });
        sArrayList = new ArrayList<>();*/

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
            showResult();
            selected_sname = (TextView) findViewById(R.id.selected_sname);
            //  TextView textview79 = (TextView) findViewById(R.id.textview79);
            Intent intent3 = new Intent(getApplicationContext(), Information2.class);
//            intent3.putExtra("addressFromMain", position);
            intent3.putExtra("s_name", selected_sname.getText().toString());
            intent3.putExtra("name",selected_sname.getText().toString());
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

            selected_sname = (TextView) findViewById(R.id.selected_sname);
            //  TextView textview79 = (TextView) findViewById(R.id.textview79);

            try {
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for(int i=0;i<jsonArray.length();i++){

                    JSONObject item = jsonArray.getJSONObject(i);
                    String s_name = item.getString(TAG_SNAME);
                    String c_name = item.getString(TAG_CNAME);
                    String f_address = item.getString(TAG_FADDRESS);
                    String content = item.getString(TAG_CONTENT);

                    HashMap<String,String> hashMap = new HashMap<>();

                    hashMap.put(TAG_SNAME, s_name);
                    hashMap.put(TAG_CNAME, c_name);
                    hashMap.put(TAG_FADDRESS, f_address);
                    hashMap.put(TAG_CONTENT, content);

                    sArrayList.add(hashMap);
                }

                for(int i=0;i<jsonArray.length();i++){

                    HashMap<String, String> outputHashMap = sArrayList.get(i);
                    String s_name = outputHashMap.get("s_name");
                    String c_name = outputHashMap.get("c_name");
                    String f_address = outputHashMap.get("f_address");
                    String content = outputHashMap.get("content");

                    String str = String.format(getResources()

                            .getString(R.string.textview_message), s_name, c_name, f_address, content);
                    selected_sname.append(str);
                }



/*            ListAdapter adapter = new SimpleAdapter(
                    CheckTitle.this, sArrayList, R.layout.item_list,
                    new String[]{TAG_CONTENT,
                            TAG_CNAME,TAG_SNAME,TAG_FADDRESS},
                    new int[]{R.id.textView_list_sname, R.id.textView_list_cname
                            , R.id.textView_list_faddress, R.id.textView_list_content}
            );

            sListViewList.setAdapter(adapter);*/

            } catch (JSONException e) {

                Log.d(TAG, "showResult : ", e);
            }


/*        String sum2 = "";
        for (HashMap<String, String> hash : sArrayList) {
            for (String current : hash.values()) {
                sum2 = sum2 + current + "<#>";
            }
        }
        s_list = sum2.split("<#>");

        for (int k = 0; k < s_list.length; k++) {

            Log.d("@@@@@@@@sname@@@@@@", s_list[k]);
        }*/



        }

    }



}








//        // string[] 다시 arraylist로 변환
//        final ArrayList<String> selectarr = new ArrayList<String>();
//        Collections.addAll(selectarr,arraylist_addr);
//
//
//        selectarr.add(TAG_SNAME);