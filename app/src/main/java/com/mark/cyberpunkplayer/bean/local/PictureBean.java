package com.mark.cyberpunkplayer.bean.local;

public class PictureBean {

    public PictureBean(String path, boolean focused, long fileTime, long size) {
        this.path = path;
        this.focused = focused;
        this.fileTime = fileTime;
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public long getFileTime() {
        return fileTime;
    }

    public void setFileTime(long fileTime) {
        this.fileTime = fileTime;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PictureBean{" +
                "path='" + path + '\'' +
                ", focused=" + focused +
                ", fileTime=" + fileTime +
                ", size=" + size +
                '}';
    }

    private String path;
    private boolean focused;
    private long fileTime;
    private long size;
}
