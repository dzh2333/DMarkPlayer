package com.mark.cyberpunkplayer.util;

import android.util.Log;

import com.orhanobut.logger.Logger;

import static com.mark.cyberpunkplayer.app.Constants.APPDEBUG;

public class LogUtils {
    private static final String TAG = "DMarkCodeHelp LOG";

    public static void i(String content){
        if (APPDEBUG){
            Log.d(TAG, content);
        }
    }

    public static void e(String content){
        if (APPDEBUG){
            Log.e(TAG, content );
        }
    }

    public static void d(String content){
        Logger.d(content);
    }

    public static void w(String content){
        if (APPDEBUG){
            Log.w(TAG, content );
        }
    }

}
