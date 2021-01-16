package com.mark.cyberpunkplayer.event;

public class BTMsgEvent {

    public static final int FILE_TRANSPORT_FINISH = 1;

    public BTMsgEvent(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BTMsgEvent{" +
                "type=" + type +
                ", msg='" + msg + '\'' +
                '}';
    }

    private int type;
    private String msg;


}
