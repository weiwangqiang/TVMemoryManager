package com.example.user.core;

import android.app.Application;
import android.content.Context;

/**
 * Created by user on 2017/11/21.
 */

public class BaseApplication extends Application {
    public static Application getApplication() {
        return application;
    }

    public static Context getmCtx() {
        return mCtx;
    }

    public static Application application ;
    public static Context mCtx ;
    @Override
    public void onCreate() {
        super.onCreate();
        mCtx = getApplicationContext();
        application = this ;
    }
}
