package com.marchengraffiti.nearism.nearism.course;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.marchengraffiti.nearism.nearism.R;

import androidx.appcompat.app.AppCompatActivity;

public class CourseDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        ImageView firstimage = (ImageView) findViewById(R.id.firstimage);
        Intent intent = getIntent();
        TextView placeTitle = findViewById(R.id.placeTitle);

        //imgURL = intent.getStringExtra("firstimage");
        //Log.d("firebaseLog", imgURL);
        placeTitle.setText(intent.getStringExtra("placeTitle"));

    }

}
