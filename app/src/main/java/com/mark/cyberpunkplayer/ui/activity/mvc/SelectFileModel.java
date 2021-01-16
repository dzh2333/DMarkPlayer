package com.mark.cyberpunkplayer.ui.activity.mvc;

import com.mark.cyberpunkplayer.bean.local.BTSelectedFileBean;
import com.mark.cyberpunkplayer.event.FileEvent;
import com.mark.cyberpunkplayer.ui.fragment.mvc.MVCCallBack;
import com.mark.cyberpunkplayer.util.AppExecutors;
import com.mark.cyberpunkplayer.util.FileUtil;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SelectFileModel {

    public void getFileList(final MVCCallBack<List<BTSelectedFileBean>> callBack, final String path){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<BTSelectedFileBean> res = new ArrayList<BTSelectedFileBean>();
                File file = new File(path);
                if (file.exists()){
                    if (file.isDirectory()){
                        String[] list = file.list();
                        for (int i = 0; i < list.length; i++) {
                            File file1 = new File(file.getPath().toString() + "/" + list[i]);
                            if (file1.exists()){
                                if (file1.isDirectory()){
                                    res.add(new BTSelectedFileBean(FileUtil.getFileName(list[i]), file.getPath().toString() + "/" + list[i], true));
                                }else {
                                    res.add(new BTSelectedFileBean(FileUtil.getFileName(list[i]), file.getPath().toString() + "/" + list[i], false));
                                }
                            }
                        }
                        callBack.callback(res);
                    }else {
                        EventBus.getDefault().post(new FileEvent(FileEvent.SELECTED_FILE, ""));
                    }
                }

            }
        });
    }
}
