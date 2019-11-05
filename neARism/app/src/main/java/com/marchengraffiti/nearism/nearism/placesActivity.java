package com.marchengraffiti.nearism.nearism;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class placesActivity extends AppCompatActivity {


    List<String> list = new ArrayList<String>();
    GoogleMap mMap;
    double lati, longi;

    String[] mapValue; // longitude, latitude, title
    double latitude, longitude;
    String msg;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_detail);

        /*
        FragmentManager fragmentManager = getFragmentManager();
        final MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        final ParsingAPI parsingAPI = new ParsingAPI();
        parsingAPI.connection();*/
    }
/*
    ImageView mainImage = findViewById(R.id.main_photo);
    TextView title = findViewById(R.id.title);
    TextView adress = findViewById(R.id.place_addr);


    private class task extends AsyncTask<Void, String, Void> {
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
        protected void onProgressUpdate(String... values){
            mapValue = values[0].split(",");
            latitude = Double.valueOf(mapValue[0]);
            longitude = Double.valueOf(mapValue[1]);
            msg = mapValue[2];

            LatLng position = new LatLng(latitude, longitude);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title(msg);
            markerOptions.position(position);

            list.add(msg);

            mMap.addMarker(markerOptions);
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }*/


}


