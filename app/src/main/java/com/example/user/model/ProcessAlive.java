package com.example.user.model;

import android.util.Log;

import com.example.user.bean.AppInfo;
import com.example.user.db.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/11/21.
 * 包活的临时集合
 */

public class ProcessAlive {
    public static ProcessAlive getProcessAlive() {
        return processAlive;
    }
    private static String TAG = "ProcessAlive" ;
    public static ProcessAlive processAlive = new ProcessAlive() ;
    private  DataBaseHelper dataBaseHelper ;
    public List<AppInfo> getAliveList() {
        return aliveList;
    }

    public List<AppInfo> aliveList = new ArrayList<>();
    //初始化白名单
    private ProcessAlive(){
         dataBaseHelper = DataBaseHelper.getDataBaseHelper() ;
         aliveList.addAll(dataBaseHelper.getAliveList() );
    }
    //判断是否是白名单中
    public boolean isAlive(AppInfo appInfo){
        if(aliveList.size() == 0) return false;
        boolean b = aliveList.contains(appInfo);
        return  b  ;
    }
    //解锁
    public void removeAlive(AppInfo appInfo){
        if(aliveList.contains(appInfo)){
            dataBaseHelper.deleteAlive(appInfo);
            aliveList.remove(appInfo);
            Log.i(TAG, "removeAlive: remove "+appInfo.getName());
        }
    }
    //加锁
    public void addAlive(AppInfo appInfo){
        dataBaseHelper.addAlive(appInfo);
        aliveList.add(appInfo) ;
        Log.i(TAG, "addAlive : add  "+appInfo.getName());

    }
}
