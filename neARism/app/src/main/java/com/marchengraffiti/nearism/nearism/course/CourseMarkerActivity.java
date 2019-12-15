package com.marchengraffiti.nearism.nearism.course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
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
import com.marchengraffiti.nearism.nearism.firebase.MyCallback;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourseMarkerActivity extends AppCompatActivity implements OnMapReadyCallback {

    int count = 0;
    Button finishBtn;
    GoogleMap mMap;
    static ArrayList<String> arrayData = new ArrayList<String>();
    String subname, lat, lng;


    private LatLng startLatLng = new LatLng(0, 0);        //polyline Start
    private LatLng endLatLng = new LatLng(0, 0);          //polyline End

    double[] latList;
    double[] lngList;
    List<LatLng> points = new ArrayList<LatLng>();


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


        Intent i = getIntent();
        lngList = i.getDoubleArrayExtra("lngList");
        latList = i.getDoubleArrayExtra("latList");
        Log.d("List marker", Arrays.toString(latList) + " "+ Arrays.toString(lngList));
        List<LatLng> points = createLatlangList(latList, lngList);

        points = createLatlangList(latList, lngList);
        Log.d("List Created point", String.valueOf(points));

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

        drawPath(points);
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
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latList[0], lngList[0]), 14));
    }

    // Draw polyline
    private void drawPath(List<LatLng> points){

        for(int i = 0 ; i < points.size()-1; i++) {
            PolylineOptions options = new PolylineOptions().add(points.get(i), points.get(i+1)).width(5).color(Color.RED);
            Polyline line = mMap.addPolyline(options);

            mMap.addMarker(new MarkerOptions().position(points.get(i))
                    .title(Integer.toString(i)));
        }
    }

    //change latlng arrays to latlng list
    private List<LatLng> createLatlangList(double[] latList, double[] lngList){

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



}
