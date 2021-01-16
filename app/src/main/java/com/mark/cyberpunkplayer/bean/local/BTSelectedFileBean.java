package com.mark.cyberpunkplayer.bean.local;

public class BTSelectedFileBean {

    public BTSelectedFileBean(String fileName, String path, boolean isDirectory) {
        this.fileName = fileName;
        this.path = path;
        this.isDirectory = isDirectory;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    @Override
    public String toString() {
        return "BTSelectedFileBean{" +
                "fileName='" + fileName + '\'' +
                ", path='" + path + '\'' +
                ", isDirectory=" + isDirectory +
                '}';
    }

    private String fileName;
    private String path;
    private boolean isDirectory;

}
