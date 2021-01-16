package com.mark.cyberpunkplayer.bean.local;

import android.graphics.drawable.Drawable;

public class ShowVideoBean {

    public ShowVideoBean(){

    }


    public ShowVideoBean(String name, String path, long size, long time, int width, int height, String rotation) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.time = time;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }

    @Override
    public String toString() {
        return "ShowVideoBean{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                ", time=" + time +
                ", width=" + width +
                ", height=" + height +
                ", rotation='" + rotation + '\'' +
                '}';
    }

    private String name;
    private String path;
    private long size;
    private long time;
    private int width;
    private int height;
    private String rotation;

}
