package com.example.sorting;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.lang.Math;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Algorithm extends DBHelper {


    private static boolean[] IsVisit; // 방문 여부 체크하는 Boolean 배열.
    public Algorithm(@Nullable @org.jetbrains.annotations.Nullable Context context) {
        super(context);
    }


    private double calcEachDistance(double x1, double y1, double x2, double y2){
        return Math.sqrt( Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) );
    }
    // 각 노드끼리의 직선거리 계산 (calcShortestNode()에서 활용됨).


    private int calcShortestNode(ArrayList<AddressItem> arrayList, double currXLocation, double currYLocation){
        double shortestDist = 100; // 거리 중 최댓값보다 항상 커야 해서 무식하게 100 넣었음.
        int shortestIndex = -1;

        for(int i=0; i<arrayList.size(); i++){
            if(!IsVisit[i]){ // 선택 된 적 없는 노드인 경우에 대해서만 계산.
                double tmp = calcEachDistance(currXLocation, currYLocation, arrayList.get(i).getLatitude(), arrayList.get(i).getLongitude());
                if(shortestDist > tmp){
                    shortestDist = tmp;
                    shortestIndex = i;
                }
            }
        }
        return shortestIndex;
    }
    // 현 위치에서 가장 가까운 노드의 인덱스(id) 리턴 -> runAlgorithm()메소드에서 활용 됨.


    public int[] runAlgorithm (ArrayList<AddressItem> addressItems, double currXLocation, double currYLocation) {  //현 위치 x, y좌표 넣기. // db는 이미 static 으로 만든 addressItems 사용.

        IsVisit = new boolean[addressItems.size()]; // 방문여부 체크 배열 사이즈 지정.
        for(int i=0; i<IsVisit.length; i++){ IsVisit[i] = false; } // 방문 여부는 모두 false 로 초기화.

        int[] array = new int[IsVisit.length]; // IsVisit 배열과 같은 사이즈로 int 형 배열 선언.

        for(int i=0; i<IsVisit.length; i++){
            int tmp = calcShortestNode(addressItems, currXLocation, currYLocation);
            IsVisit[tmp] = true;
            array[i] = tmp;
            currXLocation = addressItems.get(tmp).getLatitude(); // 현 위치가 가장 최근에 방문한 노드로 옮겨졌다고 가정.
            currYLocation = addressItems.get(tmp).getLongitude(); // 현 위치가 가장 최근에 방문한 노드로 옮겨졌다고 가정.
        }
        return array;
    }

    public void sortAlgorithm(ArrayList<AddressItem> Items, double currXLocation, double currYLocation){

        SQLiteDatabase db = getWritableDatabase();
        int tempId;
        String tempNumber;      //운송장번호
        String tempAddress;     //주소
        double tempLatitude;     //위도
        double tempLongitude;    //경도
        int[] shortArray;       //
        ArrayList<AddressItem> items = new ArrayList<AddressItem>();

        shortArray = runAlgorithm (Items, currXLocation, currYLocation);

        if(shortArray.length == Items.size()) { // runAlgorithm 의 인덱스 수와 Items 의 인덱스 수가 같을 때 DB 갱신 수행
            for (int i = 0; i < shortArray.length; i++) { // 임시로 만든 addressItems 에 최단경로대로 데이터 저장
                int index;
                index = shortArray[i];
                items.add(Items.get(index));
            }

            for (int i = 0; i < items.size(); i++) { // 최단경로 순서대로 저장된 addressItems 의 데이터를 복사하여 DB에 저장
                tempId = Items.get(i).getId();
                tempNumber = items.get(i).getNumber();
                tempAddress = items.get(i).getAddress();
                tempLatitude = items.get(i).getLatitude();
                tempLongitude = items.get(i).getLongitude();
                db.execSQL("UPDATE AddressList SET number = '" + tempNumber + "', address = '" + tempAddress + "', latitude = '" + tempLatitude + "', longitude = '" + tempLongitude + "' WHERE id = '" + tempId + "'");
            }
        }

    }

//    private int[] runAlgorithm (ArrayList<AddressItem> addressItems, double currXLocation, double currYLocation) {
//        IsVisit = new boolean[addressItems.size()];
//        for(int i=0; i<IsVisit.length; i++){ IsVisit[i] = false; }
//        int[] array = new int[IsVisit.length];
//        for(int i=0; i<IsVisit.length; i++){
//            double shortestDist = 100; // 거리 중 최댓값보다 항상 커야 해서 무식하게 100 넣었음.
//            int shortestIndex = -1;
//            for(int j=0; j<IsVisit.length; j++){
//                if(!IsVisit[j]){
//                    double tmp = calcEachDistance(currXLocation, currYLocation, addressItems.get(j).getLatitude(), addressItems.get(j).getLongitude());
//                    if(tmp < shortestDist){
//                        shortestDist = tmp;
//                        shortestIndex = j;
//                    }
//                }
//            }
//
//            array[i] = shortestIndex;
//        }
//        return array;
//    }


}