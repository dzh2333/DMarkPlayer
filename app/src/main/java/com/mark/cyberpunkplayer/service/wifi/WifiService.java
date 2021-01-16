package com.mark.cyberpunkplayer.service.wifi;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.orhanobut.logger.Logger;

public class WifiService extends Service {

    private WifiHttpThread wifiHttpThread;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (wifiHttpThread == null){
            Logger.d("创建了  WifiXXXXXXXXService");
            wifiHttpThread = new WifiHttpThread();
            wifiHttpThread.start();
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("结束了  WifiXXXXXXXXService");
        wifiHttpThread.removeAllListener();
    }
}
