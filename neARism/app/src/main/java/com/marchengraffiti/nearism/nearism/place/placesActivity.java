package com.marchengraffiti.nearism.nearism.place;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.marchengraffiti.nearism.nearism.R;
import com.marchengraffiti.nearism.nearism.firebase.FirebaseRead;
import com.marchengraffiti.nearism.nearism.firebase.MyCallback;
import com.marchengraffiti.nearism.nearism.map.PlaceMapFragment;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class placesActivity extends AppCompatActivity{

    WebView webView;
    WebSettings webSettings;

    String[] mapValue;
    String placeTitle, addr1, firstimage;

    ImageView mainImage;
    Bitmap bitmap;
    TextView title, address;
    ImageButton back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_detail);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.mapfrag, new PlaceMapFragment());
        fragmentTransaction.commit();

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

            String placeName;
            double lat, lng;
            lat = Double.valueOf(intent.getStringExtra("lat"));
            lng = Double.valueOf(intent.getStringExtra("lng"));
            placeName = intent.getStringExtra("title");

            if(placeTitle.equals(placeName)) {
                Log.d("webViewList", placeTitle);

                webView = findViewById(R.id.webview);
                webView.setWebViewClient(new WebViewClient());
                webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setSupportMultipleWindows(false);
                webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
                webSettings.setLoadWithOverviewMode(true);
                webSettings.setUseWideViewPort(true);
                webSettings.setSupportZoom(false);
                webSettings.setBuiltInZoomControls(false);
                webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
                webSettings.setDomStorageEnabled(true);
                //webView.loadUrl("https://www.google.com/search?q=" + placeTitle +
                //        "&source=lnms&tbm=isch");

                webView.loadUrl("https://www.instagram.com/explore/tags/" + placeTitle + "/");

                TMapView t = (TMapView) findViewById(R.id.place_tmap);
                t.setSKTMapApiKey("l7xxef96fe182e8243f489da89904f951211");

                Bitmap pin = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.redpin);

                TMapPoint point = new TMapPoint(lat, lng);
                TMapMarkerItem markerItem1 = new TMapMarkerItem();
                markerItem1.setIcon(pin);
                markerItem1.setTMapPoint(point);
                markerItem1.setName(placeName);
                //markerItem1.setEnableClustering(true);

                markerItem1.setCalloutTitle(placeName);
                markerItem1.setCanShowCallout(true);
                markerItem1.setAutoCalloutVisible(true);
                t.addMarkerItem(placeName, markerItem1);
                t.setCenterPoint(lng, lat, false);

                // text
                title.setText(placeTitle);
                address.setText(addr1);

                setMainImage();
            }

            //mMap.addMarker(markerOptions);
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

    public void setMainImage() {
        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL imgurl = new URL(firstimage);
                    HttpURLConnection connection = (HttpURLConnection) imgurl.openConnection();
                    connection.setDoInput(true);
                    connection.connect();

                    InputStream ist = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(ist);
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

}

