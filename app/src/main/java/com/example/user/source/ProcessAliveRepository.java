package com.example.user.source;

import com.example.user.bean.AppInfoBean;
import com.example.user.core.BaseApplication;
import com.example.user.db.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/11/21.
 * 处理保活工作的厂库
 */

public class ProcessAliveRepository {
    private static String TAG = "ProcessAliveRepository";
    public static ProcessAliveRepository mProcessAliveRepository = new ProcessAliveRepository();
    private DataBaseHelper mDataBaseHelper;

    public List<AppInfoBean> getAliveList() {
        return mAliveList;
    }

    public List<AppInfoBean> mAliveList = new ArrayList<>();

    public static ProcessAliveRepository getProcessAlive() {
        return mProcessAliveRepository;
    }

    //初始化白名单
    private ProcessAliveRepository() {
        mDataBaseHelper = DataBaseHelper.getDataBaseHelper(BaseApplication.getmCtx());
        mAliveList.addAll(mDataBaseHelper.getAliveList());
    }

    //判断是否是白名单中
    public boolean isAlive(AppInfoBean appInfoBean) {
        if (mAliveList.size() == 0) return false;
        boolean b = mAliveList.contains(appInfoBean);
        return b;
    }

    //解锁
    public void removeAlive(AppInfoBean appInfoBean) {
        if (mAliveList.contains(appInfoBean)) {
            mDataBaseHelper.deleteAlive(appInfoBean);
            mAliveList.remove(appInfoBean);
        }
    }

    //加锁
    public void addAlive(AppInfoBean appInfoBean) {
        mDataBaseHelper.addAlive(appInfoBean);
        mAliveList.add(appInfoBean);
    }
}
