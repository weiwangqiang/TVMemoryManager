package com.example.user.utils;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Debug;
import android.util.Log;

import com.example.user.bean.AppInfo;
import com.example.user.bean.PackagesInfo;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by user on 2017/11/20.
 */

public class TaskManager {
    //获取后台应用列表
    public static List<AppInfo> getAppInfoList(Context mCtx){
        PackagesInfo pi = new PackagesInfo(mCtx);
        ActivityManager mActivityManager = (ActivityManager) mCtx.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runList = mActivityManager.getRunningAppProcesses();
        PackageManager pm = mCtx.getPackageManager();
       List<AppInfo> mList = new ArrayList<>();
        for (ActivityManager.RunningAppProcessInfo ra : runList) {
            ApplicationInfo applicationInfo = pi.getInfo(ra.processName);
            if (applicationInfo == null || ra.processName.equals("system") || ra.processName.equals
                    (mCtx.getPackageName())) {
                continue;
            }
            AppInfo appInfo = new AppInfo();
            Debug.MemoryInfo[] memoryInfo = mActivityManager.getProcessMemoryInfo(new int[]{ra.pid});
            appInfo.setPackageName(applicationInfo.packageName);
            appInfo.setIcon(applicationInfo.loadIcon(pm));
            appInfo.setName(applicationInfo.loadLabel(pm).toString());
            appInfo.setMemory(TextFormat.formatByte(memoryInfo[0].dalvikPrivateDirty));
            mList.add(appInfo);
        }
        return mList ;
    }


    private static void getTopApp(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                UsageStatsManager m = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            if (m != null) {
                long now = System.currentTimeMillis();
                //获取60秒之内的应用数据
                List<UsageStats> stats = m.queryUsageStats(UsageStatsManager.INTERVAL_BEST, now - 60 * 1000, now);
                Log.i(TAG, "Running app number in last 60 seconds : " + stats.size());

                String topActivity = "";

                //取得最近运行的一个app，即当前运行的app
                if ((stats != null) && (!stats.isEmpty())) {
                    int j = 0;
                    for (int i = 0; i < stats.size(); i++) {
                        if (stats.get(i).getLastTimeUsed() > stats.get(j).getLastTimeUsed()) {
                            j = i;
                        }
                    }
                    topActivity = stats.get(j).getPackageName();
                }
                Log.i(TAG, "top running app is : "+topActivity);
            }
        }
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
}
