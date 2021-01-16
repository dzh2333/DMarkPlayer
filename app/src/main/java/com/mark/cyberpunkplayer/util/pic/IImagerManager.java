package com.mark.cyberpunkplayer.util.pic;

import android.graphics.Bitmap;

public interface IImagerManager {

    Bitmap getBitmap(String url);

    void setBitMap(String url , Bitmap bitmap);
}
