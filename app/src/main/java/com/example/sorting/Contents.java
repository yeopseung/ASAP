package com.example.sorting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class Contents extends AppCompatActivity {


    private RecyclerView mRv_sorting;
    private ArrayList<AddressItem> mAddressItems;
    private DBHelper mDBHelper = new DBHelper(this);
    private CustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        setInit();
    }

    private void setInit() {  //mAddressItems 에 DB 내용을 저장하는 메소드
        mAddressItems = new ArrayList<>();
        loadRecentDB();
    }
    private void loadRecentDB() {  //저장되어 있던 DB를 복사하여 mAddressItems 에 저장하는 메소드
        mAddressItems = mDBHelper.getAddressList();
        if(mAdapter == null){
            mAdapter = new CustomAdapter(mAddressItems,this);
            mRv_sorting = findViewById(R.id.rv_sorting);
            mRv_sorting.setHasFixedSize(true);
            mRv_sorting.setAdapter(mAdapter);
        }
    }

    public int getSize(){  //DB에 저장된 table 개수 리턴하는 메소드
        mAddressItems = mDBHelper.getAddressList();
        return mAddressItems.size();
    }

}
