package com.example.user.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.user.customView.floatBall.FloatBallManager;

/**
 * Created by user on 2017/11/22.
 */

public class FloatBallService extends Service {
    private final String TAG = "FloatBallService" ;
    FloatBallManager mFloatBallManager ;
    @Override
    public void onCreate() {
        super.onCreate();
        mFloatBallManager = FloatBallManager.
                getFloatBallManager(this) ;
        mFloatBallManager.createBigFloatView();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1,new Notification());
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
