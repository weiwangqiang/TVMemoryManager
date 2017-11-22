package com.example.user.view.floatBall;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * Created by user on 2017/11/22.
 * floatBall 管理
 */

public class FloatBallManager {
    public static FloatBallManager floatBallManager;
    private static Context mCtx;
    private WindowManager mWindowManager;
    private FloatBallSmallView mSmallView;
    private FloatBallBigView mFloatBallBigView;
    private RocketLauncher mLauncherView;
    private LayoutParams mSmallViewParams;
    private LayoutParams mBigViewParams;
    private LayoutParams mLauncherParams;

    private FloatBallManager(Context mCtx) {
        this.mCtx = mCtx;
        mWindowManager = (WindowManager) mCtx
                .getSystemService(Context.WINDOW_SERVICE);
    }

    public static FloatBallManager getFloatBallManager(Context mCtx) {
        if (null == floatBallManager)
            floatBallManager = new FloatBallManager(mCtx);
        return floatBallManager;
    }

    public WindowManager getmWindowManager() {
        return mWindowManager;
    }

    public void createSmallFloatView() {
        int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        if (mSmallView == null) {
            mSmallView = new FloatBallSmallView(mCtx);
            if (mSmallViewParams == null) {
                mSmallViewParams = new LayoutParams();
                mSmallViewParams.type = LayoutParams.TYPE_TOAST;
                mSmallViewParams.format = PixelFormat.RGBA_8888;
                mSmallViewParams.flags =
                        LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL;
                mSmallViewParams.gravity = Gravity.LEFT | Gravity.TOP;
                mSmallViewParams.width = mSmallView.windowViewWidth;
                mSmallViewParams.height = mSmallView.windowViewHeight;
                mSmallViewParams.x = screenWidth;
                mSmallViewParams.y = screenHeight / 2;
            }
            mSmallView.setParams(mSmallViewParams);
            mWindowManager.addView(mSmallView, mSmallViewParams);
        }
    }

    public void removeSmallFloatView() {
        if (mSmallView != null) {
            mWindowManager.removeView(mSmallView);
            mSmallView = null;
        }
    }

    public void createBigFloatView() {
        int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        if (mFloatBallBigView == null) {
            mFloatBallBigView = new FloatBallBigView(mCtx);
            if (mBigViewParams == null) {
                mBigViewParams = new LayoutParams();
                mBigViewParams.x = screenWidth / 2
                        - mFloatBallBigView.getViewWidth() / 2;
                mBigViewParams.y = screenHeight / 2
                        - mFloatBallBigView.getViewHeight() / 2;
                mBigViewParams.type = LayoutParams.TYPE_TOAST;
                mBigViewParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL; //设置外部可以点击
                mBigViewParams.format = PixelFormat.RGBA_8888;
//                mBigViewParams.gravity = Gravity.LEFT | Gravity.TOP;//暂时不定
                mBigViewParams.width = mFloatBallBigView.getViewWidth();
                mBigViewParams.height = mFloatBallBigView.getViewHeight();
            }
            mWindowManager.addView(mFloatBallBigView, mBigViewParams);
        }
    }

    public void removeBigFloatView() {
        if (mFloatBallBigView != null) {
            mWindowManager.removeView(mFloatBallBigView);
            mFloatBallBigView = null;
        }
    }

    /**
     * 创建一个火箭发射台，位置为屏幕底部。
     */
    public void createLauncher() {
        int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        if (mLauncherView == null) {
            mLauncherView = new RocketLauncher(mCtx);
            if (mLauncherParams == null) {
                mLauncherParams = new LayoutParams();
                mLauncherParams.x = screenWidth / 2 - RocketLauncher.width / 2;
                mLauncherParams.y = screenHeight - RocketLauncher.height;
                mLauncherParams.type = LayoutParams.TYPE_TOAST;
                mLauncherParams.format = PixelFormat.RGBA_8888;
                mLauncherParams.gravity = Gravity.LEFT | Gravity.TOP;
                mLauncherParams.width = RocketLauncher.width;
                mLauncherParams.height = RocketLauncher.height;
            }

            mWindowManager.addView(mLauncherView, mLauncherParams);
        }
    }

    /**
     * 更新火箭发射台的显示状态。
     */
    public void updateLauncher() {
        if (mLauncherView != null) {
            mLauncherView.updateLauncherStatus(isReadyToLaunch());
        }
    }

    /**
     * 将火箭发射台从屏幕上移除。
     */
    public void removeLauncher() {
        if (mLauncherView != null) {
            mWindowManager.removeView(mLauncherView);
            mLauncherView = null;
        }
    }

    /**
     * 判断是否到发射台
     *
     * @return
     */
    private String TAG = "FloatViewBig ";

    public boolean isReadyToLaunch() {
        boolean b1 = mSmallViewParams.x > mLauncherParams.x;
        Log.i(TAG, "isReadyToLaunch: b1 " + b1);
        boolean b2 = (mSmallViewParams.x + mSmallView.windowViewWidth
                <= (mLauncherParams.x + mLauncherParams.width));
        Log.i(TAG, "isReadyToLaunch: b2 " + b2);
        boolean b3 = (mSmallViewParams.y + mSmallViewParams.height > mLauncherParams.y);
        Log.i(TAG, "isReadyToLaunch: b3 " + b3);
        return b1 && b2 && b3;
    }
}
