package com.mark.cyberpunkplayer.service.smb;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class SmbService extends Service {

    private SmbEXThread mSmbThread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mSmbThread == null){
            mSmbThread = new SmbEXThread();
            mSmbThread.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSmbThread.removeAllListener();
    }

}
