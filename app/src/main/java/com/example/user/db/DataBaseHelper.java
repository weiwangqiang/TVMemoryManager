package com.example.user.db;

import android.database.sqlite.SQLiteDatabase;

import com.example.user.bean.AppInfo;
import com.example.user.dao.AppInfoDao;
import com.example.user.dao.DaoMaster;
import com.example.user.dao.DaoSession;
import com.example.user.core.BaseApplication;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by user on 2017/11/21.
 */

public class DataBaseHelper {
    private final static String dbName = "APP_INFO";
    private DaoMaster.DevOpenHelper openHelper;

    public static DataBaseHelper getDataBaseHelper() {
        return dataBaseHelper;
    }

    public static DataBaseHelper dataBaseHelper  = new DataBaseHelper() ;
    private  DataBaseHelper(){
    }
    public void addAlive(AppInfo appInfo){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        AppInfoDao userDao = daoSession.getAppInfoDao();
        userDao.insert(appInfo);
    }
    public void deleteAlive(AppInfo appInfo){
        //挨个删
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        AppInfoDao userDao = daoSession.getAppInfoDao();
        userDao.delete(appInfo);
    }
    public List<AppInfo> getAliveList(){
        //根据关键词获取一定有序的集合
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        AppInfoDao userDao = daoSession.getAppInfoDao();
        QueryBuilder<AppInfo> qb = userDao.queryBuilder();
        List<AppInfo> list = qb.list();
        return list;
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(
                    BaseApplication.getmCtx(), dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }
    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(
                    BaseApplication.getmCtx(), dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }
}
