package com.mark.cyberpunkplayer.player;

import androidx.databinding.BaseObservable;

public class PlayerBean extends BaseObservable {

    public PlayerBean(String name, String url, long size, long time, int vWidth, int vHeight, int style) {
        this.name = name;
        this.url = url;
        this.size = size;
        this.time = time;
        this.vWidth = vWidth;
        this.vHeight = vHeight;
        this.style = style;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public int getvWidth() {
        return vWidth;
    }

    public void setvWidth(int vWidth) {
        this.vWidth = vWidth;
    }

    public int getvHeight() {
        return vHeight;
    }

    public void setvHeight(int vHeight) {
        this.vHeight = vHeight;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    private String name;
    private String url;
    private long size;
    private long time;
    private int vWidth;
    private int vHeight;
    private int style;

}
