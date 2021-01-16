package com.mark.cyberpunkplayer.bean.local;

import android.os.Environment;

import java.util.Arrays;

public class FolderBean {

    public FolderBean(String fileName, String absolutePath, long size, long createTime, boolean directory, String[] folderDirectory) {
        this.fileName = fileName;
        this.absolutePath = absolutePath;
        this.size = size;
        this.createTime = createTime;
        this.directory = directory;
        this.folderDirectory = folderDirectory;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public String[] getFolderDirectory() {
        return folderDirectory;
    }

    public void setFolderDirectory(String[] folderDirectory) {
        this.folderDirectory = folderDirectory;
    }

    @Override
    public String toString() {
        return "FolderBean{" +
                "fileName='" + fileName + '\'' +
                ", absolutePath='" + absolutePath + '\'' +
                ", size=" + size +
                ", createTime=" + createTime +
                ", directory=" + directory +
                ", folderDirectory=" + Arrays.toString(folderDirectory) +
                '}';
    }

    private String fileName;
    private String absolutePath;
    private long size;
    private long createTime;
    boolean directory;
    private String[] folderDirectory;
}
