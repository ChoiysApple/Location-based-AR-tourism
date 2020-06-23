package com.marchengraffiti.nearism.nearism;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.marchengraffiti.nearism.nearism.map.MapFragment;

// Done : 메인 화면, 장소 디테일 화면, 4square - 티맵으로 교체
// To do : auto complete 검색창,
//         placesActivity 에서 지도 프레그먼트 카메라 위치 이동, 메인화면 지도 카메라 위치 이동

public class MainActivity extends AppCompatActivity {

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

        //final ParsingAPI parsingAPI = new ParsingAPI();
        //parsingAPI.connection();
    }

}