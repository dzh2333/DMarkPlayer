package com.mark.cyberpunkplayer.event;

import com.mark.cyberpunkplayer.db.SmbBean;

public class SmbSessionEvent {

    public static final int OPEN_DIRECTORY = 0;
    public static final int OPEN_FILE = 1;
    public static final int DELETE_FILE = 2;
    public static final int MOVE_FILE = 3;
    public static final int SHARE_FILE = 4;
    public static final int OPEN_SESSION = 5;
    public static final int BACK_TO_UP_LEVEL = 6;
    public static final int SUCCESS_SESSION = 10;
    public static final int FAIL_SESSION = 11;
    public static final int DELETE_SESSION = 12;

    private int type;
    private SmbBean bean;


    public SmbSessionEvent(int type, SmbBean bean) {
        this.type = type;
        this.bean = bean;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public SmbBean getBean() {
        return bean;
    }

    public void setBean(SmbBean bean) {
        this.bean = bean;
    }

    @Override
    public String toString() {
        return "SmbSessionEvent{" +
                "type=" + type +
                ", bean=" + bean +
                '}';
    }

}
