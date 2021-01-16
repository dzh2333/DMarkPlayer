package com.mark.cyberpunkplayer.util.pic;

import android.graphics.Bitmap;
import android.os.MemoryFile;
import android.util.LruCache;

import com.bumptech.glide.disklrucache.DiskLruCache;

import java.lang.ref.SoftReference;
import java.util.HashMap;

public class MemoryCacheUtils extends AImageManager{

    private MemoryCacheUtils(){}

    public MemoryCacheUtils(ImageLoadConfig config){
        lruCache = new LruCache<String, Bitmap>((int) config.getMaxMemory()){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return (Bitmap) lruCache.get(url);
    }

    @Override
    public void setBitMap(String url, Bitmap bitmap) {
        lruCache.put(url, bitmap);
    }

}

