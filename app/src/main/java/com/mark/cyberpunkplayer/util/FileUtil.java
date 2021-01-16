package com.mark.cyberpunkplayer.util;

import android.os.Environment;

import java.io.File;

public class FileUtil {

    public static String getFileName(String fileName){
        String[] tmp = fileName.split("/");
        if (tmp.length >= 1){
            return tmp[tmp.length - 1];
        }
       return "";
    }

    public static String getLastPath(String fileName){
        String[] tmp = fileName.split("/");
        String res = "";
        for (int i = 0; i < tmp.length - 1; i++) {
            res = res + tmp[i] + "/";
        }
        return res;
    }

    public static void mkdir(String path){
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
    }
}
