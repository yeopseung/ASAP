package com.example.sorting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.skt.Tmap.TMapTapi;
import java.util.ArrayList;


public class TmapActivity extends AppCompatActivity {

    private DBHelper dbHelper = new DBHelper(this);
    ArrayList<AddressItem> addressItems = new ArrayList<>();
    private CustomAdapter mAdapter;
    private RecyclerView mRv_sorting;

    String APIKEY = "l7xxcdd63787be6c4e00aa5089373925bd14";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmap);
        loadRecentDB();

        TMapTapi tMapTapi = new TMapTapi(this);
        tMapTapi.setSKTMapAuthentication(APIKEY);
        tMapTapi.setOnAuthenticationListener(new TMapTapi.OnAuthenticationListenerCallback() {
            @Override
            public void SKTMapApikeySucceed() {
                System.out.println("APIKEY 인증 성공 !");
                //loadAllPackages(); // 설치된 전체 패키지 목록 출력.
            }
            @Override
            public void SKTMapApikeyFailed(String errorMsg) {
                System.out.println("APIKEY 인증 실패 !");
            }
        });
    }
    private void loadRecentDB() {
        addressItems = dbHelper.getAddressList();
        if(mAdapter == null){
            mAdapter = new CustomAdapter(addressItems,this);
            mRv_sorting = findViewById(R.id.rv_sorting);
            mRv_sorting.setHasFixedSize(true);
            mRv_sorting.setAdapter(mAdapter);
        }
    }
}