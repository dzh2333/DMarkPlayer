package com.mark.cyberpunkplayer.ui.fragment.mvc;

import android.os.Environment;
import android.provider.MediaStore;

import com.mark.cyberpunkplayer.db.AudioBean;
import com.mark.cyberpunkplayer.util.AppExecutors;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class AllAudioViewModel {

    public static final int AUDIO_SORT_MODE_TIME = 0;
    public static final int AUDIO_SORT_MODE_SIZE = 1;
    private List<AudioBean> res;

    public void getAllAudioCallBack(final MVCCallBack<List<AudioBean>> callBack, final int sortMode){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                res = new ArrayList<>();
                List<AudioBean> tmpRes = getAllAudio(Environment.getExternalStorageDirectory());
                List<AudioBean> tmpRes2 = new ArrayList<>();
                for (int i = 0; i < tmpRes.size(); i++) {
                    int max = i;
                    for (int j = i; j < tmpRes.size(); j++) {
                        if (tmpRes.get(j).getLastTime() >= tmpRes.get(max).getLastTime()) {
                            max = j;
                        }
                    }

                    tmpRes2.add(tmpRes.get(max));
                    tmpRes.remove(max);
                }
                callBack.callback(tmpRes2);
            }
        });
    }

    private List<AudioBean> getAllAudio(File file) {
        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String name = file.getName();
                int i = name.indexOf('.');
                if (i != -1) {
                    name = name.substring(i);
                    if (name.equalsIgnoreCase(".mp3")) {
                        AudioBean bean = new AudioBean();
                        bean.setPlaying(false);
                        bean.setName(file.getName());
                        bean.setPath(file.getAbsolutePath());
                        bean.setLastTime(file.lastModified());
                        res.add(bean);
                    }
                    //判断是不是目录
                } else if (file.isDirectory()) {
                    getAllAudio(file);
                }
                return false;
            }
        });
        return res;
    }
}
