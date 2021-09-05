package com.example.sorting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback, MarkerAdapter.OnStartDragListener {
    private GoogleMap googleMap;

    Animation translateUpAnim;
    Animation translateDownAnim;
    ConstraintLayout markers;
    Button sliding_button;


    boolean isPageOpen=false;
    private DBHelper mDBHelper = new DBHelper(this);
    private ArrayList<AddressItem> mAddressItems;

    private Location curLocation;

    private MarkerAdapter adapter;
    private ItemTouchHelper itemTouchHelper;
    private Algorithm algorithm = new Algorithm(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        //DB에 있는 리스트 불러오기
        mAddressItems = new ArrayList<>();
        mAddressItems = mDBHelper.getAddressList();
        if(adapter == null){
            adapter = new MarkerAdapter(mAddressItems,this,this);
        }


        //mapFragment 선언
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        AndPermission.with(this)
                .runtime()
                .permission(
                        Permission.ACCESS_FINE_LOCATION,
                        Permission.ACCESS_COARSE_LOCATION)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //showToast("허용된 권한 갯수 : " + permissions.size());
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //showToast("거부된 권한 갯수 : " + permissions.size());
                    }
                })
                .start();



        //슬라이딩 애니메이션을 활용 (화면 열기, 닫기)
        markers = findViewById(R.id.markerlist);
        translateUpAnim = AnimationUtils.loadAnimation(this,R.anim.translate_up);
        translateDownAnim = AnimationUtils.loadAnimation(this,R.anim.translate_down);

        SlidingAnimationListener animListener = new SlidingAnimationListener();
        translateUpAnim.setAnimationListener(animListener);
        translateDownAnim.setAnimationListener(animListener);

        sliding_button = findViewById(R.id.sliding_button);
        sliding_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPageOpen){
                    markers.startAnimation(translateUpAnim);

                }
                else {
                    markers.setVisibility(View.VISIBLE);
                    markers.startAnimation(translateDownAnim);

                }
            }
        });


        //RecyclerView 연결
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        //삭제, 이동을 위한 itemTouchHelper
        itemTouchHelper = new ItemTouchHelper(new MarkerItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);


        //화면 업데이트 버튼 (마커 삭제 or 수정한 것을 적용)
        Button apply_button = findViewById(R.id.apply_button);
        apply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGoogleMap();
            }
        });


        Button sort_button = findViewById(R.id.sort_button);
        sort_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<AddressItem> addressItems = mDBHelper.getAddressList();
                algorithm.sortAlgorithm(addressItems,curLocation.getLatitude(),curLocation.getLongitude());  //현재 위치를 알고리즘에 반영
                showToast("자동정렬 완료");
            }
        });

    }



    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }


    class SlidingAnimationListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if(isPageOpen){
                markers.setVisibility(View.INVISIBLE);
                sliding_button.setText("마커목록");
                isPageOpen = false;
            }
            else{
                sliding_button.setText("닫기");
                isPageOpen = true;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }


    public void showToast(String message) {
       Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(@NotNull GoogleMap googleMap) {
        this.googleMap = googleMap;


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //현 위치를 불러와주는 함수
            startLocationService();
            //GoogleMap에서 제공하는 현 위치 불러와주는 함수
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);


        }


        //현재 마커 갱신
        for (int i=0;i< mAddressItems.size();i++){
            double mLatitude = mAddressItems.get(i).getLatitude();
            double mLongitude = mAddressItems.get(i).getLongitude();
            String mAddress = mAddressItems.get(i).getAddress();
            NewMarker(mAddress, mLatitude, mLongitude,i+1);
        }

    }


    //새로운 마커를 추가해주는 함수 (위도, 경도, 주소) 입력
    public void NewMarker(String name, double latitude, double longitude, int i){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(latitude, longitude));
        markerOptions.title(Integer.toString(i));
        markerOptions.snippet(name);
        googleMap.addMarker(markerOptions);
    }


    //GPS
    static class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) { }

        public void onProviderDisabled(String provider) { }

        public void onProviderEnabled(String provider) { }

        public void onStatusChanged(String provider, int status, Bundle extras) { }
    }


    //GPS를 활용한 현 위치 제공 함수
    public void startLocationService() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                curLocation = location;
                showCurrentLocation(latitude, longitude);  //GPS로 받은 위도 경도 전달
            }

            GPSListener gpsListener = new GPSListener();
            long minTime = 10000;
            float minDistance = 0;

            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime, minDistance, gpsListener);

            Toast.makeText(getApplicationContext(), "내 위치확인 요청함",
                    Toast.LENGTH_SHORT).show();

        } catch(SecurityException e) {
            e.printStackTrace();
        }

    }


    //현 위치를 마커를 찍어 보여줌
    private void showCurrentLocation(Double latitude, Double longitude) {
        LatLng curPoint = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
    }

    //구글맵 새로고침 (마커 업데이트에 활용)
    public void updateGoogleMap(){

        try {
            Intent intent = getIntent();
            finish(); //현재 액티비티 종료
            overridePendingTransition(0, 0);
            startActivity(intent); //현재 액티비티 재실행
            overridePendingTransition(0, 0);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}