package com.example.administrator.jubangbangtest2.data;

public class OrderDetail {

    private int imageId;
    private String text01;
    private String text02;

    public OrderDetail(int imageId, String text01, String text02) {
        this.imageId = imageId;
        this.text01 = text01;
        this.text02 = text02;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getText01() {
        return text01;
    }

    public void setText01(String text01) {
        this.text01 = text01;
    }

    public String getText02() {
        return text02;
    }

    public void setText02(String text02) {
        this.text02 = text02;
    }
}
