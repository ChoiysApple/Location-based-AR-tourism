package com.marchengraffiti.nearism.nearism.firebase;

import java.util.HashMap;
import java.util.Map;

public class FirebasePost {
    public String addr1 = null, cat3 = null, contentid = null, contenttypeid = null;
    public String firstimage = null, firstimage2 = null, mapx = null, mapy = null;
    public String mlevel = null, tel = null, placeTitle = null;

    public FirebasePost() {

    }

    public FirebasePost(String addr1, String cat3, String contentid, String contenttypeid,
                        String firstimage, String firstimage2, String mapx, String mapy,
                        String mlevel, String tel, String placeTitle) {
        this.addr1 = addr1;
        this.cat3 = cat3;
        this.contentid = contentid;
        this.contenttypeid = contenttypeid;
        this.firstimage = firstimage;
        this.firstimage2 = firstimage2;
        this.mapx = mapx;
        this.mapy = mapy;
        this.mlevel = mlevel;
        this.tel = tel;
        this.placeTitle = placeTitle;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("addr1", addr1);
        result.put("cat3", cat3);
        result.put("contentid", contentid);
        result.put("contenttypeid", contenttypeid);
        result.put("firstimage", firstimage);
        result.put("firstimage2", firstimage2);
        result.put("mapx", mapx);
        result.put("mapy", mapy);
        result.put("mlevel", mlevel);
        result.put("tel", tel);
        result.put("placeTitle", placeTitle);

        return result;
    }

}
