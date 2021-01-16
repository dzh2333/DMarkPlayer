package com.mark.cyberpunkplayer.event;

public class AppEvent {

    public AppEvent(String type, String data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AppEvent{" +
                "type='" + type + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    private String type;
    private String data;

}
