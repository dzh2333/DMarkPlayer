package com.mark.cyberpunkplayer.db;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class VideoBean {

    @Id
    @Unique
    private String path;
    private String name;
    private long size;
    private long time;
    @Generated(hash = 120757156)
    public VideoBean(String path, String name, long size, long time) {
        this.path = path;
        this.name = name;
        this.size = size;
        this.time = time;
    }
    @Generated(hash = 2024490299)
    public VideoBean() {
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
    public long getSize() {
        return this.size;
    }
    public void setSize(long size) {
        this.size = size;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }


}
