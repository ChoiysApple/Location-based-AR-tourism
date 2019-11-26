package com.marchengraffiti.nearism.nearism.map;

public class MarkerItem {
    double lat, lng;
    String msg;

    public MarkerItem(double lat, double lng, String msg) {
        this.lat = lat;
        this.lng = lng;
        this.msg = msg;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}