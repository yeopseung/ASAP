package com.example.sorting;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListMaking extends AppCompatActivity {
    private Button newList;
    private Button oldList;
    private DBHelper mDBHelper = new DBHelper(this);
    private Algorithm algorithm = new Algorithm(this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_listmaking);

        oldList = findViewById(R.id.btn_oldList); // btn_oldList 버튼을 눌렀을 때 기존 DB에 추가
        oldList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 팝업 창 띄우기
                ArrayList<AddressItem> addressItems = mDBHelper.getAddressList();

                Intent intent = new Intent(ListMaking.this,reading.class);
                startActivity(intent);

            }
        });



        newList = findViewById(R.id.btn_newList); // btn_newList 버튼을 눌렀을 때 새로운 DB 생성
        newList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<AddressItem> addressItems = mDBHelper.getAddressList();
//                algorithm.sortAlgorithm(addressItems,37.47716016671259, 126.86673391650392);
                mDBHelper.dbInitialize(); // db 초기화

                Intent intent = new Intent(ListMaking.this,reading.class);
                startActivity(intent);
            }
        });
    }
}