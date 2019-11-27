package com.marchengraffiti.nearism.nearism.course;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.marchengraffiti.nearism.nearism.R;
import com.marchengraffiti.nearism.nearism.firebase.CourseRead;
import com.marchengraffiti.nearism.nearism.firebase.MyCallback;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class CourseDetailActivity extends AppCompatActivity {

    String subnum, detailimg, subname, overview, cid;
    String contentid;

    ImageButton back;

    private ArrayList<CourseDetailItem> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

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
                //subnum = value_split[0]; // 코스 장소 순서
                detailimg = value_split[1]; // 각 코스 장소마다 이미지
                subname = value_split[2]; // 코스 장소 이름
                overview = value_split[3]; // 코스 장소 상세 설명
                cid = value_split[4]; // 메인 코스랑 장소 매칭시키려고
                Log.d("courseData2222", value);

                CourseDetailItem item = new CourseDetailItem(detailimg, subname, overview, cid);
                if(cid.equals(intent.getStringExtra("cid")))
                    data.add(item);
                Log.d("cidLog", intent.getStringExtra("cid"));
                Log.d("cidLog", "cid = " + cid);

                // course_item.xml 은 코스 메인화면에 보이는 리스트뷰
                // 코스 메인화면 레이아웃에 arrayList 세팅
                CourseDetailAdapter adapter = new CourseDetailAdapter(getApplicationContext(), R.layout.course_detail_item, data);
                listView.setAdapter(adapter);
            }
        });

    }

}
