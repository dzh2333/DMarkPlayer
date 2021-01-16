package com.mark.cyberpunkplayer.ui.fragment.mvc;

import android.database.Cursor;
import android.provider.MediaStore;

import com.mark.cyberpunkplayer.app.Constants;
import com.mark.cyberpunkplayer.app.MainApplication;
import com.mark.cyberpunkplayer.bean.local.PictureBean;
import com.mark.cyberpunkplayer.util.AppExecutors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AllPicViewModel  {

    public static final int PIC_SORT_MODE_TIME = 0;
    public static final int PIC_SORT_MODE_SIZE = 1;

    public void getAllPicCallBack(final MVCCallBack<List<PictureBean>> callback, final int mode){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<PictureBean> res = new ArrayList<>();
                Cursor cursor = MainApplication.getInstance().getContentResolver()
                        .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    boolean addable = true;
                    for (int i = 0; i < Constants.FILTER_PATH.length; i++){
                        if (path.contains(Constants.FILTER_PATH[i])){
                            addable = false;
                        }
                    }
                    File file = new File(path);
                    if (file.exists() && addable){
                        res.add(new PictureBean(path, false, file.lastModified(), file.getTotalSpace()));
                    }
                }

                switch (mode){
                    case PIC_SORT_MODE_SIZE:
                        for(int i = 0; i < res.size();i++){
                            long max = 0;
                            int maxIndex = 0;
                            for(int j = i; j < res.size();j++){
                                if (max <= res.get(j).getSize()){
                                    max = res.get(j).getSize();
                                    maxIndex = j;
                                }
                            }
                            //如果 PictureBean tmp = res.get(i)会出现res全部为第一次max的值,需要实例化一个对象而不是指向一个List索引的值;
                            PictureBean tmp = new PictureBean(res.get(i).getPath(),
                                    res.get(i).isFocused(),
                                    res.get(i).getFileTime(),
                                    res.get(i).getSize());

                            res.get(i).setPath(res.get(maxIndex).getPath());
                            res.get(i).setFileTime(res.get(maxIndex).getFileTime());
                            res.get(i).setFocused(res.get(maxIndex).isFocused());

                            res.get(maxIndex).setFileTime(tmp.getFileTime());
                            res.get(maxIndex).setFocused(tmp.isFocused());
                            res.get(maxIndex).setPath(tmp.getPath());
                        }
                        break;
                    default:
                        for(int i = 0; i < res.size();i++){
                            long max = 0;
                            int maxIndex = 0;
                            for(int j = i; j < res.size();j++){
                                if (max <= res.get(j).getFileTime()){
                                    max = res.get(j).getFileTime();
                                    maxIndex = j;
                                }
                            }
                            //如果 PictureBean tmp = res.get(i)会出现res全部为第一次max的值,需要实例化一个对象而不是指向一个List索引的值;
                            PictureBean tmp = new PictureBean(res.get(i).getPath(),
                                    res.get(i).isFocused(),
                                    res.get(i).getFileTime(),
                                    res.get(i).getSize());

                            res.get(i).setPath(res.get(maxIndex).getPath());
                            res.get(i).setFileTime(res.get(maxIndex).getFileTime());
                            res.get(i).setFocused(res.get(maxIndex).isFocused());

                            res.get(maxIndex).setFileTime(tmp.getFileTime());
                            res.get(maxIndex).setFocused(tmp.isFocused());
                            res.get(maxIndex).setPath(tmp.getPath());
                        }
                        break;
                }
                callback.callback(res);
            }
        });
    }
}
