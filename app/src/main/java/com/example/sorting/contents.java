package com.example.sorting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class contents extends AppCompatActivity {


    private RecyclerView mRv_sorting;
    private ArrayList<AddressItem> mAddressItems;
    private DBHelper mDBHelper = new DBHelper(this);
    private CustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

//        mDBHelper.InsertAddress("1","광명고등학교",37.478593,126.866050);

        setInit();
    }

    private void setInit() {
        mAddressItems = new ArrayList<>();
        loadRecentDB();
    }
    private void loadRecentDB() {

        //저장되어 있던 DB를 가져온다.
        mAddressItems = mDBHelper.getAddressList();
        if(mAdapter == null){
            mAdapter = new CustomAdapter(mAddressItems,this);
            mRv_sorting = findViewById(R.id.rv_sorting);
            mRv_sorting.setHasFixedSize(true);
            mRv_sorting.setAdapter(mAdapter);
        }
    }
    public int getSize(){
        mAddressItems = mDBHelper.getAddressList();
        return mAddressItems.size();
    }

}
