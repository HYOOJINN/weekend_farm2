package com.example.buyer_map;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class MapsActivityCurrentPlace extends AppCompatActivity
        implements OnMapReadyCallback {

    private static final String TAG = MapsActivityCurrentPlace.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 10;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;

    // x,y,address 값 받아오기
    String[] arraylist_x;
    String[] arraylist_y;
    String[] arraylist_farmName;

    //buyer에서 받아온 주소 listview에 출력하기
    String[] arraylist_address;
    String Distance;
    //ListView address_listview;
    String selected_item;
    String receive_address;

    double[] double_arrX;
    double[] double_arrY;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps_current_place);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Intent intent4 = getIntent();
        receive_address = intent4.getStringExtra("cropFromBuyer");

        ///////////////////////////////////////
        //buyer에서 받아온 주소 listview에 출력하기

        ListView address_listview = (ListView) findViewById(R.id.mark_address_list);
        Intent intent2 = getIntent();
        arraylist_address = intent2.getExtras().getStringArray("arr_address");
        for(int j = 0; j<  arraylist_address.length; j++){
            Log.d("###", arraylist_address[j]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, arraylist_address);
        address_listview.setAdapter(adapter);



        //리스트뷰의 아이템을 클릭시 해당 아이템의 문자열을 가져오기 위한 처리
        address_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

                selected_item = (String)adapterView.getItemAtPosition(position);

                androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage(selected_item);

                builder.setPositiveButton("정보 확인", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id){

                        Intent resultIntent2 = new Intent(getApplicationContext(), Information.class);
                        resultIntent2.putExtra("addressFromMain", selected_item);
                        resultIntent2.putExtra("cropFromMain", receive_address);
                        startActivity(resultIntent2);
                        Toast.makeText(getApplicationContext(), "OK Click", Toast.LENGTH_SHORT).show();

                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(getApplicationContext(), "Cancel Click", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }

        });


    }




    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;


        ////////////////////////////////주소 받아와서 마커찍기 //////////////////////////////////
        Intent intent = getIntent();
        arraylist_x = intent.getExtras().getStringArray("arr_x");
        for(int j = 0; j<  arraylist_x.length; j++){
            Log.d("###", arraylist_x[j]);
        }

        arraylist_y = intent.getExtras().getStringArray("arr_y");
        for(int k = 0; k<  arraylist_y.length; k++){
            Log.d("###", arraylist_y[k]);
        }

        arraylist_farmName = intent.getExtras().getStringArray("arr_farmName"); // 농장이름 배열로 받아오기
        arraylist_address = intent.getExtras().getStringArray("arr_address"); // 농장주소 배열로 받아오기


        double_arrX = new double[arraylist_x.length];

        for(int i = 0; i<arraylist_x.length; i++){
            double_arrX[i] = Double.parseDouble(arraylist_x[i]);
        }

        double_arrY = new double[arraylist_y.length];

        for(int i = 0; i<arraylist_y.length; i++){
            double_arrY[i] = Double.parseDouble(arraylist_y[i]);
        }

        //마커 모아서찍기
//        LatLngBounds.Builder latlngBuilder = new LatLngBounds.Builder();
//        LatLng sydney1 = new LatLng(37.4229, 126.6516);
//        LatLng sydney2 = new LatLng(37.9019,127.0565);
//        latlngBuilder.include(sydney1);
//        latlngBuilder.include(sydney2);
//        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBuilder.build(), 100));

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


//                Location location_me = new Location("me");
//                location_me.setLatitude(mLastKnownLocation.getLatitude());
//                location_me.setLatitude(mLastKnownLocation.getLongitude());
//
//                Location location_farm = new Location("farm");
//                location_farm.setLatitude(double_arrX[i]);
//                location_farm.setLatitude(double_arrY[i]);
//                Distance = Double.toString(location_me.distanceTo(location_farm));

                Log.v("C_______LAT", Double.toString(mLastKnownLocation.getLatitude()));
                Log.v("C_______LONG", Double.toString(mLastKnownLocation.getLongitude()));
                Log.v("F_______LAT", Double.toString(double_arrX[i]));
                Log.v("F_______LONG", Double.toString(double_arrY[i]));
                Log.v("____DI___", Distance);


        }

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }


    ///////////////////////////////////info랑 zoom 동시에 안됨///////////////////////////
    /////////////마커 클릭 리스너-> ZOOM
    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            LatLng location = marker.getPosition();

            CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
            mMap.animateCamera(center);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.latitude, location.longitude), 15));
            return true;
        }
    };


    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
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

                            Log.v("####Lat###", Double.toString(mLastKnownLocation.getLatitude()));
                            Log.v("####Long###", Double.toString(mLastKnownLocation.getLongitude()));
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


    //거리찍기
//    public double getDistance(LatLng LatLng1, LatLng LatLng2){
//        /////////거리찍기./////////
//        //double[] Distance;
//        Location location_me = new Location("me");
//        location_me.setLatitude(mLastKnownLocation.getLatitude());
//        location_me.setLatitude(mLastKnownLocation.getLongitude());
//
//        Location location_farm = new Location("farm");
//        for(int i = 0; i<double_arrX.length; i++){
//            location_farm.setLatitude(double_arrX[i]);
//            location_farm.setLatitude(double_arrY[i]);
//        }
//        for(int j = 0; j<double_arrX.length; j++){
//            Distance[j] = location_me.distanceTo()(location_farm;
//        }

//float distance = crntLocation.distanceTo(newLocation);  in meters
//        float distance =crntLocation.distanceTo(newLocation) / 1000; // in km
//    }




    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
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

    /**
     * Handles the result of the request for location permissions.
     */
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

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    private void showCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<PlaceLikelihoodBufferResponse> placeResult =
                    mPlaceDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener
                    (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

                                // Set the count, handling cases where less than 5 entries are returned.
                                int count;
                                if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
                                    count = likelyPlaces.getCount();
                                } else {
                                    count = M_MAX_ENTRIES;
                                }


                                // Release the place likelihood buffer, to avoid memory leaks.
                                likelyPlaces.release();

                                // Show a dialog offering the user the list of likely places, and add a
                                // marker at the selected place.
                                openPlacesDialog();

                            } else {
                                Log.e(TAG, "Exception: %s", task.getException());
                            }
                        }
                    });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    private void openPlacesDialog() {}

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
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
