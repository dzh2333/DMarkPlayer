package com.mark.cyberpunkplayer.event;

public class FileEvent {

    public static final int SELECTED_FILE = 1;
    public static final int SELECT_FILE_PRE = 10;
    public static final int OPEN_DIRECTORY = 20;

    public FileEvent(int type, String extraData) {
        this.type = type;
        this.extraData = extraData;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    @Override
    public String toString() {
        return "FileEvent{" +
                "type=" + type +
                ", extraData='" + extraData + '\'' +
                '}';
    }

    private int type;
    private String extraData;
}
