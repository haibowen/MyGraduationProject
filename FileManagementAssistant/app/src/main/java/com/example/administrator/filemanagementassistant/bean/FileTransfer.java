package com.example.administrator.filemanagementassistant.bean;

import java.io.Serializable;

public class FileTransfer implements Serializable {

    private String filePath;
    private Long fileLength;
    private String md5;

    public FileTransfer(String path, long length) {

        this.filePath=path;
        this.fileLength=length;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileLength() {
        return fileLength;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    @Override
    public String toString() {

        return "FileTransfer{" +
                "filePath='" + filePath + '\'' +
                ", fileLength=" + fileLength +
                ", md5='" + md5 + '\'' +
                '}';
    }
}
