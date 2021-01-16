package com.mark.cyberpunkplayer.util;

import android.media.MediaMetadataRetriever;

import com.mark.cyberpunkplayer.bean.local.ShowVideoBean;

public class VideoHelper {

    public static ShowVideoBean getLocalVideoInfo(ShowVideoBean showVideoBean) {
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(showVideoBean.getPath());
            showVideoBean.setTime(Integer.parseInt(mmr.extractMetadata
                    (MediaMetadataRetriever.METADATA_KEY_DURATION)));
            showVideoBean.setWidth(Integer.parseInt(mmr.extractMetadata
                    (MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)));
            showVideoBean.setHeight(Integer.parseInt(mmr.extractMetadata
                    (MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)));
        } catch (Exception e) {
            e.printStackTrace();
            return showVideoBean;
        }
        return showVideoBean;
    }
}
