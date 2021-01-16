package com.mark.cyberpunkplayer.util.pic;

public class ImageLoadConfig {

    public ImageLoadConfig(String url, long maxMemory) {
        this.url = url;
        this.maxMemory = maxMemory;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(long maxMemory) {
        this.maxMemory = maxMemory;
    }

    private String url;
    private long maxMemory;


}
