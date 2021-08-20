package com.example.sorting;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListMaking extends AppCompatActivity {
    private Button newList;
    private Button oldList;
    private DBHelper mDBHelper = new DBHelper(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_listmaking);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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



                builder.setMessage("새 목록이 생성되면 기존 목록이 삭제됩니다.\n계속하시겠습니까?");

                builder.setNegativeButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDBHelper.dbInitialize(); // db 초기화
                        Intent intent = new Intent(ListMaking.this,reading.class);
                        startActivity(intent);
                    }
                }).setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }


        });
    }
}