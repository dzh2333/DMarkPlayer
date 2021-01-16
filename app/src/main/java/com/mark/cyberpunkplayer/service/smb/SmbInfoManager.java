package com.mark.cyberpunkplayer.service.smb;

import com.mark.cyberpunkplayer.db.SmbBean;

public class SmbInfoManager {

    private static SmbInfoManager instance;

    private SmbInfoManager(){

    }

    private SmbBean tmpBean;

    public static SmbInfoManager getInstance(){
        if (instance == null){
            synchronized (SmbInfoManager.class){
                if (instance == null){
                    instance = new SmbInfoManager();
                }
            }
        }
        return instance;
    }

    public SmbBean getTmpBean() {
        return tmpBean;
    }

    public void setTmpBean(SmbBean tmpBean) {
        this.tmpBean = tmpBean;
    }

}
