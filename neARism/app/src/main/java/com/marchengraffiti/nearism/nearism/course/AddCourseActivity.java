package com.marchengraffiti.nearism.nearism.course;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.marchengraffiti.nearism.nearism.R;
import com.marchengraffiti.nearism.nearism.firebase.CourseData;
import com.marchengraffiti.nearism.nearism.firebase.FirebasePost;
import com.marchengraffiti.nearism.nearism.firebase.FirebaseRead;
import com.marchengraffiti.nearism.nearism.firebase.MyCallback;
import com.marchengraffiti.nearism.nearism.map.MarkerItem;
import com.marchengraffiti.nearism.nearism.place.PlaceItem;
import com.marchengraffiti.nearism.nearism.place.placesActivity;

import java.io.BufferedReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class AddCourseActivity extends AppCompatActivity {

    int idx = 0;
    Uri selectedImageUri;
    private DatabaseReference mPostReference, mPostReference2;

    String addr1 = null, cat3 = null, contentid = null, contenttypeid = null;
    String firstimage = null, firstimage2 = null, mapx = null, mapy = null;
    String mlevel = null, tel = null, placeTitle = null;

    String subid = null, detailalt = null, detailimg = null, overview = null, subname = null, subnum = null;

    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview, choose_course;
    private ImageButton back;

    String hour, minute, cost;
    Button addBtn, cancelBtn;
    EditText titleEditText;
    //EditText hourEditText, minuteEditText, costEditText;

    // listview
    String detailimg_list, overview_list, subname_list, cid;
    String firstimage_c, placeTitle_c, cid_c;

    static ArrayList<CourseDetailItem> data = new ArrayList<>();
    ListView listView;
    CourseDetailAdapter adapter;
    CourseDetailItem courseDetailItem;

    static int increase_num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        // course detail adapter로 바꾸기

        listView = findViewById(R.id.addCourseListView);
        addBtn = findViewById(R.id.addCourseBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        titleEditText = findViewById(R.id.titleEditText);

        final Intent i = getIntent();
        detailimg_list = i.getStringExtra("detailimg");
        subname_list = i.getStringExtra("subname");
        overview_list = i.getStringExtra("overview");
        cid = i.getStringExtra("cid");
        // course main 에서 cid 같은거 (첫번째것만) 정보 가져오기

        Log.d("dataAdd", detailimg_list + "  " + subname_list + "  " + overview_list);

        if (detailimg_list != null && subname_list != null && overview_list != null) {
            new task().execute();

            courseDetailItem = new CourseDetailItem(detailimg_list, subname_list, overview_list, cid);
            data.add(courseDetailItem);
            //Log.d("dataAdd", data.toString());

            adapter = new CourseDetailAdapter(getApplicationContext(), R.layout.course_detail_item, data);
            listView.setAdapter(adapter);

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    data.remove(position);
                    adapter.notifyDataSetChanged();
                    return false;
                }
            });
        }

        //hourEditText = findViewById(R.id.hourEditText);
        //minuteEditText = findViewById(R.id.minuteEditText);
        //costEditText = findViewById(R.id.costEditText);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*hour = hourEditText.getText().toString();
                minute = minuteEditText.getText().toString();
                cost = costEditText.getText().toString();*/
                placeTitle = titleEditText.getText().toString();
                CourseBasicList();

                for(idx=0; idx<data.size(); idx++) {
                    detailimg = data.get(idx).getDetailimg();
                    overview = data.get(idx).getOverview();
                    subname = data.get(idx).getSubname();
                    //Log.d("courseDBcheck", detailimg + " " + overview + " " + subname);
                    CourseList(idx);
                }
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        choose_course = findViewById(R.id.choose_course);
        choose_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCourseActivity.this, ChooseCourseActivity.class);
                startActivity(intent);
                finish();
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageview = (ImageView) findViewById(R.id.imageView);
        imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            imageview.setImageURI(selectedImageUri);
        }
    }

    public void CourseBasicList() {
        Log.d("courseBasicList", addr1 + " " + cat3 + " " + contentid + " " + contenttypeid + " " +
                firstimage + " " + mapx + " " + mapy + " " + mlevel + " " + placeTitle);
        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        String key = mPostReference.child("posts").push().getKey();

        FirebasePost post = new FirebasePost(addr1, cat3, contentid, contenttypeid,
                firstimage, firstimage2, mapx, mapy, mlevel, tel, placeTitle);
        Map<String, Object> postValues = post.toMap();
        Log.d("firebaseLog", String.format("%s",postValues));

        childUpdates.put("/course_basic_info/" + key, postValues);
        mPostReference.updateChildren(childUpdates);
    }

    public void CourseList(int idx) {
        Log.d("courseList", detailimg + " " + overview + " " + subname + " / " + contentid);
        mPostReference2 = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        String key = contentid;
        String courseKey = mPostReference2.child("posts").push().getKey();

        CourseData courseData = new CourseData(subid, detailalt, detailimg, overview, subname, subnum, key);
        Map<String, Object> postValues = courseData.toMap();
        Log.d("firebaseLog", String.format("%s",postValues));

        childUpdates.put("/course_list/" + key + "/" + courseKey, postValues);
        mPostReference2.updateChildren(childUpdates);
    }

    private class task extends AsyncTask<Void, String, Void> implements GoogleMap.OnMarkerClickListener {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params){

                CourseMainActivity.ReadCourseBasicList(new MyCallback() {
                    @Override
                    public void onCallback(String value) {
                        publishProgress(value);
                    }
                });

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            increase_num++;
            String[] value_split = values[0].split("#");
            firstimage_c = value_split[0]; // 이미지 URL String
            placeTitle_c = value_split[1]; // 코스 타이틀
            cid_c = value_split[2];

            if(cid.equals(cid_c)) {
                contentid = cid_c + increase_num;
                firstimage = firstimage_c;
                placeTitle = "New Course";
                Log.d("cid_c", "firstimage_c : " + firstimage_c + ", cid_c : " + cid_c + "/" + placeTitle_c + " (" + subname + ")");
                Log.d("cid_c", "firstimage : " + firstimage + ", cid : " + cid + "/" + placeTitle + " (" + subname + ")");
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }

        // onMarkerClick
        @Override
        public boolean onMarkerClick(Marker marker) {
            Log.d("Marker", marker.getTitle());
            Intent intent = new Intent(getApplicationContext(), placesActivity.class);
            intent.putExtra("title", marker.getTitle());                        // send marker title to placeDetailActivity
            intent.putExtra("lat", marker.getPosition().latitude+"");
            intent.putExtra("lng", marker.getPosition().longitude+"");

            startActivity(intent);                                                     // display place details
            return false;
        }
    }
}