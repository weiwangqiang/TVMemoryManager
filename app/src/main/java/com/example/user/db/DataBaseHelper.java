package com.example.user.db;

import android.content.Context;

import com.example.user.bean.AppInfoBean;

import java.util.List;

/**
 * Created by user on 2017/11/21.
 */

public class DataBaseHelper {
    private final static String dbName = "APP_INFO";
    private AppInfoHelper  mAppInfoHelper;

    public static DataBaseHelper getDataBaseHelper(Context mCtx) {
        if(mDataBaseHelper == null)
            mDataBaseHelper = new DataBaseHelper(mCtx) ;
        return mDataBaseHelper;
    }

    public static DataBaseHelper mDataBaseHelper  ;
    private  DataBaseHelper(Context mCtx){
        mAppInfoHelper = new AppInfoHelper(mCtx) ;
    }
    public void addAlive(AppInfoBean appInfoBean){
        mAppInfoHelper.insert(appInfoBean);
    }
    public void deleteAlive(AppInfoBean appInfoBean){
        //挨个删
        mAppInfoHelper.delete(appInfoBean);
    }
    public List<AppInfoBean> getAliveList(){
        //根据关键词获取一定有序的集合
         return mAppInfoHelper.selectAll() ;
    }
}
