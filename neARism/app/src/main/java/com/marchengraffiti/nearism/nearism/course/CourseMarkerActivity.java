package com.marchengraffiti.nearism.nearism.course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marchengraffiti.nearism.nearism.MainActivity;
import com.marchengraffiti.nearism.nearism.R;
import com.marchengraffiti.nearism.nearism.firebase.FirebaseRead;
import com.marchengraffiti.nearism.nearism.firebase.MyCallback;
import com.marchengraffiti.nearism.nearism.map.MarkerItem;
import com.marchengraffiti.nearism.nearism.place.placesActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.android.gms.maps.model.JointType.BEVEL;

public class CourseMarkerActivity extends AppCompatActivity implements OnMapReadyCallback {

    int count = 0;
    Button finishBtn;
    GoogleMap mMap;
    static ArrayList<String> arrayData = new ArrayList<String>();
    String subname, lat, lng;

    double[] latList;
    double[] lngList;
    String[] nameList;
    ArrayList<LatLng> points = new ArrayList<LatLng>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_marker);

        finishBtn = findViewById(R.id.finishBtn);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseMarkerActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        MapFragment mapFragment1 = (MapFragment)getFragmentManager().findFragmentById(R.id.courseMap);
        mapFragment1.getMapAsync(this);





        Log.d("markerlog", "courseMarkerActivity");

        ReadCourse(new MyCallback() {
            @Override
            public void onCallback(String value) {
                Log.d("markerlog", value);
                String[] value_split = value.split("#");
                subname = value_split[0]; // 코스 장소 이름
                lat = value_split[1];
                lng = value_split[2];

                Log.d("markerlog", count + "");
                Log.d("markerlog", subname + " " + lat + " " + lng);
            }
        });



    }

    public void ReadCourse(final MyCallback myCallback) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("course_list");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot deeperSnapshot : snapshot.getChildren()) {
                        String key = snapshot.getKey();
                        CourseMarkerData get = deeperSnapshot.getValue(CourseMarkerData.class);
                        String[] info = {get.subname, get.latitude, get.longitude};
                        String result = info[0] + "#" + info[1] + "#" + info[2];
                        arrayData.add(result);
                        Log.d("Reference", result);
                        count = arrayData.size();
                        myCallback.onCallback(result);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    // Google Map API
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Intent i = getIntent();
        lngList = i.getDoubleArrayExtra("lngList");
        latList = i.getDoubleArrayExtra("latList");
        nameList = i.getStringArrayExtra("nameList");
        Log.d("List marker", Arrays.toString(latList) + " "+ Arrays.toString(lngList));

        points = (ArrayList<LatLng>) createLatlngList(latList, lngList);
        Log.d("List Created point", String.valueOf(points));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points.get(2), 10));

        drawPath(points, nameList, googleMap);
    }

    // Draw polyline
    private void drawPath(List<LatLng> points, String[] nameList, GoogleMap mMap){
        for(int i = 0 ; i < points.size()-1; i++) {
            PolylineOptions polylineOptions = new PolylineOptions().add(points.get(i), points.get(i+1)).width(5).color(Color.RED).jointType(BEVEL);
            Polyline line = mMap.addPolyline(polylineOptions);

            MarkerOptions markerOptions = new MarkerOptions().position(points.get(i)).title(nameList[i]);
            mMap.addMarker(markerOptions);
        }

        MarkerOptions markerOptions = new MarkerOptions().position(points.get(points.size()-1)).title(nameList[points.size()-1]);
        mMap.addMarker(markerOptions);


    }

    //change latlng arrays to latlng list
    private List<LatLng> createLatlngList(double[] latList, double[] lngList){

        List<LatLng> points = new ArrayList<LatLng>();
        int count = 0;

        while (true){
            if (latList[count] == 0 && lngList[count]==0)
                break;

            points.add(new LatLng(latList[count], lngList[count]));
            Log.d("List create points", String.valueOf(points.get(count)));
            count++;
        }

        Log.d("List points", String.valueOf(points));
        return points;
    }

    // onMarkerClick
    public boolean onMarkerClick(Marker marker) {

        return false;
    }





}


