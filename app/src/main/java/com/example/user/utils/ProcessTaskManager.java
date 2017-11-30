package com.example.user.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.example.user.bean.AppInfoBean;
import com.example.user.bean.PackagesInfo;
import com.example.user.source.ProcessAliveRepository;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by user on 2017/11/20.
 * 任务管理
 */

public class ProcessTaskManager {

    private static String TAG = "ProcessTaskManager " ;
    /**
     *     获取后台应用列表
     */
    public static List<AppInfoBean> getAppInfoList(Context mCtx){
        PackagesInfo pi =  PackagesInfo.getPackagesInfo(mCtx);
        ProcessAliveRepository processAliveRepository =  ProcessAliveRepository.getProcessAlive() ;
        ActivityManager mActivityManager = (ActivityManager)
                mCtx.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runList =
                mActivityManager.getRunningAppProcesses();
        PackageManager pm = mCtx.getPackageManager();
       List<AppInfoBean> mList = new ArrayList<>();
        for (ActivityManager.RunningAppProcessInfo ra : runList) {
            ApplicationInfo applicationInfo = pi.getInfo(ra.processName);
            if (applicationInfo == null || ra.processName.equals
                    (mCtx.getPackageName())) {
                continue;
            }

            AppInfoBean appInfoBean = new AppInfoBean();
            if(isSystemPid(applicationInfo)){
                appInfoBean.setLock(true);
                appInfoBean.setSystem(true);
            }
            appInfoBean.setPackageName(applicationInfo.packageName);
            appInfoBean.setProcessName(applicationInfo.loadLabel(pm).toString());
            appInfoBean.setLock(processAliveRepository.isAlive(appInfoBean));
            mList.add(appInfoBean);
        }
        return mList ;
    }
    /**
     * 杀进程(非白名单内的进程)
     * @param mCtx 上下文
     * @param packageName 包名
     * @return  杀除成功
     */
    public static boolean killProcess(Context mCtx, String packageName) {
        PackagesInfo pi =  PackagesInfo.getPackagesInfo(mCtx);
        ActivityManager mActivityManager = (ActivityManager) mCtx
                .getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runList =
                mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runServiceInfo : runList) {
            ApplicationInfo applicationInfo = pi.getInfo(runServiceInfo.processName);
            if (applicationInfo == null || runServiceInfo.processName.equals("system")
                    || runServiceInfo.processName.equals
                    (mCtx.getPackageName())) {
                continue;
            }
            String pkgName = applicationInfo.packageName; // 包名
            if (pkgName.equals(packageName)) {
                mActivityManager.killBackgroundProcesses(packageName);
                return true ;
            }

        }
        return false ;
    }

    /**
     * 判断是否是系统进程
     * @param appInfo
     * @return
     */
    public static boolean isSystemPid(ApplicationInfo appInfo){
        if ((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true ;
        } else if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true ;
        }
        return false ;
    }

}
