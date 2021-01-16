package com.mark.cyberpunkplayer.ui.fragment.mvc;

import com.hierynomus.msfscc.fileinformation.FileAllInformation;
import com.hierynomus.msfscc.fileinformation.FileDirectoryQueryableInformation;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.mark.cyberpunkplayer.base.BaseCallBack;
import com.mark.cyberpunkplayer.base.BaseListCallBack;
import com.mark.cyberpunkplayer.db.SmbBean;
import com.mark.cyberpunkplayer.service.smb.SmbEXThread;
import com.mark.cyberpunkplayer.util.AppExecutors;
import com.mark.cyberpunkplayer.util.LogUtils;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SmbSessionModel {

    private static final String a_z = "abcdefghijklmnopqrstuvwsyz";

    /**
     * 判断是否可以连接,返回可连接的磁盘
     * @param bean
     * @param callBack
     */
    public void openSession(final SmbBean bean, final BaseCallBack<List<SmbBean>> callBack){
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                SMBClient client = new SMBClient();
                List<SmbBean> res = new ArrayList<>();
                for (int i = 0; i < a_z.length(); i++) {
                    try (Connection connection = client.connect(bean.getHostName())) {
                        AuthenticationContext ac = new AuthenticationContext(bean.getUserName(),
                                bean.getPassword().toCharArray(),
                                "DOMAIN");
                        Session session = connection.authenticate(ac);
                        DiskShare share = (DiskShare) session.connectShare(String.valueOf(a_z.charAt(i)));
                        LogUtils.d(" share folder File size: " + share.list("").size());
                        SmbBean s1 = new SmbBean();
                        s1.setIsDirectory(true);
                        s1.setFileName(bean.getFileName());
                        s1.setMarkName(bean.getMarkName());
                        s1.setFolderPath("");
                        s1.setPort(bean.getPort());
                        s1.setUserName(bean.getUserName());
                        s1.setPassword(bean.getPassword());
                        s1.setHostName(bean.getHostName());
                        s1.setSessionId(bean.getSessionId());
                        s1.setDiskPath(String.valueOf(a_z.charAt(i)));
                        s1.setShowLevel(1);

                        res.add(s1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.d(" share folder File error : " + e);
                    }
                }
                callBack.callBack(res);
            }
        });
    }

    /**
     * 获得bean里的文件列表
     * @param bean
     * @param callBack
     */
    public void openFile(final SmbBean bean, final BaseListCallBack<SmbBean> callBack){
        Logger.d("点击  当前层级  FP" + bean.getFolderPath());
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                SMBClient client = new SMBClient();
                List<SmbBean> res = new ArrayList<>();

                try (Connection connection = client.connect(bean.getHostName())) {
                    AuthenticationContext ac = new AuthenticationContext(bean.getUserName(),
                            bean.getPassword().toCharArray(),
                            "DOMAIN");
                    Session session = connection.authenticate(ac);

                    DiskShare diskShare = (DiskShare) session.connectShare(bean.getDiskPath());

                    List<FileIdBothDirectoryInformation> tmpList = diskShare.list(bean.getFolderPath());
                    for (int i = 0; i < tmpList.size(); i++) {
                        if (tmpList.get(i).getFileName().equals(".")
                                || tmpList.get(i).getFileName().equals("..")){
                            continue;
                        }
                        FileAllInformation allInfo = diskShare.getFileInformation(bean.getFolderPath() + tmpList.get(i).getFileName());
                        SmbBean addBean = new SmbBean();
                        addBean.setDiskPath(bean.getDiskPath());
                        addBean.setHostName(bean.getHostName());
                        addBean.setUserName(bean.getUserName());
                        addBean.setPassword(bean.getPassword());
                        addBean.setPort(bean.getPort());
                        addBean.setSessionId(bean.getSessionId());
                        if (allInfo.getStandardInformation().isDirectory()){
                            addBean.setIsDirectory(true);
                            if (bean.getFolderPath() == null ||
                                    bean.getFolderPath().equals("") ||
                                    bean.getFolderPath().equals("null")){
                                addBean.setFolderPath(tmpList.get(i).getFileName() + "\\");
                            }else {
                                addBean.setFolderPath(bean.getFolderPath()  + tmpList.get(i).getFileName() + "\\");
                            }
                        }else {
                            addBean.setIsDirectory(false);
                            addBean.setFolderPath(bean.getFolderPath());
                        }
                        addBean.setFileName(tmpList.get(i).getFileName());
                        addBean.setShowLevel(2);
                        addBean.setSessionId(bean.getSessionId());
                        addBean.setMarkName(bean.getMarkName());

                        res.add(addBean);
                        Logger.d("返回的FilePath为" + addBean.getFolderPath());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.d(" share folders File error : " + e);
                }
                callBack.callBacks(res);
            }
        });
    }

}
