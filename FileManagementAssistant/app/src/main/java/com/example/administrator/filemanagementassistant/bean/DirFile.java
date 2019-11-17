package com.example.administrator.filemanagementassistant.bean;

import java.io.Serializable;

public class DirFile implements Serializable {

    private String name;
    public DirFile(String s) {
        name=s;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
