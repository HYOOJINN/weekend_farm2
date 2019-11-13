


package com.example.buyer_map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng farm1 = new LatLng(37.627603, 126.856881);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(farm1);
        markerOptions.title("은지주말농장");
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(farm1));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        LatLng farm2 = new LatLng(37.627980, 126.857814);
        markerOptions.position(farm2);
        markerOptions.title("숲속주말농장");
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(farm2));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));


        LatLng farm3 = new LatLng(37.612434, 126.854174);
        markerOptions.position(farm3);
        markerOptions.title("하나주말농장");
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(farm3));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));


        LatLng farm4 = new LatLng(37.622013, 126.849619);
        markerOptions.position(farm4);
        markerOptions.title("서정주말농장");
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(farm4));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        LatLng my = new LatLng(37.621103, 126.851774);
        markerOptions.position(my);
        markerOptions.title("내 위치");
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(my));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));


//test
        LatLng farm5 = new LatLng(37.621214, 126.849426);
        markerOptions.position(farm5);
        markerOptions.title("신지주말농장");
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(farm5));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
    public void ClickButton4map(View v){
        Intent intent=new Intent(getApplicationContext(),Information.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"information activity로 전환",Toast.LENGTH_LONG).show();
    }
}
