package com.marchengraffiti.nearism.nearism.firebase;

import java.util.HashMap;
import java.util.Map;

public class CourseData {
    public String subid = null, detailalt = null, detailimg = null;
    public String overview = null, subname = null, subnum = null;
    public String key = null; // key는 cid임

    public CourseData() {

    }

    public CourseData(String subid, String detailalt, String detailimg,
                      String overview, String subname, String subnum, String key) {
        this.subid = subid;
        this.detailalt = detailalt;
        this.detailimg = detailimg;
        this.overview = overview;
        this.subname = subname;
        this.subnum = subnum;
        this.key = key;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("subid", subid);
        result.put("detailalt", detailalt);
        result.put("detailimg", detailimg);
        result.put("overview", overview);
        result.put("subname", subname);
        result.put("subnum", subnum);
        result.put("key", key);

        return result;
    }
}
