package com.marchengraffiti.nearism.nearism.course;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.marchengraffiti.nearism.nearism.R;
import com.marchengraffiti.nearism.nearism.firebase.CourseData;
import com.marchengraffiti.nearism.nearism.firebase.CourseRead;
import com.marchengraffiti.nearism.nearism.firebase.MyCallback;
import com.marchengraffiti.nearism.nearism.parsing.ParsingAPI;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CourseDetailActivity extends AppCompatActivity {

    Double lat, lng;
    Geocoder geoCoder;
    String subnum, detailimg, subname, overview, cid, subid;

    ImageButton back;
    Button startBtn;

    private ArrayList<CourseDetailItem> data = new ArrayList<>();

    double[] latList = new double[10];
    double[] lngList = new double[10];
    String[] nameList = new String[10];

    int place_count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(CourseDetailActivity.this, CourseMarkerActivity.class);
                newIntent.putExtra("lngList",lngList);
                newIntent.putExtra("latList",latList);
                newIntent.putExtra("nameList", nameList);
                Log.d("putExtra", String.valueOf(nameList));
                startActivity(newIntent);
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final ListView listView = findViewById(R.id.detailList);
        TextView placeTitle = findViewById(R.id.title);
        final Intent intent = getIntent();

        placeTitle.setText(intent.getStringExtra("placeTitle"));

        CourseRead courseRead = new CourseRead();
        courseRead.ReadCourseList(new MyCallback() {
            @Override
            public void onCallback(String value) {
                String[] value_split = value.split("#");
                //subnum = value_split[0];      // 코스 장소 순서
                detailimg = value_split[1];     // 각 코스 장소마다 이미지
                subname = value_split[2];       // 코스 장소 이름
                Log.d("names", subname);
                overview = value_split[3];      // 코스 장소 상세 설명
                cid = value_split[4];           // 메인 코스랑 장소 매칭시키려고
                subid = value_split[5];
                Log.d("courseData2222", value);

                CourseDetailItem item = new CourseDetailItem(detailimg, subname, overview, cid, subid);


                if(cid.equals(intent.getStringExtra("cid"))) {
                    data.add(item);


                    geoCoder = new Geocoder(getApplicationContext());
                    List<Address> addressList = null;
                    try {
                        addressList = geoCoder.getFromLocationName(subname, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(addressList.size() != 0) {
                        Log.d("address", addressList.get(0).toString());

                        lat = addressList.get(0).getLatitude();
                        lng = addressList.get(0).getLongitude();

                        Log.d("geocoder", lat + " " + lng);
                        latList[place_count] = lat;
                        lngList[place_count] = lng;
                        nameList[place_count] = subname;
                        place_count++;
                        Log.d("List", Arrays.toString(latList) + " " + Arrays.toString(lngList));


                        DatabaseReference mPostReference2 = FirebaseDatabase.getInstance().getReference();
                        Map<String, Object> childUpdates = new HashMap<>();
                        String key = cid;
                        String courseKey = subid; //subid
                        String detailalt = "";

                        CourseData courseData = new CourseData(courseKey, detailalt, detailimg, overview, subname, subnum, key);
                        Map<String, Object> postValues = courseData.toMap();
                        postValues.put("latitude", lat.toString());
                        postValues.put("longitude", lng.toString());
                        //Log.d("geocoder", String.format("%s", postValues));

                        childUpdates.put("/course_list/" + key + "/" + courseKey, postValues);
                        mPostReference2.updateChildren(childUpdates);
                    }
                }

                CourseDetailAdapter adapter = new CourseDetailAdapter(getApplicationContext(), R.layout.course_detail_item, data);
                listView.setAdapter(adapter);
            }
        });
    }

}
