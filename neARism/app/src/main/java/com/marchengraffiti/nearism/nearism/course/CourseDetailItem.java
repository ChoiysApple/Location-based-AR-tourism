package com.marchengraffiti.nearism.nearism.course;

public class CourseDetailItem {
    String subnum, detailimg, subname, overview;

    public String getSubnum() {
        return subnum;
    }

    public String getDetailimg() {
        return detailimg;
    }

    public String getSubname() {
        return subname;
    }

    public String getOverview() {
        return overview;
    }

    public CourseDetailItem(String subnum, String detailimg, String subname, String overview) {
        this.subnum = subnum;
        this.detailimg = detailimg;
        this.subname = subname;
        this.overview = overview;
    }
}
