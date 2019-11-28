package com.marchengraffiti.nearism.nearism;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.google.android.material.navigation.NavigationView;
import com.marchengraffiti.nearism.nearism.ar.HelloSceneformActivity;
import com.marchengraffiti.nearism.nearism.ar.LocationActivity;
import com.marchengraffiti.nearism.nearism.course.CourseMainActivity;
import com.marchengraffiti.nearism.nearism.firebase.FirebaseRead;
import com.marchengraffiti.nearism.nearism.firebase.MyCallback;
import com.marchengraffiti.nearism.nearism.map.MarkerItem;
import com.marchengraffiti.nearism.nearism.parsing.FourSquare;
import com.marchengraffiti.nearism.nearism.parsing.ParsingAPI;
import com.marchengraffiti.nearism.nearism.place.placesActivity;
import com.marchengraffiti.nearism.nearism.tflite.CameraActivity;
import com.marchengraffiti.nearism.nearism.tflite.ClassifierActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    int flag = 0;
    FloatingActionButton locationFab1, fab2, locationFab3;

    Marker marker;
    List<String> list = new ArrayList<String>();
    List<Double> latlist = new ArrayList<Double>();
    List<Double> lnglist = new ArrayList<Double>();
    List<MarkerItem> markerList = new ArrayList<MarkerItem>();

    GoogleMap mMap;
    double lati, longi, lat, lng;

    private DrawerLayout mDrawerLayout;

    String[] mapValue, parsing_split; // x좌표, y좌표, 타이틀
    double latitude, longitude;
    String msg, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab2 = findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(intent);
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

        locationFab3 = findViewById(R.id.locationFab3); // 호텔
        locationFab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 3;
                new parsing_task().execute();
            }
        });

        new task().execute();

        // [START] Drawable navigation
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.photoGuide:
                        Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.arPhotoBooth:
                        Intent intent2 = new Intent(MainActivity.this, HelloSceneformActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.browseCourse:
                        Intent intent3 = new Intent(getApplicationContext(), CourseMainActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.settings:
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;

                }

                return true;
            }
        });
        // [END] Drawable navigation

        // Google Map API Fragment
        FragmentManager fragmentManager = getFragmentManager();
        final MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final ParsingAPI parsingAPI = new ParsingAPI();
        parsingAPI.connection();
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
            if(flag == 2) { query = "inn"; }

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
                marker = mMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                );
            }
            if(flag==3) {
                marker = mMap.addMarker(new MarkerOptions()
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            //case R.id.action_settings:
            //    return true;
        }

        return super.onOptionsItemSelected(item);
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

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.800844, 128.141912), 14));
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

    public String getLocation() {
        // 현재 위치 경도, 위도 가져오기
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        } else {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longi = location.getLongitude();
            lati = location.getLatitude();

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
        }

        String value = lati + "," + longi;
        return value;
    }

}