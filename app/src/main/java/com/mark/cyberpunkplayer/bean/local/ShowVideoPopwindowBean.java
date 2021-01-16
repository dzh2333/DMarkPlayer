package com.mark.cyberpunkplayer.bean.local;

import com.mark.cyberpunkplayer.ui.adapter.popwindow.PopwindowClickInterface;

public class ShowVideoPopwindowBean {

    public ShowVideoPopwindowBean(int id, String name, int rseId, String path, PopwindowClickInterface clickInterface) {
        this.id = id;
        this.name = name;
        this.rseId = rseId;
        this.path = path;
        this.clickInterface = clickInterface;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRseId() {
        return rseId;
    }

    public void setRseId(int rseId) {
        this.rseId = rseId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public PopwindowClickInterface getClickInterface() {
        return clickInterface;
    }

    public void setClickInterface(PopwindowClickInterface clickInterface) {
        this.clickInterface = clickInterface;
    }

    @Override
    public String toString() {
        return "ShowVideoPopwindowBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rseId=" + rseId +
                ", path='" + path + '\'' +
                ", clickInterface=" + clickInterface +
                '}';
    }

    private int id;
    private String name;
    private int rseId;
    private String path;
    private PopwindowClickInterface clickInterface;

}
