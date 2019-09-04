package com.marchengraffiti.nearism.nearism;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    // firebase
    private DatabaseReference mPostReference;


    private DrawerLayout mDrawerLayout;

    // 구글 맵 참조변수 생성
    GoogleMap mMap;

    BufferedReader br = null, br2 = null;
    //double latitude, longitude;

    boolean initem = false, inaddr1 = false, incat3 = false, incontentid = false;
    boolean incontenttypeid = false, infirstimage = false, infirstimage2 = false, inmapx = false;
    boolean inmapy = false, inmlevel = false, intel = false, intitle = false;

    String addr1 = null, cat3 = null, contentid = null, contenttypeid = null;
    String firstimage = null, firstimage2 = null, mapx = null, mapy = null;
    String mlevel = null, tel = null, Title = null;

    //boolean initem2 = false, insubid = false, indetailalt = false, indetailimg = false;
    //boolean inoverview = false, insubname = false, insubnum = false;
    //String subid = null, detailalt = null, detailimg = null, overview = null, subname = null, subnum = null;

    URL url, url2;

    public static Intent getIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;

                    case R.id.arPhotoBooth:
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;

                    case R.id.browseCourse:
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;

                    case R.id.contentsHub:
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
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

        // 현재 위치 경도, 위도 가져오기
        /*final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
        }*/

        try {
            String urlstr = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList" +
                    "?ServiceKey=wR7PJI9NFm3wvvrIRBnVKZQWb7ULPrgXTWECQcSf%2F2Wk8TVbszAcAFmRQXrXm6aUecKp9k7ubTkyjAGGzVzi8A%3D%3D" +
                    "&contentTypeId=12&MobileOS=ETC&MobileApp=AppTest&numOfRows=50";
            // 트래픽 증설 후 numOfRows 수정해야 함

            url = new URL(urlstr);
            Log.d(" logging", "1");
            final HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
            Log.d(" logging", "2");

            urlconnection.setRequestMethod("GET");
            Log.d(" logging", "3");

            new Thread() {
                public void run() {
                    locationBasedData(urlconnection);
                }
            }.start();
        } catch (Exception e) {

        }
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

    public void locationBasedData(HttpURLConnection urlconnection) {
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

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            Log.d("Parsing", "parsing start");

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if (parser.getName().equals("addr1"))  //title 만나면 내용을 받을수 있게 하자
                            inaddr1 = true;
                        if (parser.getName().equals("cat3"))
                            incat3 = true;
                        if (parser.getName().equals("contentid"))
                            incontentid = true;
                        if (parser.getName().equals("contenttypeid"))
                            incontenttypeid = true;
                        if (parser.getName().equals("firstimage"))
                            infirstimage = true;
                        if (parser.getName().equals("firstimage2"))
                            infirstimage2 = true;
                        if (parser.getName().equals("mapx"))
                            inmapx = true;
                        if (parser.getName().equals("mapy"))
                            inmapy = true;
                        if (parser.getName().equals("mlevel"))
                            inmlevel = true;
                        if (parser.getName().equals("tel"))
                            intel = true;
                        if (parser.getName().equals("title"))
                            intitle = true;
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(inaddr1) {
                            addr1 = parser.getText();
                            inaddr1 = false;
                        }
                        if(incat3) {
                            cat3 = parser.getText();
                            incat3 = false;
                        }
                        if(incontentid) {
                            contentid = parser.getText();
                            incontentid = false;
                        }
                        if(incontenttypeid) {
                            contenttypeid = parser.getText();
                            //if (contenttypeid.equals("25"))
                            //    incourse = true;
                            incontenttypeid = false;
                        }
                        if(infirstimage) {
                            firstimage = parser.getText();
                            infirstimage = false;
                        }
                        if(infirstimage2) {
                            firstimage2 = parser.getText();
                            infirstimage2 = false;
                        }
                        if(inmapx) {
                            mapx= parser.getText();
                            inmapx = false;
                        }
                        if(inmapy) {
                            mapy = parser.getText();
                            inmapy = false;
                        }
                        if(inmlevel) {
                            mlevel = parser.getText();
                            inmlevel = false;
                        }
                        if(intel) {
                            tel = parser.getText();
                            intel = false;
                        }
                        if(intitle) {
                            Title = parser.getText();
                            intitle = false;
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            mPostReference = FirebaseDatabase.getInstance().getReference();
                            Map<String, Object> childUpdates = new HashMap<>();
                            String key = mPostReference.child("posts").push().getKey();

                            FirebasePost post = new FirebasePost(addr1, cat3, contentid, contenttypeid,
                                    firstimage, firstimage2, mapx, mapy, mlevel, tel, Title);
                            Map<String, Object> postValues = post.toMap();
                            Log.d("firebaseLog", String.format("%s",postValues));

                            childUpdates.put("/place_list/" + key, postValues);
                            mPostReference.updateChildren(childUpdates);

                            Log.d("ParsingResult", "\n" + "addr1 : " + addr1 + "\ncat3 : " + cat3 + "\ncontentid : " + contentid +
                                    "\ncontenttypeid : " + contenttypeid + "\nfirstimage : " + firstimage + "\nfirstimage2 : " + firstimage2 +
                                    "\nmapx : " + mapx + "\nmapy : " + mapy + "\nmlevel : " + mlevel + "\ntel : " + tel + "\ntitle : " + Title);
                            initem = false;
                        }

                        break;
                }
                parserEvent = parser.next();
            }

        } catch (Exception e) {
            Log.d("APITAG", e.toString());
        }
    }

    /*
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
    */

    // Google Map API
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Import Google Map object
        mMap = googleMap;

        for (int idx = 0; idx < 10; idx++) {
            // Set marker options
            MarkerOptions makerOptions = new MarkerOptions();
            makerOptions
                    .position(new LatLng(37.52487 + idx, 126.92723))
                    .title("마커" + idx); // 타이틀.

            // Add marker
            mMap.addMarker(makerOptions);
        }

        // Move camera to location
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.52487, 126.92723)));
    }

}