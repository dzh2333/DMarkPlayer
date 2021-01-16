package com.mark.cyberpunkplayer.player;

public interface PlayerViewCallBack {

    public void startVideo(String url);

    public void onStart();

    public void onPause();

    public void changeSeek(double index);

    public void onDestory();

    public void changeSpeed(double speed);

    //切换图片比例
    public void changSize(int mode);

    public void onError();
}
