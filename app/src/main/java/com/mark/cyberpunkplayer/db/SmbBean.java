package com.mark.cyberpunkplayer.db;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SmbBean {

    @Id
    private long sessionId;
    private String markName;
    private String hostName;
    private String userName;
    private String password;
    //磁盘
    private String diskPath;
    //文件夹
    private String folderPath;
    //文件名
    private String fileName;
    //是否是文件夹
    private boolean isDirectory;
    private int port;
    private int showLevel;
    @Generated(hash = 1694532515)
    public SmbBean(long sessionId, String markName, String hostName,
            String userName, String password, String diskPath, String folderPath,
            String fileName, boolean isDirectory, int port, int showLevel) {
        this.sessionId = sessionId;
        this.markName = markName;
        this.hostName = hostName;
        this.userName = userName;
        this.password = password;
        this.diskPath = diskPath;
        this.folderPath = folderPath;
        this.fileName = fileName;
        this.isDirectory = isDirectory;
        this.port = port;
        this.showLevel = showLevel;
    }
    @Generated(hash = 1840099767)
    public SmbBean() {
    }
    public long getSessionId() {
        return this.sessionId;
    }
    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }
    public String getMarkName() {
        return this.markName;
    }
    public void setMarkName(String markName) {
        this.markName = markName;
    }
    public String getHostName() {
        return this.hostName;
    }
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getDiskPath() {
        return this.diskPath;
    }
    public void setDiskPath(String diskPath) {
        this.diskPath = diskPath;
    }
    public String getFolderPath() {
        return this.folderPath;
    }
    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }
    public String getFileName() {
        return this.fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public boolean getIsDirectory() {
        return this.isDirectory;
    }
    public void setIsDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }
    public int getPort() {
        return this.port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public int getShowLevel() {
        return this.showLevel;
    }
    public void setShowLevel(int showLevel) {
        this.showLevel = showLevel;
    }


}
