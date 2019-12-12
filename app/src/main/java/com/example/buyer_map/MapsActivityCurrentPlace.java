package com.example.buyer_map;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

// 이용자의 현재 위치와 농장 위치를 마커로 구현한 Activity
public class MapsActivityCurrentPlace extends AppCompatActivity
        implements OnMapReadyCallback {

    private static final String TAG = MapsActivityCurrentPlace.class.getSimpleName();
    private GoogleMap mMap; // 화면이동, 마커 달기 등등으로 사용되는 구글맵 변수
    private CameraPosition mCameraPosition;  //  좌표이동 + 3d 효과 주는 변수

    private FusedLocationProviderClient mFusedLocationProviderClient; // 주변 정보를 얻는 변수

    // 인터넷 연결이 안됬을 때, default location이 시작점이 됨.
   private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 10; // 지도 줌의 정도
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1; //위치정보 사용에 대한 동의 상수로 requestCode랑 비교해서 같으면 ok
    private boolean mLocationPermissionGranted; // 위치정보 사용 동의시 true

    private Location mLastKnownLocation; // 현재 위치의 loction 정보를 담는 변수

   // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;

    // Buyer에서 받아온 x,y, 농장 이름, 농장 주소 값을 배열 형태로 저장
    String[] arraylist_x;
    String[] arraylist_y;
    String[] arraylist_farmName;
    String[] arraylist_address;

    String selected_item;
    String receive_address;
    // String형태의 x,y 좌표를 double형으로 변환하여 배열 형태로 저장
   double[] double_arrX;
    double[] double_arrY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        //activity_maps_current_place.XML의 fragment layout(activity_maps_current_place)에 지도 출력
        setContentView(R.layout.activity_maps_current_place);

        //SupportMapFragment을 통해 레이아웃에 만든 fragment의 id를 참조하고 구글맵을 호출한다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); // onMapReady()가 자동 호출됨

        Intent intent4 = getIntent();
        receive_address = intent4.getStringExtra("cropFromBuyer");

        //buyer에서 받아온 주소 listview에 출력하기
        ListView address_listview = (ListView) findViewById(R.id.mark_address_list);
        Intent intent2 = getIntent();
        arraylist_address = intent2.getExtras().getStringArray("arr_address");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, arraylist_address);
        address_listview.setAdapter(adapter);


        //리스트뷰의 아이템을 클릭시 해당 아이템의 문자열을 가져오기 위한 처리
        address_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

                selected_item = (String)adapterView.getItemAtPosition(position);

                //선택한 주소 정보를 팝업창에 저장하기
                //주소 목록 선택 시, information으로 전환하기 위한 팝업창을 화면에 출력
                androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage(selected_item);

                builder.setPositiveButton("정보 확인", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id){

                        Intent resultIntent2 = new Intent(getApplicationContext(), Information.class);
                        resultIntent2.putExtra("addressFromMain", selected_item);
                        resultIntent2.putExtra("cropFromMain", receive_address);
                        startActivity(resultIntent2);
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                    }
                });
                builder.show();
            }
        });


        Intent intent = getIntent();
        arraylist_x = intent.getExtras().getStringArray("arr_x"); // x좌표 배열로 받아오기
        arraylist_y = intent.getExtras().getStringArray("arr_y"); //y좌표 배열로 받아오기
        arraylist_farmName = intent.getExtras().getStringArray("arr_farmName"); // 농장이름 배열로 받아오기
        arraylist_address = intent.getExtras().getStringArray("arr_address"); // 농장주소 배열로 받아오기

        // String형의 x 좌표를 double형으로 강제형변환
        double_arrX = new double[arraylist_x.length];
        for(int i = 0; i<arraylist_x.length; i++){
            double_arrX[i] = Double.parseDouble(arraylist_x[i]);
        }
        // String형의 y 좌표를 double형으로 강제형변환
        double_arrY = new double[arraylist_y.length];
        for(int i = 0; i<arraylist_y.length; i++){
            double_arrY[i] = Double.parseDouble(arraylist_y[i]);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        final MarkerOptions farm_marker = new  MarkerOptions();

        for(int i = 0; i<double_arrX.length; i++) {
            farm_marker.position(new LatLng(double_arrX[i], double_arrY[i]));
            farm_marker.title(arraylist_farmName[i]);
            farm_marker.snippet(arraylist_address[i]);

            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker1);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false); // 이미지마커 등록
            farm_marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            mMap.addMarker(farm_marker);
            //마커 클릭 리스너
            this.mMap.setOnMarkerClickListener(markerClickListener);
            //정보창 클릭 리스너
            mMap.setOnInfoWindowClickListener(infoWindowClickListener);
        }

        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
    }

    //마커 클릭 리스너
    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            LatLng location = marker.getPosition();

            //마커 클릭시 지도 가운데로 이동시킨 후 ZOOM
            CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
            mMap.animateCamera(center);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.latitude, location.longitude), 15));
            return false;
        }
    };
    //정보창 클릭 리스너
    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
        }
    };

    //현위치 받아오기
    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // 지도의 기본 위치를 현위치로 설정한다.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                            BitmapDrawable imageMarker = (BitmapDrawable) getResources().getDrawable(R.drawable.current);
                            Bitmap bit = imageMarker.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(bit, 100, 100, false); // 이미지마커 등록

                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()))
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    //GPS를 사용하기 위한 권한 설정
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    //위치 권한 설정 여부에 따라 구글맵에 디바이스의 위치를 설정
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}
