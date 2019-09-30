package com.marchengraffiti.nearism.nearism.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MarkerItem implements ClusterItem {
    LatLng location;
    String msg;

    public MarkerItem(LatLng location, String msg) {
        this.location = location;
        this.msg = msg;
    }

    @Override
    public LatLng getPosition() {
        return location;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}