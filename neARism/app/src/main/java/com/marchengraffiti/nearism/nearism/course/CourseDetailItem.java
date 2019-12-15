package com.marchengraffiti.nearism.nearism.course;

public class CourseDetailItem {
    String detailimg, subname, overview, cid, subid;

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

    public String getSubid() { return subid; }

    public CourseDetailItem(String detailimg, String subname, String overview, String cid, String subid) {
        this.detailimg = detailimg;
        this.subname = subname;
        this.overview = overview;
        this.cid = cid;
        this.subid = subid;
    }
}
