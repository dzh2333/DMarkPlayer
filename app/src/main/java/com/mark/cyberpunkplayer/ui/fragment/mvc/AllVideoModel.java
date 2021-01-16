package com.mark.cyberpunkplayer.ui.fragment.mvc;

import android.os.Environment;

import com.mark.cyberpunkplayer.bean.local.ShowVideoBean;
import com.mark.cyberpunkplayer.util.AppExecutors;
import com.mark.cyberpunkplayer.util.VideoHelper;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class AllVideoModel  {

    public static final int SORT_TIME = 0;
    public static final int SORT_SIZE = 1;
    private List<ShowVideoBean> res;

    public void getAllVideoCallBack(final MVCCallBack<List<ShowVideoBean>> callback, final int sortMode){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                res = new ArrayList<>();
                List<ShowVideoBean> tmpRes = getVideoFile(Environment.getExternalStorageDirectory());
                List<ShowVideoBean> tmpRes2 = new ArrayList<>();
                switch (sortMode){
                    case SORT_SIZE:
                       for (int i = 0; i < tmpRes.size(); i++) {
                            int max = i;
                            for (int j = i; j < tmpRes.size(); j++) {
                                if (tmpRes.get(j).getSize() >= tmpRes.get(max).getSize()){
                                    max = j;
                                }
                            }

                            tmpRes2.add(tmpRes.get(max));
                            tmpRes.remove(max);
                        }
                        break;
                    default:
                        for (int i = 0; i < tmpRes.size(); i++) {
                            int max = i;
                            for (int j = i; j < tmpRes.size(); j++) {
                                if (tmpRes.get(j).getTime() >= tmpRes.get(max).getTime()){
                                    max = j;
                                }
                            }

                            tmpRes2.add(tmpRes.get(max));
                            tmpRes.remove(max);
                        }
                        break;
                }
                callback.callback(tmpRes2);
            }
        });
    }

    /**
     * 获取视频文件
     * @param file
     * @return
     */
    private List<ShowVideoBean> getVideoFile(File file) {
        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String name = file.getName();
                int i = name.indexOf('.');
                if (i != -1) {
                    name = name.substring(i);
                    if (name.equalsIgnoreCase(".mp4")
                            || name.equalsIgnoreCase(".wmv")
                            || name.equalsIgnoreCase(".rmvb")
                            || name.equalsIgnoreCase(".m4v")
                            || name.equalsIgnoreCase(".avi")) {
                        ShowVideoBean video = new ShowVideoBean();
                        video.setName(file.getName());
                        video.setPath(file.getAbsolutePath());
                        video.setSize(file.length());
                        video.setTime(file.lastModified());
                        if (file.length() >= 1024 * 40 * 1024){
                            res.add(video);
                        }
                    }
                    //判断是不是目录
                } else if (file.isDirectory()) {
                    getVideoFile(file);
                }
                return false;
            }
        });
        return res;
    }

    public void getVideoTime(final MVCCallBack<List<ShowVideoBean>> callBack, final List<ShowVideoBean> input){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<ShowVideoBean> res = input;
                for (int i = 0; i < res.size(); i++) {
                    res.get(i).setTime(VideoHelper.getLocalVideoInfo(res.get(i)).getTime());
                }
                callBack.callback(res);
            }
        });
    }
}
