package com.example.administrator.filemanagementassistant.bean;

public class Historydata {
    private String title;
    private String image;
    private String content;
    public Historydata(String ti, String im,String co) {

        title=ti;
        image=im;
        content=co;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
