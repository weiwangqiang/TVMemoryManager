package com.example.user.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Debug;
import android.util.Log;

import com.example.user.bean.AppInfo;
import com.example.user.bean.PackagesInfo;
import com.example.user.model.ProcessAlive;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by user on 2017/11/20.
 * 任务管理
 */

public class TaskManager {
    private static String TAG = "TaskManager " ;
    /**
     *     获取后台应用列表
     */
    public static List<AppInfo> getAppInfoList(Context mCtx){
        PackagesInfo pi =  PackagesInfo.getPackagesInfo(mCtx);
        ProcessAlive processAlive =  ProcessAlive.getProcessAlive() ;
        ActivityManager mActivityManager = (ActivityManager)
                mCtx.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runList =
                mActivityManager.getRunningAppProcesses();
        PackageManager pm = mCtx.getPackageManager();
       List<AppInfo> mList = new ArrayList<>();
        for (ActivityManager.RunningAppProcessInfo ra : runList) {
            ApplicationInfo applicationInfo = pi.getInfo(ra.processName);
            if (applicationInfo == null || ra.processName.equals
                    (mCtx.getPackageName())) {
                continue;
            }

            AppInfo appInfo = new AppInfo();
            if( ra.processName.equals("system") ){
                appInfo.setLock(true);
                appInfo.setSystem(true);
            }
            Debug.MemoryInfo[] memoryInfo = mActivityManager.
                    getProcessMemoryInfo(new int[]{ra.pid});
            appInfo.setPackageName(applicationInfo.packageName);
//            appInfo.setIcon(applicationInfo.loadIcon(pm));
            appInfo.setName(applicationInfo.loadLabel(pm).toString());
            appInfo.setMemory(TextFormat.formatByte(memoryInfo[0].dalvikPrivateDirty));
            appInfo.setLock(processAlive.isAlive(appInfo));
            mList.add(appInfo);
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
     * 获取当前应用名称
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        String appName = null;
        PackageManager packageManager = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            //CharSequence这两者效果是一样的.
            appName = packageManager.getApplicationLabel(applicationInfo).toString();
            appName = (String) packageManager.getApplicationLabel(applicationInfo);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("GsonUtils", "Exception=" + e.toString());
            return null;
        }

        return appName;
    }

    public static void getRunningServiceInfo(Context context, String packageName) {
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(ACTIVITY_SERVICE);
        // 通过调用ActivityManager的getRunningAppServicees()方法获得系统里所有正在运行的进程
        List<ActivityManager.RunningServiceInfo> runServiceList = mActivityManager
                .getRunningServices(50);
        List<ActivityManager.RunningAppProcessInfo> runList =
                mActivityManager.getRunningAppProcesses();
        System.out.println(runServiceList.size());
        // ServiceInfo Model类 用来保存所有进程信息
        for (ActivityManager.RunningServiceInfo runServiceInfo : runServiceList) {
            ComponentName serviceCMP = runServiceInfo.service;
            String serviceName = serviceCMP.getShortClassName(); // service 的类名
            String pkgName = serviceCMP.getPackageName(); // 包名

            if (pkgName.equals(packageName)) {
                mActivityManager.killBackgroundProcesses(packageName);
                //mActivityManager.killBackgroundProcesses(packageName);

            }

        }
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
    public static void  saveAliveProcess(Context mCtx,AppInfo appInfo){

    }
}
