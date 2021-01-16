package com.mark.cyberpunkplayer.util;

import com.orhanobut.logger.Logger;

public class FileCategoryUtils {

    public static final int FILE_UNKNOWN = 0;
    public static final int FILE_IS_VIDEO = 1;
    public static final int FILE_IS_AUDIO = 2;
    public static final int FILE_IS_PIC = 3;

    //常见视频格式
    public static final String[] videoFormats = {"mp4", "wmv", "avi", "rmvb", "flv"};
    //常见音频格式
    public static final String[] audioFormats = {"mp3", "mpeg", "wma", "aac"};
    //常见图片格式
    public static final String[] picFormats = {"jpg", "gif", "png"};

    /***
     * 返回文件类型 判断是视频还是图片还是其他文件
     * @param fileName
     * @return 0: 其他文件。可能是文件夹也可能是其他   1：视频文件   2:音频   3、图片
     */
    public static int getFileCategory(String fileName){
        String[] tmps = fileName.split("\\.");
        Logger.d("处理文件名" + fileName + "长度为" + tmps.length);
        if (tmps.length == 0 || tmps.length == 1){
            return FILE_UNKNOWN;
        }
        String format = tmps[tmps.length - 1];
        Logger.d("处理文件名" + fileName + "  格式为" + format);
        for (int i = 0; i < videoFormats.length; i++) {
            if (format.equals(videoFormats[i])){
                return FILE_IS_VIDEO;
            }
        }
        for (int i = 0; i < audioFormats.length; i++) {
            if (format.equals(audioFormats[i])){
                return FILE_IS_AUDIO;
            }
        }
        for (int i = 0; i < picFormats.length; i++) {
            if (format.equals(picFormats[i])){
                return FILE_IS_PIC;
            }
        }
        return FILE_UNKNOWN;
    }
}
