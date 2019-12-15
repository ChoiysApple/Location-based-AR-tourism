package com.marchengraffiti.nearism.nearism.course;

import java.util.HashMap;
import java.util.Map;

public class CourseMarkerData {
    public String subname = null, latitude = null, longitude = null;

    public CourseMarkerData() {

    }

    public CourseMarkerData(String subname, String latitude, String longitude) {
        this.subname = subname;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("subname", subname);
        result.put("latitude", latitude);
        result.put("longitude", longitude);

        return result;
    }
}
