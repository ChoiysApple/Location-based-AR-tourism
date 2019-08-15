package com.marchengraffiti.nearism.nearism;

public class Item {
    int image, add;
    String title, detail;

    int getImage() {
        return this.image;
    }
    int getAdd() { return this.add; }
    String getTitle() {
        return this.title;
    }
    String getDetail() {
        return this.detail;
    }

    Item(int image, int add, String title, String detail) {
        this.image = image;
        this.title = title;
        this.detail = detail;
        this.add = add;
    }
}