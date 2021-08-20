package com.example.sorting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class OrderCheck extends AppCompatActivity {
    DBHelper mDBHelper = new DBHelper(this);
    ArrayList<AddressItem> addressItems = new ArrayList<AddressItem>();
    String trackingNum;  //운송장번호
    String address;      //주소

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setInit();
    }


    private void setInit() {
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
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data); // 결과값 파싱
        StringTokenizer st;

        if (result != null) { //null 아니면 정상적으로 qr코드 스캐너가 전달한 ActivityResult 값
            if (result.getContents() != null) { //스캐너가 qr코드를 정상적으로 인식 했다
                st = new StringTokenizer(result.getContents(), "\n");
                addressItems = mDBHelper.getAddressList();
                trackingNum = st.nextToken();   //운송장번호 전달
                address = st.nextToken();        //주소 전달

                int total = addressItems.size();
                int orderNumber = parcelOrder(trackingNum);

//                Toast.makeText(OrderCheck.this, "n / N 번째 택배입니다!", Toast.LENGTH_SHORT).show();

                if(orderNumber < total/3) {
                    LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.toast_back, null);
                    Toast toast = Toast.makeText(this, "토스트", Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                }
                else if(orderNumber < total/3*2) {
                    LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.toast_middle, null);
                    Toast toast = Toast.makeText(this, "토스트", Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                }

                else {
                    LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.toast_front, null);
                    Toast toast = Toast.makeText(this, "토스트", Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage(orderNumber + "/" + total +"번째 택배입니다!");
                builder.setTitle("택배 순서 체크");
                builder.setNegativeButton("스캔 더 하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scanCode();
                    }
                }).setPositiveButton("끝내기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private int parcelOrder(String number){ // QR 코드가 받은 운송장 번호에 해당하는 택배가 몇 번째 순서인지 알려주는 메소드
        int parcel_order = 0;   // 0번째로 초기화

        addressItems = mDBHelper.getAddressList();
        for(int i=0; i<addressItems.size();i++) {
            if (addressItems.get(i).getNumber().equals(number)){ // QR코드의 운송장번호와 복사본의 운송장번호가 같을 경우
                parcel_order = (i+1);
                break;  //parcel_order에 순서를 저장
            }
            else if(i == (addressItems.size()-1)){  // 복사본에 일치하는 데이터가 없는 경우
                parcel_order = -1;
            }
            else
                ;
        }

        return parcel_order;
    }

}

