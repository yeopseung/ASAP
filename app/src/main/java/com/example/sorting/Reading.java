package com.example.sorting;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Reading extends AppCompatActivity {
    String trackingNum;  //운송장번호
    String address;      //주소
    double latitude;     //위도
    double longitude;    //경도

    private ArrayList<AddressItem> mAddressItems;
    private DBHelper mDBHelper = new DBHelper(this);
    private CustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInit();
    }



    private void setInit() {
        mAddressItems = new ArrayList<>();
        loadRecentDB();
        scanCode();
    }

    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);    //세로모드 지원하기 위
        integrator.setOrientationLocked(false); //이것도
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("택배 운송장 바코드 스캔"); //스캐너 하단부에 메세지 띄움
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        GeocodeUtil geocodeUtil = new GeocodeUtil(Reading.this);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data); // 결과값 파싱
        StringTokenizer st;

        if (result != null) { //null 아니면 정상적으로 qr코드 스캐너가 전달한 ActivityResult 값
            if (result.getContents() != null) { //스캐너가 qr코드를 정상적으로 인식 했다
                st = new StringTokenizer(result.getContents(), "\n");
                trackingNum = st.nextToken();   //운송장번호 전달
                address = st.nextToken();        //주소 전달
                latitude = geocodeUtil.getGeoLocationListUsingAddress(address).get(0).latitude; //위도 전달
                longitude = geocodeUtil.getGeoLocationListUsingAddress(address).get(0).longitude;   //경도 달

                // insert DB
                mDBHelper.InsertAddress(trackingNum, address, latitude, longitude);
                Toast.makeText(Reading.this, "할일 목록에 추가 되었습니다 !", Toast.LENGTH_SHORT).show();

                scanCode();
//
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void loadRecentDB() {

        //저장되어 있던 DB를 가져온다.
        mAddressItems = mDBHelper.getAddressList();
        if (mAdapter == null) {
            mAdapter = new CustomAdapter(mAddressItems, this);
        }
    }
}

class GeocodeUtil { //주소 <=> 위도경도 변환
    final Geocoder geocoder;

    public static class GeoLocation { //이너클래스
        double latitude;
        double longitude;

        public GeoLocation(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    public GeocodeUtil(Context context) {   //생성자
        geocoder = new Geocoder(context);
    }

    public ArrayList<GeoLocation> getGeoLocationListUsingAddress(String address) {  //주소를 위도경도로 변환하는 메소
        ArrayList<GeoLocation> resultList = new ArrayList<>();
        try {
            List<Address> list = geocoder.getFromLocationName(address, 10);

            for (Address addr : list) {
                resultList.add(new GeoLocation(addr.getLatitude(), addr.getLongitude()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
}