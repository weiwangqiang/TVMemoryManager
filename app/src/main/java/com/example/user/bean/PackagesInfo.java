package com.example.user.bean;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.List;

public class PackagesInfo {
    private List<ApplicationInfo> appList ;

    public static PackagesInfo getPackagesInfo(Context mCtx) {
        if(null == packagesInfo)
            packagesInfo = new PackagesInfo(mCtx) ;
        return packagesInfo;
    }

    public static PackagesInfo packagesInfo  ;

    private PackagesInfo(Context context) {
        PackageManager pm = context.getApplicationContext().getPackageManager();
        appList = pm.getInstalledApplications(0);
    }


    /**
     * 通过一个程序名返回该程序的一个Application对象。
     */

    public ApplicationInfo getInfo(String name) {
        if (name == null) {
            return null;
        }
        for (ApplicationInfo app : appList) {
            if (name.equals(app.processName)) {
                return app;
            }
        }
        return null;
    }
}
