package com.marchengraffiti.nearism.nearism;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.marchengraffiti.nearism.nearism.course.CourseMainActivity;
import com.marchengraffiti.nearism.nearism.firebase.FirebaseRead;
import com.marchengraffiti.nearism.nearism.firebase.MyCallback;
import com.marchengraffiti.nearism.nearism.map.MarkerItem;
import com.marchengraffiti.nearism.nearism.parsing.FourSquare;
import com.marchengraffiti.nearism.nearism.parsing.ParsingAPI;
import com.marchengraffiti.nearism.nearism.place.placesActivity;
import com.marchengraffiti.nearism.nearism.tflite.ClassifierActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener {

    int flag = 0;
    FloatingActionButton locationFab1, fab2, locationFab3, fab3, fab1, currentFab, locationFab2;

    Marker marker, marker2, marker3, marker4;
    List<String> list = new ArrayList<String>();
    List<Double> latlist = new ArrayList<Double>();
    List<Double> lnglist = new ArrayList<Double>();
    List<MarkerItem> markerList = new ArrayList<MarkerItem>();

    boolean myLocationEnabled = false;

    GoogleMap mMap;
    double lati, longi, lat, lng;
    Geocoder geoCoder;

    private DrawerLayout mDrawerLayout;

    String[] mapValue, parsing_split; // x좌표, y좌표, 타이틀
    double latitude, longitude;
    String msg, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab3 = findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ArImageActivity.class);
                startActivity(i);
            }
        });

        fab2 = findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClassifierActivity.class);
                startActivity(intent);
            }
        });

        fab1 = findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CourseMainActivity.class);
                startActivity(i);
            }
        });

        locationFab1 = findViewById(R.id.locationFab1); // 음식점
        locationFab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                new parsing_task().execute();
            }
        });

        locationFab2 = findViewById(R.id.locationFab2); // 카페
        locationFab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 2;
                new parsing_task().execute();
            }
        });

        locationFab3 = findViewById(R.id.locationFab3); // 호텔
        locationFab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 3;
                new parsing_task().execute();
            }
        });

        /*currentFab = findViewById(R.id.fabCurrent); // 호텔
        currentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myLocationEnabled)
                    mMap.setMyLocationEnabled(true);
                else
                    mMap.setMyLocationEnabled(false);

            }
        });*/

        new task().execute();


        // Google Map API Fragment
        FragmentManager fragmentManager = getFragmentManager();
        final MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final ParsingAPI parsingAPI = new ParsingAPI();
        parsingAPI.connection();

    }

    @Override
    public void onMyLocationChange(Location location) {

    }


    private class parsing_task extends AsyncTask<Void, String, Void> {
        String query;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d("parsing_task", "flag : " + flag);
            if(flag == 1) { query = "restaurant"; }
            if(flag == 2) { query = "cafe"; }
            if(flag == 3) { query = "inn"; }

            FourSquare f = new FourSquare(query);
            f.fourSquareParsing(new MyCallback() {
                @Override
                public void onCallback(String value) {
                    publishProgress(value);
                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            parsing_split = values[0].split("#");
            lat = Double.valueOf(parsing_split[1]);
            lng = Double.valueOf(parsing_split[2]);
            name = parsing_split[0];
            Log.d("parsing_task", lat + " " + lng + " " + name);

            LatLng position = new LatLng(lat, lng);

            if(flag==1) {
                marker2 = mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                );
            }

            if(flag==2) {
                marker4 = mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                );
            }

            if(flag==3) {
                marker3 = mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                );
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

    private class task extends AsyncTask<Void, String, Void> implements GoogleMap.OnMarkerClickListener {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params){
            FirebaseRead firebaseRead = new FirebaseRead();
            firebaseRead.ReadDB(new MyCallback() {
                @Override
                public void onCallback(String value) {
                    publishProgress(value);
                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            mapValue = values[0].split(",");
            latitude = Double.valueOf(mapValue[0]);
            longitude = Double.valueOf(mapValue[1]);
            msg = mapValue[2];

            LatLng position = new LatLng(latitude, longitude);

            marker = mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(msg)
            );

            MarkerItem markerItem = new MarkerItem(latitude, longitude, msg);
            markerList.add(markerItem);
            if (markerList.size() == 56)
                autoComplete(markerList);
            // define marker click listener
            mMap.setOnMarkerClickListener(this);
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }

        // onMarkerClick
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (flag == 0) {
                Log.d("Marker", marker.getTitle());
                Intent intent = new Intent(getApplicationContext(), placesActivity.class);
                intent.putExtra("title", marker.getTitle());                        // send marker title to placeDetailActivity
                intent.putExtra("lat", marker.getPosition().latitude + "");
                intent.putExtra("lng", marker.getPosition().longitude + "");

                startActivity(intent);                                                     // display place details
            }
            return false;
        }
    }

    public void autoComplete(final List<MarkerItem> markerList) {

        for(int i=0; i<markerList.size(); i++) {
            list.add(markerList.get(i).getMsg());
            latlist.add(markerList.get(i).getLat());
            lnglist.add(markerList.get(i).getLng());
        }

        //Log.d("testList", "latlist : " + String.valueOf(latlist));
        //Log.d("testList", "lnglist : " + String.valueOf(lnglist));

        final AutoCompleteTextView autoCompleteTextView = findViewById(R.id.toolField);
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, list));

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                AutoCompleteTextView autoComplete = (AutoCompleteTextView)findViewById(R.id.toolField);

                // 열려있는 키패드 닫기
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(autoComplete.getWindowToken(), 0);

                for(int i=0; i<list.size(); i++) {
                    if(String.valueOf(autoComplete.getText()).equals(list.get(i))) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latlist.get(i), lnglist.get(i)), 18));
                    }
                }
            }
        });
    }

    // Google Map API
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Import Google Map object
        mMap = googleMap;

        /*String values = getLocation();
        String[] value;
        value = values.split(",");

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(value[0]), Double.valueOf(value[1])), 14));
        Log.d("onMapReady", Double.valueOf(value[0]) + "/" + Double.valueOf(value[1]));
        */

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.450541, 127.129904), 14));
    }

}