package com.example.user.core;

import android.app.Application;
import android.content.Context;
import android.os.Handler;


/**
 * Created by user on 2017/11/21.
 */

public class BaseApplication extends Application {
    public static Application getApplication() {
        return mApplication;
    }

    public static Context getmCtx() {
        return mCtx;
    }

    public static Application mApplication ;
    public static Context mCtx ;

    public static Handler getHandler() {
        return handler;
    }

    public static Handler handler ;
    @Override
    public void onCreate() {
        super.onCreate();
        mCtx = getApplicationContext();
        mApplication = this ;
        handler = new Handler();
    }
}
