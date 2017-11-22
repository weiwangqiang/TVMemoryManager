package com.example.user.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.user.core.BaseApplication;
import com.example.user.view.floatBall.FloatBallManager;

/**
 * Created by user on 2017/11/22.
 */

public class FloatBallService extends Service {
    private String TAG = "FloatBallService" ;
    FloatBallManager floatBallManager ;
    @Override
    public void onCreate() {
        super.onCreate();
        floatBallManager = FloatBallManager.
                getFloatBallManager(BaseApplication.getmCtx()) ;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        floatBallManager.createSmallFloatView();
        Log.i(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
