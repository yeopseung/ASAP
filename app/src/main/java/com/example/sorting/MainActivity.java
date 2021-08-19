package com.example.sorting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button read;
    private Button order;
    private Button googleMap;
    private Button tmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        read = findViewById(R.id.barcode);
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,reading.class);
                startActivity(intent);
            }
        });
        googleMap = findViewById(R.id.googleMap);
        googleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GoogleMapActivity.class);
                startActivity(intent);
            }
        });
        order = findViewById(R.id.orderCheck);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,OrderCheck.class);
                startActivity(intent);
            }
        });

        tmap = findViewById(R.id.tmap);
        tmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TmapActivity.class);
                startActivity(intent);
            }
        });
    }
}