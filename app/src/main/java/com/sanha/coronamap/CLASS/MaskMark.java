package com.sanha.coronamap.CLASS;

public class MaskMark {
    public Double lat;
    public Double lng;
    public  String storeName;
    public String stockTime;
    public  String UpdateTime;
    public  String remain;

    public MaskMark() {
    }

    public MaskMark(Double lat, Double lng, String storeName, String stockTime, String updateTime, String remain) {
        this.lat = lat;
        this.lng = lng;
        this.storeName = storeName;
        this.stockTime = stockTime;
        UpdateTime = updateTime;
        this.remain = remain;
    }


}
