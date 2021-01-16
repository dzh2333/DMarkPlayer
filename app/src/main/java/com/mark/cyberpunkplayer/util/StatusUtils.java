package com.mark.cyberpunkplayer.util;

import android.appwidget.AppWidgetManager;
import android.util.Log;
import android.view.WindowManager;

import com.mark.cyberpunkplayer.app.AppManager;

public class StatusUtils {

    public static void setNowActivityStatusHideOrShare(boolean show){
        Log.e("SSS", "setNowActivityStatusHideOrShare: " + show);
        if (show){
            WindowManager.LayoutParams attrs = AppManager.getActivity().getWindow().getAttributes();
            AppManager.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            AppManager.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            AppManager.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            AppManager.getActivity().getWindow().setAttributes(attrs);
        }else {
            WindowManager.LayoutParams attrs = AppManager.getActivity().getWindow().getAttributes();
            AppManager.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            AppManager.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            AppManager.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            AppManager.getActivity().getWindow().setAttributes(attrs);
        }
    }
}
