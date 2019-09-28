package com.marchengraffiti.nearism.nearism;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class placesActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_detail);
    }

    ImageView mainImage = findViewById(R.id.main_photo);
    TextView title = findViewById(R.id.title);
    TextView adress = findViewById(R.id.place_addr);

    


}
