package com.example.administrator.filemanagementassistant.bean;

public class Historydata {
    private String title;
    private String image;

    public Historydata(String ti, String im) {

        title=ti;
        image=im;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
