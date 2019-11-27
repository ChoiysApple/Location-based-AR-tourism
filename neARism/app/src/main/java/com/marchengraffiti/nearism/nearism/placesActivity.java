package com.marchengraffiti.nearism.nearism;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.marchengraffiti.nearism.nearism.firebase.FirebaseRead;
import com.marchengraffiti.nearism.nearism.firebase.MyCallback;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;

public class placesActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;

    String[] mapValue;
    String placeTitle, addr1, firstimage;

    ImageView mainImage;
    Bitmap bitmap;
    TextView title, address;
    ImageButton back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_detail);

        mainImage = findViewById(R.id.main_photo);
        title = findViewById(R.id.title);
        address = findViewById(R.id.place_addr);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new task().execute();

        // Google Map API Fragment
        FragmentManager fragmentManager = getFragmentManager();
        final MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.800844, 128.141912), 14));
    }

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
                    Log.d("placeDB", value);
                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values){
            mapValue = values[0].split(",");
            placeTitle = mapValue[2];
            addr1 = mapValue[3];
            firstimage = mapValue[4];

            final Intent intent = getIntent();

            double lat, lng;
            lat = Double.valueOf(intent.getStringExtra("lat"));
            lng = Double.valueOf(intent.getStringExtra("lng"));

            //if(cid.equals(intent.getStringExtra("cid"))) {
            if(placeTitle.equals(intent.getStringExtra("title"))) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .title(intent.getStringExtra("title")));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 14));

                title.setText(placeTitle);
                address.setText(addr1);

                Thread mThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(firstimage);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true);
                            conn.connect();

                            InputStream is = conn.getInputStream();
                            bitmap = BitmapFactory.decodeStream(is);
                        } catch (IOException ex) {

                        }
                    }
                };

                mThread.start();

                try {
                    mThread.join();
                    mainImage.setImageBitmap(bitmap);
                } catch (InterruptedException e) {

                }
            }

            //mMap.addMarker(markerOptions);
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

}
