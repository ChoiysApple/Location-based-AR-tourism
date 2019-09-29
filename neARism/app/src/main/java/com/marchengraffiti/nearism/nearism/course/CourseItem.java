package com.marchengraffiti.nearism.nearism.course;

public class CourseItem {
    String firstimage, placeTitle, cid;

    public String getFirstImage() {
        return firstimage;
    }

    public String getPlaceTitle() {
        return placeTitle;
    }

    public String getCid() {
        return cid;
    }

    public CourseItem(String firstimage, String placeTitle, String cid) {
        this.firstimage = firstimage;
        this.placeTitle = placeTitle;
        this.cid = cid;
    }
}
