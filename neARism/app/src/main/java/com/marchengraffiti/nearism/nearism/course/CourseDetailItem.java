package com.marchengraffiti.nearism.nearism.course;

public class CourseDetailItem {
    String detailimg, subname, overview, cid;

    public String getDetailimg() {
        return detailimg;
    }

    public String getSubname() {
        return subname;
    }

    public String getOverview() {
        return overview;
    }

    public String getCid() {
        return cid;
    }

    public CourseDetailItem(String detailimg, String subname, String overview, String cid) {
        this.detailimg = detailimg;
        this.subname = subname;
        this.overview = overview;
        this.cid = cid;
    }
}
