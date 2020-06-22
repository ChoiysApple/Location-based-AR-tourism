package com.marchengraffiti.nearism.nearism.course;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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
import com.google.ar.core.Frame;
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
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.android.gms.maps.model.JointType.BEVEL;

public class CourseMarkerActivity extends AppCompatActivity {

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
        Intent i = getIntent();
        lngList = i.getDoubleArrayExtra("lngList");
        latList = i.getDoubleArrayExtra("latList");
        nameList = i.getStringArrayExtra("nameList");
        Log.d("List marker", Arrays.toString(latList) + " " + Arrays.toString(lngList));

        points = (ArrayList<LatLng>) createLatlngList(latList, lngList);
        Log.d("List Created point", String.valueOf(points));


        finishBtn = findViewById(R.id.finishBtn);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseMarkerActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
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

        Log.d("TestArray", Arrays.toString(lngList));
        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        TMapView t = new TMapView(this);
        t.setSKTMapApiKey("l7xxef96fe182e8243f489da89904f951211");
        linearLayoutTmap.addView(t);
        Bitmap pin = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.redpin);
        int l = latList.length;
        ArrayList<TMapPoint> alTMapPoint = new ArrayList<TMapPoint>();
        for (int j = 0; j < l; j++) {
            TMapMarkerItem markerItem1 = new TMapMarkerItem();
            markerItem1.setIcon(pin);
            TMapPoint point = new TMapPoint(latList[j], lngList[j]);
            Log.d("tmap markerlog", String.valueOf(latList[j]) + String.valueOf(lngList[j]) + j);
            markerItem1.setTMapPoint(point);
            t.addMarkerItem("marker" + j, markerItem1);

            alTMapPoint.add(new TMapPoint(latList[j], lngList[j]));
        }
        t.setCenterPoint(lngList[2],latList[2], true);
        TMapPolyLine tMapPolyLine = new TMapPolyLine();
        tMapPolyLine.setLineColor(Color.BLUE);
        tMapPolyLine.setLineWidth(2);
        for (int k = 0; k < alTMapPoint.size(); k++) {
            tMapPolyLine.addLinePoint(alTMapPoint.get(k));
        }
        t.addTMapPolyLine("Line1", tMapPolyLine);


    }

    public void ReadCourse(final MyCallback myCallback) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("course_list");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot deeperSnapshot : snapshot.getChildren()) {
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


    //change latlng arrays to latlng list
    private List<LatLng> createLatlngList(double[] latList, double[] lngList) {

        List<LatLng> points = new ArrayList<LatLng>();
        int count = 0;

        while (true) {
            if (latList[count] == 0 && lngList[count] == 0)
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


