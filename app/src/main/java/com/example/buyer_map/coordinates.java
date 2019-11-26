//package com.example.buyer_map;
////구매자 버튼에서 연결되는 crop
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.SimpleAdapter;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//
//public class coordinates extends AppCompatActivity {
//
//    //작물 리스트
//    private ListView mList;
//    private String[] data = {"고구마", "감자", "깻잎", "상추", "무", "고추", "호박", "가지", "대파", "양파", "당근", "쑥갓",
//            "열무", "방울토마토", "부추", "옥수수", "치커리", "가지", "미나리"};
//    private TextView selected_crop2;
//
//    //선택한 작물 기반 주소 출력
//
//    private static String TAG = "phpquerytest";
//    private static final String TAG_JSON = "webnautes";
//    private static final String TAG_x = "x";
//    private static final String TAG_y = "y";
//
//    ArrayList<HashMap<String, String>> mArrayList2;
//    ArrayList<HashMap<String, String>> mArrayList3;
//    ListView mListViewList;
//    ListView mListViewList2;
//    TextView mEditTextSearchKeyword1;
//    String mJsonString;
//
//    ///////
//    String[] arr_X;
//    String[] arr_Y;
//
//    public void nextgo(View v) {
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.putExtra("arr_x", arr_X);
//        intent.putExtra("arr_y", arr_Y);
//        startActivity(intent);
//        Toast.makeText(getApplicationContext(), "다음화면 갑니다", Toast.LENGTH_LONG).show();
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_coordinates);
//
//
//        //작물 리스트에서 선택한 값 textview에 출력
//        this.getEditTextObject();
//
//        mList = (ListView) findViewById(R.id.crop_list);
//        selected_crop2 = (TextView) findViewById(R.id.selected_crop2);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                this, android.R.layout.simple_list_item_1, data);
//        mList.setAdapter(adapter);
//
//        //리스트뷰의 아이템을 클릭시 해당 아이템의 문자열을 가져오기 위한 처리
//        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView,
//                                    View view, int position, long id) {
//
//                //클릭한 아이템의 문자열을 가져옴
//                String selected_item2 = (String) adapterView.getItemAtPosition(position);
//
//                //텍스트뷰에 출력
//                selected_crop2.setText(selected_item2);
//            }
//        });
//
//
//        //선택한 작물의 주소 list 형식으로 불러오기
//
//        mListViewList = (ListView) findViewById(R.id.listView_main_list);
//        mListViewList2 = (ListView) findViewById(R.id.listView_main_list2);
//
//        mEditTextSearchKeyword1 = (TextView) findViewById(R.id.selected_crop2);
//
//        Button button_search = (Button) findViewById(R.id.button_main_search);
//        button_search.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                mArrayList2.clear();
//                mArrayList3.clear();
//
//                GetData task = new GetData();
//                GetData task2 = new GetData();
//                task.execute(mEditTextSearchKeyword1.getText().toString());
//                task2.execute(mEditTextSearchKeyword1.getText().toString());
//            }
//        });
//        mArrayList2 = new ArrayList<>();
//        mArrayList3 = new ArrayList<>();
//
//    }
//
//    //작물 리스트에서 선택한 값 textview에 출력
//    public void getEditTextObject() {
//        selected_crop2 = (TextView) findViewById(R.id.selected_crop2);
//    }
//
//    public class GetData extends AsyncTask<String, Void, String> {
//
//        ProgressDialog progressDialog;
//        String errorString = null;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            progressDialog = ProgressDialog.show(coordinates.this,
//                    "Please Wait", null, true, true);
//        }
//
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            progressDialog.dismiss();
//            Log.d(TAG, "response - " + result);
//
//            mJsonString = result;
//            showResult();
//        }
//
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            String searchKeyword1 = params[0];
//
//            String serverURL = "http://ec2-3-134-104-28.us-east-2.compute.amazonaws.com/query7.php";
//
//            String postParameters = "c_name=" + searchKeyword1;
//
//
//            try {
//
//                URL url = new URL(serverURL);
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//
//
//                httpURLConnection.setReadTimeout(5000);
//                httpURLConnection.setConnectTimeout(5000);
//                httpURLConnection.setRequestMethod("POST");
//                httpURLConnection.setDoInput(true);
//                httpURLConnection.connect();
//
//
//                OutputStream outputStream = httpURLConnection.getOutputStream();
//                outputStream.write(postParameters.getBytes("UTF-8"));
//                outputStream.flush();
//                outputStream.close();
//
//
//                int responseStatusCode = httpURLConnection.getResponseCode();
//                Log.d(TAG, "response code - " + responseStatusCode);
//
//                InputStream inputStream;
//                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
//                    inputStream = httpURLConnection.getInputStream();
//                } else {
//                    inputStream = httpURLConnection.getErrorStream();
//                }
//
//
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//                StringBuilder sb = new StringBuilder();
//                String line;
//
//                while ((line = bufferedReader.readLine()) != null) {
//                    sb.append(line);
//                }
//
//
//                bufferedReader.close();
//
//
//                return sb.toString().trim();
//
//
//            } catch (Exception e) {
//
//                Log.d(TAG, "InsertData: Error ", e);
//                errorString = e.toString();
//
//                return null;
//            }
//
//        }
//
//    }
//
//
//    private void showResult() {
//
//        //x좌표 찍기
//        try {
//            JSONObject jsonObject = new JSONObject(mJsonString);
//            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//
//                JSONObject item = jsonArray.getJSONObject(i);
//
//                String x = item.getString(TAG_x);
//
//                HashMap<String, String> hashMap = new HashMap<>();
//
//                hashMap.put(TAG_x, x);
//
//                mArrayList2.add(hashMap);
//            }
//
//            ListAdapter adapter = new SimpleAdapter(
//                    coordinates.this, mArrayList2, R.layout.list_xy,
//                    new String[]{TAG_x},
//                    new int[]{R.id.x_coordinates}
//            );
//
//
//            mListViewList.setAdapter(adapter);
//
//
//        } catch (JSONException e) {
//
//            Log.d(TAG, "showResult : ", e);
//        }
//
//
//        //y좌표 찍기
//        try {
//            JSONObject jsonObject = new JSONObject(mJsonString);
//            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//
//                JSONObject item = jsonArray.getJSONObject(i);
//
//                String y = item.getString(TAG_y);
//
//                HashMap<String, String> hashMap2 = new HashMap<>();
//
//                hashMap2.put(TAG_y, y);
//
//                mArrayList3.add(hashMap2);
//
//            }
//
//
//            ListAdapter adapter = new SimpleAdapter(
//                    coordinates.this, mArrayList3, R.layout.list_y,
//                    new String[]{TAG_y},
//                    new int[]{R.id.y_coordinates}
//            );
//            mListViewList2.setAdapter(adapter);
//
//        } catch (JSONException e) {
//
//            Log.d(TAG, "showResult : ", e);
//        }
//
//
//
//        ////////////////////////////////////////////////////////////////////
//        //X,Y좌표 String[]으로 받아오기//
//
//        String sum = "";
//        for (HashMap<String, String> hash : mArrayList2) {
//            for (String current : hash.values()) {
//                sum = sum + current + "<#>";
//            }
//        }
//        arr_X = sum.split("<#>");
//
//        for (int k = 0; k < arr_X.length; k++) {
//
//            Log.d("@@@@@@@@XXXXXX@@@@@@", arr_X[k]);
//        }
//
//
//        String sum2 = "";
//        for (HashMap<String, String> hash : mArrayList3) {
//            for (String current : hash.values()) {
//                sum2 = sum2 + current + "<#>";
//            }
//        }
//        arr_Y = sum2.split("<#>");
//
//        for (int  k= 0; k < arr_Y.length; k++) {
//
//            Log.d("@@@@@@@@YYYYYYYY@@@@@@", arr_Y[k]);
//        }
//
//
//    }
//}
//
//
//
//
//
