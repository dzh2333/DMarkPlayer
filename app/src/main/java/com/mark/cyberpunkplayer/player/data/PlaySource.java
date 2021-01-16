package com.mark.cyberpunkplayer.player.data;

import android.media.session.PlaybackState;

import com.mark.cyberpunkplayer.player.PlayerBean;

import java.util.List;

public class PlaySource {

    private static PlaySource insatnce;

    private List<PlayerBean> playerBeans;
    private int playIndex;

    private PlaySource(){

    }

    public static PlaySource getInstance(){
        if (insatnce == null){
            synchronized (PlaySource.class){
                if (insatnce == null){
                    insatnce = new PlaySource();
                }
            }
        }
        return insatnce;
    }

    public void setPlayIndex(int index){
        this.playIndex = index;
    }

    public PlayerBean getPlayBean(){
        if (playIndex >= playerBeans.size()){
            return null;
        }
        return playerBeans.get(playIndex);
    }


    public void setPlayerBeans(List<PlayerBean> playerBeans) {
        this.playerBeans = playerBeans;
    }

}
