package com.example.sorting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.SlidingDrawer;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback, MarkerAdapter.OnStartDragListener {
    private GoogleMap googleMap;

    Animation translateLeftAnim;
    Animation translateRightAnim;
    ConstraintLayout markerlist;
    Button button;
    boolean isPageOpen=false;
    private DBHelper mDBHelper = new DBHelper(this);
    private ArrayList<AddressItem> mAddressItems;

    private String mAddress;
    private double mLongitude;
    private double mLatitude;
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

//        AddressItem addressItem = new AddressItem();
//        addressItem.setId(1);
//        addressItem.setNumber("1");
//        addressItem.setAddress("광명고등학교");
//        addressItem.setLatitude(37.478593);
//        addressItem.setLongitude(126.866050);
//

        //mapFragment 선언
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        AndPermission.with(this)
                .runtime()
                .permission(
                        Permission.ACCESS_FINE_LOCATION,
                        Permission.ACCESS_COARSE_LOCATION)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        showToast("허용된 권한 갯수 : " + permissions.size());
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        showToast("거부된 권한 갯수 : " + permissions.size());
                    }
                })
                .start();



        //슬라이딩 애니메이션을 활용 (화면 열기, 닫기)
        markerlist= findViewById(R.id.markerlist);
        translateLeftAnim = AnimationUtils.loadAnimation(this,R.anim.translate_left);
        translateRightAnim = AnimationUtils.loadAnimation(this,R.anim.translate_right);

        SlidingAnimationListener animListener = new SlidingAnimationListener();
        translateLeftAnim.setAnimationListener(animListener);
        translateRightAnim.setAnimationListener(animListener);

        button = findViewById(R.id.sliding_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPageOpen){
                    markerlist.startAnimation(translateLeftAnim);

                }
                else {
                    markerlist.setVisibility(View.VISIBLE);
                    markerlist.startAnimation(translateRightAnim);

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
                markerlist.setVisibility(View.INVISIBLE);
                button.setText("마커목록");
                isPageOpen = false;
            }
            else{
                button.setText("닫기");
                isPageOpen = true;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }


    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;


        Context context = this;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //현 위치를 불러와주는 함수
            startLocationService();

            //GoogleMap에서 제공하는 현 위치 불러와주는 함수
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);


        }


        //현재 마커 갱신
        for (int i=0;i< mAddressItems.size();i++){
            mLatitude = mAddressItems.get(i).getLatitude();
            mLongitude = mAddressItems.get(i).getLongitude();
            mAddress = mAddressItems.get(i).getAddress();
            NewMarker(mAddress,mLatitude,mLongitude);
        }


//        NewMarker(37.478593, 126.866050, "광명고등학교");
//        NewMarker(37.48704614469403, 126.86800142510582, "광명북초등학교");
//        NewMarker(37.484143880672896, 126.86361479883942, "광명동초등학교");
//        NewMarker(37.48511581598014, 126.87027489645584, "도덕초등학교");
//

    }


//    public  static Location ChangeAddrtoLatLng(Context context, String address, int size){
//        Location location = new Location("");
//        final Geocoder geocoder = new Geocoder(context);
//        List<Address> addresses = null;
//
//            try{
//                addresses = geocoder.getFromLocationName(address,1);
//            }catch (IOException e){
//                e.printStackTrace();
//                Log.d("tag","onComplete: 주소변환 실패");
//            }
//            if(addresses != null){
//                for(int j =0; j< addresses.size(); j++){
//                    Address latlng = addresses.get(j);
//                    location.setLatitude(latlng.getLatitude());
//                    location.setLongitude(latlng.getLongitude());
//                }
//            }
//        return location;
//    }

//    public static Location findGeoPoint(Context mcontext, String address) {
//        Location loc = new Location("");
//        Geocoder coder = new Geocoder(mcontext);
//        List<Address> addr = null;// 한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 설정
//
//        try {
//            addr = coder.getFromLocationName(address, 5);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }// 몇개 까지의 주소를 원하는지 지정 1~5개 정도가 적당
//        if (addr != null) {
//            for (int i = 0; i < addr.size(); i++) {
//                Address lating = addr.get(i);
//                double lat = lating.getLatitude(); // 위도가져오기
//                double lon = lating.getLongitude(); // 경도가져오기
//                loc.setLatitude(lat);
//                loc.setLongitude(lon);
//            }
//        }
//        return loc;
//    }



    //새로운 마커를 추가해주는 함수 (위도, 경도, 주소) 입력
    public Marker NewMarker(String name, double latitude, double longtitude){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(latitude, longtitude));
        markerOptions.title(name);
        Marker marker = googleMap.addMarker(markerOptions);
        return marker;
    }


    //GPS
    class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
//            Double latitude = location.getLatitude();
//            Double longitude = location.getLongitude();
//
//            String message = "내 위치 -> Latitude : "+ latitude + "\nLongitude:"+ longitude;
//            Log.d("Map", message);
        }

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
//                String message = "최근 위치 -> Latitude : " + latitude + "\nLongitude:" + longitude;
//
//                Log.d("Map", message);
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
        NewMarker("현 위치",latitude,longitude);
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