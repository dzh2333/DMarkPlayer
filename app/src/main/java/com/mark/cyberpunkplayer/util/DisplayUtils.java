package com.mark.cyberpunkplayer.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.mark.cyberpunkplayer.app.MainApplication;


public class DisplayUtils {

    public static int getWidthDp(){
        WindowManager wm = (WindowManager) MainApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        return screenWidth;
    }

    /**
     * 获得设备DPI
     * @return
     */
    public static String getDpi(){
        WindowManager wm = (WindowManager) MainApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return String.valueOf(dm.densityDpi);
    }

    /**
     * 获得分辨率
     * @return
     */
    public static String getResolution(){
        WindowManager wm = (WindowManager) MainApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return "" + dm.heightPixels + "x" + dm.widthPixels;
    }

    public static int  getWidthPixels(){
        WindowManager wm = (WindowManager) MainApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return  dm.widthPixels;
    }

    public static int  getHeightPixels(){
        WindowManager wm = (WindowManager) MainApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return  dm.heightPixels;
    }

    public void getAndroiodScreenProperty() {
        WindowManager wm = (WindowManager) MainApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)
    }
}
