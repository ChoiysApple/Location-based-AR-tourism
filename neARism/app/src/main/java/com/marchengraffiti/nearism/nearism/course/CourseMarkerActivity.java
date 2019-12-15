package com.marchengraffiti.nearism.nearism.course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marchengraffiti.nearism.nearism.MainActivity;
import com.marchengraffiti.nearism.nearism.R;
import com.marchengraffiti.nearism.nearism.firebase.MyCallback;

import java.util.ArrayList;

public class CourseMarkerActivity extends AppCompatActivity implements OnMapReadyCallback {

    int count = 0;
    Button finishBtn;
    GoogleMap mMap;
    static ArrayList<String> arrayData = new ArrayList<String>();
    String subname, lat, lng;

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

        //Intent i = getIntent();
        //String count = i.getStringExtra("count");

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
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.450541, 127.129904), 14));
    }
}
