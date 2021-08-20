package com.example.sorting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private Button read;
    private Button order;
    private Button googleMap;
    private Button tmap;

    LinearLayout left;
    LinearLayout right;
    Animation translateOpenLeftAnim;
    Animation translateOpenRightAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        translateOpenLeftAnim = AnimationUtils.loadAnimation(this,R.anim.translate_open_left);
        translateOpenRightAnim = AnimationUtils.loadAnimation(this,R.anim.translate_open_right);

        SlidingAnimationListener animListener = new SlidingAnimationListener();
        translateOpenLeftAnim.setAnimationListener(animListener);
        translateOpenRightAnim.setAnimationListener(animListener);


        left = findViewById(R.id.left);
        left.startAnimation(translateOpenLeftAnim);
        left.setVisibility(View.VISIBLE);

        right = findViewById(R.id.right);
        right.startAnimation(translateOpenRightAnim);
        right.setVisibility(View.VISIBLE);






        read = findViewById(R.id.barcode);

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ListMaking.class);
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

    class SlidingAnimationListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}

