package com.mark.cyberpunkplayer.bean.network;

import java.io.File;

public class LocalNetResBean {

    public LocalNetResBean(boolean isDirectory, String path) {
        this.isDirectory = isDirectory;
        this.path = path;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "LoclNetResBean{" +
                "isDirectory=" + isDirectory +
                ", path='" + path + '\'' +
                '}';
    }

    private boolean isDirectory;
    private String path;

}
