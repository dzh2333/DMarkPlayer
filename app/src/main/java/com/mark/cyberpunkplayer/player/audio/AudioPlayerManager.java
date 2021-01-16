package com.mark.cyberpunkplayer.player.audio;

import com.mark.cyberpunkplayer.event.PlayerEvent;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

public class AudioPlayerManager {

    /**
     * JNI调用改变Seek进度
     * @param seek
     */
    public static void changeSeek(double seek){
        if (seek == 0){
            return;
        }
        Logger.d("音频进度" + seek);
        EventBus.getDefault().post(new PlayerEvent(PlayerEvent.PLAY_AUDIO_SEEK_CHANGE, "", seek  * 10));
    }



    public static void playOver(){
        EventBus.getDefault().post(new PlayerEvent(PlayerEvent.PLAY_AUDIO_OVER, "", 0));
    }

}
