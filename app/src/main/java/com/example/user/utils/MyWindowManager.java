package com.example.user.utils;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by user on 2017/11/23.
 * 获取window参数
 */

public class MyWindowManager {
    public static android.view.WindowManager mWindowManager ;

    public static int getScreenWidth(Context mCtx) {
        if(mScreenWidth == 0)
            mScreenWidth = getmWindowManager(mCtx).getDefaultDisplay().getWidth();
        return mScreenWidth;
    }

    public static int mScreenWidth ;

    public static int getScreenHeight(Context mCtx ) {
        if(mScreenHeight == 0)
            mScreenHeight = getmWindowManager(mCtx).getDefaultDisplay().getHeight() ;
        return mScreenHeight;
    }

    public static int mScreenHeight ;
    public static boolean isInLeftOfScreen(Context mCtx , int x){
        WindowManager manager = getmWindowManager(mCtx);
        if(mScreenWidth == 0){
            mScreenWidth = manager.getDefaultDisplay().getWidth();
            mScreenHeight = manager.getDefaultDisplay().getHeight();
        }
        return mScreenWidth - x >= x;
    }
    public static android.view.WindowManager getmWindowManager(Context mCtx){
        return   mWindowManager = (WindowManager) mCtx
                        .getSystemService(Context.WINDOW_SERVICE);
    }
}
