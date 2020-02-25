package com.sanha.coronamap.CLASS;

public class MarkerData{
    public String nNum;
    public String detail;
    public String happenData;
    public double mLatitude;
    public double mLongitude;
    public int marksNum;

    public MarkerData(String nn, String de, double lt, double lg, int mn, String hd){
        nNum = nn;
        detail = de;
        mLatitude = lt;
        mLongitude = lg;
        marksNum = mn;
        happenData = hd;
    }

    public MarkerData(){

    }
}
