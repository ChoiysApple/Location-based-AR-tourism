package com.marchengraffiti.nearism.nearism.course;

public class CourseItem {
    String firstimage, placeTitle;

    public String getFirstImage() {
        return firstimage;
    }

    public String getPlaceTitle() {
        return placeTitle;
    }

    public CourseItem(String firstimage, String placeTitle) {
        this.firstimage = firstimage;
        this.placeTitle = placeTitle;
    }
}
