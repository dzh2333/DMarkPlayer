package com.mark.cyberpunkplayer.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class PicBean {

    @Unique
    @Id
    private String path;
    private String name;
    private int format;
    private long size;
    private int width;
    private int height;
    @Generated(hash = 1925312775)
    public PicBean(String path, String name, int format, long size, int width,
            int height) {
        this.path = path;
        this.name = name;
        this.format = format;
        this.size = size;
        this.width = width;
        this.height = height;
    }
    @Generated(hash = 461998603)
    public PicBean() {
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getFormat() {
        return this.format;
    }
    public void setFormat(int format) {
        this.format = format;
    }
    public long getSize() {
        return this.size;
    }
    public void setSize(long size) {
        this.size = size;
    }
    public int getWidth() {
        return this.width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return this.height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

}
