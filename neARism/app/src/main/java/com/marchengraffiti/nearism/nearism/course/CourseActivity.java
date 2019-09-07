package com.marchengraffiti.nearism.nearism.course;

import android.os.Bundle;

import com.marchengraffiti.nearism.nearism.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CourseActivity extends AppCompatActivity {

    final int ITEM_SIZE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        List<Item> items = new ArrayList<>();
        Item[] item = new Item[ITEM_SIZE];
        item[0] = new Item(R.drawable.a, R.drawable.add_course, "대장경테마파크", "대정경 조판이전부터 경전의 전래와 결집, 천년을 이어왔던 장경판전의 숨겨진 과학에 이르는 역사의 시공간적 대장정을 감상할 수 있다.");
        item[1] = new Item(R.drawable.b, R.drawable.add_course, "해인사(합천)", "해인사를 우리나라 삼보사찰의 하나인 법보사찰이라 부르는 것은 해인사 대장경판전에 고려대장경판인 법보가 보관되어 있기 때문이다. ");
        item[2] = new Item(R.drawable.b, R.drawable.add_course,"달의 정원", "한옥 뒤로 가야산 정상이 한눈에 담긴다. 달의 정원은 총 10개 객실을 마련했다.");
        item[3] = new Item(R.drawable.c, R.drawable.add_course, "황매산(산청)", "정상에 올라서면 주변의 풍광이 활짝 핀 매화꽃잎 모양을 닮아 마치 매화꽃 속에 홀로 떠 있는 듯 신비한 느낌을 주어 황매산이라 부른다.");
        item[4] = new Item(R.drawable.c, R.drawable.add_course, "함벽루", "취적봉 기슭에 위치하여 황강 정양호를 바라보는 수려한 풍경으로 많은 시인 묵객들이 풍류를 즐긴 장소로, 퇴계 이황, 남명 조식, 우암 송시열 등의 글이 걸려 있다.");

        for (int i = 0; i < ITEM_SIZE; i++) {
            items.add(item[i]);
        }

        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), items, R.layout.activity_course));
    }
}