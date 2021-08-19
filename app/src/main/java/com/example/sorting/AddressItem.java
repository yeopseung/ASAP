package com.example.sorting;

public class AddressItem {
    private int id;             //게시물의 고유 ID
    private String number;      //운송장번호
    private String address;     //주소
    private double latitude;     //위도
    private double longitude;    //경도


    public AddressItem() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() { return latitude;}

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude;}

    public void setLongitude(double longitude) { this.longitude = longitude; }

}