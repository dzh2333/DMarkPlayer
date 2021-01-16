package com.mark.cyberpunkplayer.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

import com.mark.cyberpunkplayer.app.AppManager;

import static com.mark.cyberpunkplayer.app.AppManager.getActivity;

public class PhoneLightUtil {

    public static int setLight(boolean addAction, int nowLight){
        int brightness = nowLight;
        LogUtils.d("亮度为" + brightness);

        if (addAction){
            brightness += 4;
        }else {
            brightness -= 4;
        }
        if (brightness < 0){
            brightness = 0;
        }
        if (brightness > 255){
            brightness = 255;
        }

        Window window = AppManager.getActivity().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255.0f;
        window.setAttributes(lp);
        return brightness;
    }

}
