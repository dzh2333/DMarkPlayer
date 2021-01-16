package com.mark.cyberpunkplayer.event;

public class SeekEvent {

    public static final int VIDEO_PLAYER_SEEK = 1;
    public static final int AUDIO_PLAYER_SEEK = 2;

    public SeekEvent(int type, double seekProgress) {
        this.type = type;
        this.seekProgress = seekProgress;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getSeekProgress() {
        return seekProgress;
    }

    public void setSeekProgress(double seekProgress) {
        this.seekProgress = seekProgress;
    }

    private int type;
    private double seekProgress;

}
