package com.example.user.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.format.Formatter;

import com.example.user.bean.CpuBean;
import com.example.user.bean.MemoryBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by user on 2017/11/24.
 * 获取内存，cpu信息
 */

public class MySystemManager {
    /**
     * 用于获取手机可用内存
     */
    private static ActivityManager mActivityManager;
    public static MemoryBean memoryBean = new MemoryBean()  ;

    public static MemoryBean getMemoryBean(Context context) {
        //获得MemoryInfo对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo() ;
        //获得系统可用内存，保存在MemoryInfo对象上
        getActivityManager(context).getMemoryInfo(memoryInfo) ;
        long availableSize = memoryInfo.availMem ;
        long totalMemorySize = memoryInfo.totalMem;
        int percent = (int) ((totalMemorySize - availableSize)
                / (float) totalMemorySize * 100);
        memoryBean.setRate(percent);
        return memoryBean ;
    }

    //调用系统函数，字符串转换 long -String KB/MB
    private String formateFileSize(long size,Context context){
        return Formatter.formatFileSize(context, size);
    }

    /**
     * 获取当前可用内存，返回数据以字节为单位。
     *
     * @param context 可传入应用程序上下文。
     * @return 当前可用内存。
     */
    private static long getAvailableMemory(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        getActivityManager(context).getMemoryInfo(mi);
        return mi.availMem;
    }

    /**
     * get CPU rate
     *
     * @return
     */
    public static CpuBean cpuBean = new CpuBean();
    public static CpuBean getProcessCpuBean() {
        StringBuilder tv = new StringBuilder();
        int rate = 0;
        try {
            String Result;
            Process p;
            p = Runtime.getRuntime().exec("top -n 1");

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((Result = br.readLine()) != null) {
                if (Result.trim().length() < 1) {
                    continue;
                } else {
                    String[] CPUusr = Result.split("%");
                    tv.append("USER:" + CPUusr[0] + "\n");
                    String[] CPUusage = CPUusr[0].split("User");
                    String[] SYSusage = CPUusr[1].split("System");
                    tv.append("CPU:" + CPUusage[1].trim() + " length:" + CPUusage[1].trim().length() + "\n");
                    tv.append("SYS:" + SYSusage[1].trim() + " length:" + SYSusage[1].trim().length() + "\n");

                    rate = Integer.parseInt(CPUusage[1].trim()) + Integer.parseInt(SYSusage[1].trim());
                    break;
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cpuBean.setRate(rate);
        return cpuBean;
    }

    private static ActivityManager getActivityManager(Context context) {
        if (mActivityManager == null) {
            mActivityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
        }
        return mActivityManager;
    }
}
