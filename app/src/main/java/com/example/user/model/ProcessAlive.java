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
    private static String TAG = "ProcessAlive";
    public static ProcessAlive mProcessAlive = new ProcessAlive();
    private DataBaseHelper mDataBaseHelper;

    public List<AppInfo> getAliveList() {
        return mAiveList;
    }

    public List<AppInfo> mAiveList = new ArrayList<>();

    public static ProcessAlive getProcessAlive() {
        return mProcessAlive;
    }

    //初始化白名单
    private ProcessAlive() {
        mDataBaseHelper = DataBaseHelper.getDataBaseHelper();
        mAiveList.addAll(mDataBaseHelper.getAliveList());
    }

    //判断是否是白名单中
    public boolean isAlive(AppInfo appInfo) {
        if (mAiveList.size() == 0) return false;
        boolean b = mAiveList.contains(appInfo);
        return b;
    }

    //解锁
    public void removeAlive(AppInfo appInfo) {
        if (mAiveList.contains(appInfo)) {
            mDataBaseHelper.deleteAlive(appInfo);
            mAiveList.remove(appInfo);
            Log.i(TAG, "removeAlive: remove " + appInfo.getName());
        }
    }

    //加锁
    public void addAlive(AppInfo appInfo) {
        mDataBaseHelper.addAlive(appInfo);
        mAiveList.add(appInfo);
        Log.i(TAG, "addAlive : add  " + appInfo.getName());

    }
}
