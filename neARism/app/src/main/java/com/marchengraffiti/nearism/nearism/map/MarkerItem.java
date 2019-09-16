package com.marchengraffiti.nearism.nearism.map;

public class MarkerItem {
    double latitude, longitude;
    String msg;

    public MarkerItem(double latitude, double longitude, String msg) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
