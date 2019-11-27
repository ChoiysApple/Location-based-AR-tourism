package com.marchengraffiti.nearism.nearism.course;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marchengraffiti.nearism.nearism.R;
import com.marchengraffiti.nearism.nearism.firebase.FirebasePost;
import com.marchengraffiti.nearism.nearism.firebase.MyCallback;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class CourseMainActivity extends AppCompatActivity {

    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();
    private ArrayList<CourseItem> data = new ArrayList<>();

    String firstimage, placeTitle, cid;

    ImageButton back, addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_main);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseMainActivity.this, AddCourseActivity.class);
                startActivity(intent);
            }
        });

        final ListView listView = findViewById(R.id.listView);

        ReadCourseBasicList(new MyCallback() {
            @Override
            public void onCallback(String value) {
                String[] value_split = value.split("#");
                firstimage = value_split[0]; // 이미지 URL String
                placeTitle = value_split[1]; // 코스 타이틀
                cid = value_split[2];
                Log.d("courseMainData", value);

                CourseItem item = new CourseItem(firstimage, placeTitle, cid);
                data.add(item);

                // course_item.xml 은 코스 메인화면에 보이는 리스트뷰
                // 코스 메인화면 레이아웃에 arrayList 세팅
                CourseAdapter adapter = new CourseAdapter(getApplicationContext(), R.layout.course_item, data);
                listView.setAdapter(adapter);

                // 리스트뷰 클릭시 화면 전환
                // 이미지 URL, 코스 타이틀, content id 넘김
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), CourseDetailActivity.class);
                        intent.putExtra("firstimage", data.get(position).getFirstImage());
                        intent.putExtra("placeTitle", data.get(position).getPlaceTitle());
                        intent.putExtra("cid", data.get(position).getCid());
                        startActivity(intent);
                    }
                });
            }
        });

    }

    // 디비 읽는 메소드
    // course_basic_info 는 contenttypeid=25인 코스 데이터에 대한 기본 정보 저장돼있음
    public static void ReadCourseBasicList(final MyCallback myCallback) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("course_basic_info");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey(); // course_basic_info 하위의 key 값 가져옴
                    FirebasePost get = postSnapshot.getValue(FirebasePost.class); // FirebasePost는 기본정보 가져오는 클래스
                    String[] info = {get.firstimage, get.placeTitle, get.contentid}; // get에 담겨있는 기본정보 중에서 firstimage랑 placeTitle, cid 가져옴
                    String result = info[0] + "#" + info[1] + "#" + info[2];
                    arrayData.add(result);
                    arrayIndex.add(key);
                    Log.d("FirebaseRead", result);
                    Log.d("FirebaseRead", key);
                    myCallback.onCallback(result); // result string을 바로 클래스에서 못 받아서 인터페이스 통해서 넘겨야 함
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }
}
