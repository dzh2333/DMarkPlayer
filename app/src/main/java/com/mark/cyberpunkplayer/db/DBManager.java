package com.mark.cyberpunkplayer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.mark.cyberpunkplayer.app.MainApplication;
import com.orhanobut.logger.Logger;

import java.util.List;

public class DBManager {
    private final static String dbName = "base_module_db";
    private DaoSession daoSession;
    private Context context;
    private DaoMaster daoMaster;
    private DaoMaster.DevOpenHelper openHelper;
    private volatile  static DBManager instance;
    private SQLiteDatabase db;

    private VideoBeanDao videoBeanDao;
    private SmbBeanDao smbBeanDao;

    public static DBManager getInstance(){
        if (instance == null){
            synchronized (DBManager.class){
                if (instance == null){
                    instance = new DBManager();
                    instance.init(MainApplication.getInstance());
                }
            }
        }
        return instance;
    }

    public DBManager init(Context context){
        setDateBase(context);
        return this;
    }

    /**
     * 初始化数据库
     * @param context
     */
    private void setDateBase(Context context){
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        db = openHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        videoBeanDao = daoSession.getVideoBeanDao();
        smbBeanDao = daoSession.getSmbBeanDao();

    }

    public DaoSession getDaoSession(){
        return daoSession;
    }

    public SQLiteDatabase getDb(){
        return db;
    }

    //************************************* Video DAO *******************************************

    public List<VideoBean> getAllVideoBean(){
        return videoBeanDao.queryBuilder().list();
    }

    public void addVideoBean(List<VideoBean> inputs){
        videoBeanDao.insertInTx(inputs);
    }

    public void removeVideoBeans(List<VideoBean> inputs){
        videoBeanDao.deleteInTx(inputs);
    }

    public void updateVideoBean(List<VideoBean> inputs){
        videoBeanDao.updateInTx(inputs);
    }


    //************************************* SmbSession DAO *************************************

    public List<SmbBean> getAllSmbBean(){
        return smbBeanDao.queryBuilder().list();
    }

    public void addSmbBean(SmbBean input){
        smbBeanDao.insert(input);
    }

    public void removeSmbBeans(SmbBean input){
        smbBeanDao.delete(input);
    }

    public void updateSmbBean(SmbBean input){
        smbBeanDao.update(input);
    }


}
