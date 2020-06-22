package com.marchengraffiti.nearism.nearism;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.marchengraffiti.nearism.nearism.course.CourseMainActivity;
import com.marchengraffiti.nearism.nearism.map.MapFragment;
import com.marchengraffiti.nearism.nearism.tflite.ClassifierActivity;

// Done : 메인 화면, 장소 디테일 화면 - 티맵으로 교체
// To do : locationFab 버튼 연결, auto complete 검색창,
//         placesActivity 에서 지도 프레그먼트 카메라 위치 이동, 메인화면 지도 카메라 위치 이동
//         코스 쪽은 지도 아직 안 바꿈

public class MainActivity extends AppCompatActivity {

    int flag = 0;
    FloatingActionButton locationFab1, fab2, locationFab3, fab3, fab1, locationFab2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // t map
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.tmap_container, new MapFragment());
        fragmentTransaction.commit();



        fab3 = findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ArImageActivity.class);
                startActivity(i);
            }
        });

        fab2 = findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClassifierActivity.class);
                startActivity(intent);
            }
        });

        fab1 = findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CourseMainActivity.class);
                startActivity(i);
            }
        });

        locationFab1 = findViewById(R.id.locationFab1); // 음식점
        locationFab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                //new parsing_task().execute();
            }
        });

        locationFab2 = findViewById(R.id.locationFab2); // 카페
        locationFab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 2;
                //new parsing_task().execute();
            }
        });

        locationFab3 = findViewById(R.id.locationFab3); // 호텔
        locationFab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 3;
                //new parsing_task().execute();
            }
        });

        //final ParsingAPI parsingAPI = new ParsingAPI();
        //parsingAPI.connection();
    }
}