package com.example.sorting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.skt.Tmap.TMapTapi;
import java.util.ArrayList;
import java.util.List;


public class TmapActivity extends AppCompatActivity {

    private DBHelper dbHelper = new DBHelper(this);
    ArrayList<AddressItem> addressItems = new ArrayList<>();
    private CustomAdapter mAdapter;
    private RecyclerView mRv_sorting;

    String APIKEY = "l7xxcdd63787be6c4e00aa5089373925bd14";

//     private void loadAllPackages(){
//         List<PackageInfo> appsInfo = getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES);
//         for(int i=0; i<appsInfo.size(); i++){
//             PackageInfo packageInfo = appsInfo.get(i);
//             Log.d("test", "설치된 패키지명 = " + packageInfo.packageName);
//         }
//     }
//    // 설치된 패키지명 모두 출력하는 함수

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