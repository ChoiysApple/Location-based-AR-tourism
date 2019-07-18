package com.marchengraffiti.nearism.nearism;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.OnMapReadyCallback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    BufferedReader br = null;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Google Map API Fragment
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // 현재 위치 경도, 위도 가져오기
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        } else {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
        }

        try {
            String urlstr = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList?ServiceKey=wR7PJI9NFm3wvvrIRBnVKZQWb7ULPrgXTWECQcSf%2F2Wk8TVbszAcAFmRQXrXm6aUecKp9k7ubTkyjAGGzVzi8A%3D%3D"
                    + "&mapX=" + latitude + "&mapY=" + longitude + "&radius=1000&listYN=Y"
                    + "&arrange=A&MobileOS=ETC&MobileApp=AppTest&_type=json";

            URL url = new URL(urlstr);
            Log.d(" logging", "1");
            final HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
            Log.d(" logging", "2");

            urlconnection.setRequestMethod("GET");
            Log.d(" logging", "3");


            new Thread() {
                public void run() {

                    try {
                        br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
                        String result = "";
                        String line;
                        while ((line = br.readLine()) != null) {
                            result = result + line + "\n";
                            Log.d("result log", result);
                        }
                        Log.d(" logging", "5");

                        Log.d("APITAG Result", result);
                    } catch (Exception e) {
                        Log.d("APITAG", e.toString());
                    }

                }
            }.start();

        } catch (Exception e) {

        }

    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

    // Google Map API
    @Override
    public void onMapReady(final GoogleMap map) {

        // 추가할 것 - 공공데이터포털에서 가져온 관광지들 위도, 경도로 지도에 마킹해야 함

        LatLng SEOUL = new LatLng(37.56, 126.97);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울"); // title
        markerOptions.snippet("한국의 수도"); // sub title
        map.addMarker(markerOptions);

        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL)); // 카메라를 지정한 경도, 위도로 이동
        map.animateCamera(CameraUpdateFactory.zoomTo(10)); // 지정한 단계로 카메라 줌을 조정, 숫자 커질수록 상세 지도
    }
}