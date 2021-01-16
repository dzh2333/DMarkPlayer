package com.mark.cyberpunkplayer.util;

import android.content.Context;
import android.media.AudioManager;

import com.mark.cyberpunkplayer.app.MainApplication;

import static android.media.AudioManager.ADJUST_LOWER;
import static android.media.AudioManager.ADJUST_RAISE;
import static android.media.AudioManager.FLAG_PLAY_SOUND;
import static android.media.AudioManager.FLAG_SHOW_UI;

public class VolumeUtil {

    private static final int aimVolumeType = AudioManager.STREAM_MUSIC;

    public static void addVolume(){
        AudioManager audioManager = (AudioManager) MainApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);
        int nowNum = audioManager.getStreamVolume(aimVolumeType);
        LogUtils.d("目前的音量" + nowNum );
        audioManager.setStreamVolume(aimVolumeType, nowNum, FLAG_SHOW_UI);
    }

    public static void reduceVolume(){
        AudioManager audioManager = (AudioManager) MainApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);
        int nowNum = audioManager.getStreamVolume(aimVolumeType);
        LogUtils.d("目前的音量" + nowNum);
        audioManager.setStreamVolume(aimVolumeType, nowNum - 1, FLAG_SHOW_UI);
    }
}
