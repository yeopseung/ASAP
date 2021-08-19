package com.example.sorting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.StringTokenizer;

public class OrderCheck extends AppCompatActivity {
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
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
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
                trackingNum = st.nextToken();   //운송장번호 전달
                address = st.nextToken();        //주소 전달

//                Toast.makeText(OrderCheck.this, "n / N 번째 택배입니다!", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("n / N 번째 택배입니다!");
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
}
