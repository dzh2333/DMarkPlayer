package com.mark.cyberpunkplayer.db;

import androidx.annotation.IntDef;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class AudioBean {

    @Id
    private String path;
    private String name;
    private String author;
    private long lastTime;
    private boolean playing;
    @Generated(hash = 1771078585)
    public AudioBean(String path, String name, String author, long lastTime,
            boolean playing) {
        this.path = path;
        this.name = name;
        this.author = author;
        this.lastTime = lastTime;
        this.playing = playing;
    }
    @Generated(hash = 1628963493)
    public AudioBean() {
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
    public String getAuthor() {
        return this.author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public long getLastTime() {
        return this.lastTime;
    }
    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
    public boolean getPlaying() {
        return this.playing;
    }
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
   
}
