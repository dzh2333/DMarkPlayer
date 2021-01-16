package com.mark.cyberpunkplayer.app;

import android.app.Activity;

public class AppManager {

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        AppManager.activity = activity;
    }

    private static Activity activity;


}
