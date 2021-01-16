package com.mark.cyberpunkplayer.bean.local;

public class HomeBean {

    public HomeBean(int index, Class cls) {
        this.index = index;
        this.cls = cls;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }

    @Override
    public String toString() {
        return "HomeBean{" +
                "index=" + index +
                ", cls=" + cls +
                '}';
    }

    private int index;
    private Class cls;


}
