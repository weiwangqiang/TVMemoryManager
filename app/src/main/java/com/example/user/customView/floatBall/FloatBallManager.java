package com.example.user.customView.floatBall;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.example.user.utils.MyWindowManager;

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
    private int mScreenWidth;
    private int mScreenHeight;

    private FloatBallManager(Context mCtx) {
        this.mCtx = mCtx;
        mWindowManager = (WindowManager) mCtx
                .getSystemService(Context.WINDOW_SERVICE);

        mScreenWidth = mWindowManager.getDefaultDisplay().getWidth();
        mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();
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
        if (mSmallView == null) {
            mSmallView = new FloatBallSmallView(mCtx);
            if (mSmallViewParams == null) {
                mSmallViewParams = new LayoutParams();
                mSmallViewParams.type = LayoutParams.TYPE_TOAST;//如果是TV 则改为TYPE_TOAST
                mSmallViewParams.format = PixelFormat.RGBA_8888;
                mSmallViewParams.flags =
                        LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL;
                mSmallViewParams.gravity = Gravity.LEFT | Gravity.TOP;
                mSmallViewParams.width = mSmallView.mWindowViewWidth;
                mSmallViewParams.height = mSmallView.mWindowViewHeight;
                mSmallViewParams.x = mScreenWidth;
                mSmallViewParams.y = mScreenHeight / 2;
            }
            mSmallView.setParams(mSmallViewParams);
        }
        mWindowManager.addView(mSmallView, mSmallViewParams);
    }

    public void removeSmallFloatView() {
        if (mSmallView != null) {
            mWindowManager.removeView(mSmallView);
        }
    }

    public void createBigFloatView() {
        if (mFloatBallBigView == null) {
            mFloatBallBigView = new FloatBallBigView(mCtx);
            if (mBigViewParams == null) {
                mBigViewParams = new LayoutParams();
                mBigViewParams.x = mScreenWidth / 2
                        - mFloatBallBigView.getViewWidth() / 2;
                mBigViewParams.y = mScreenHeight / 2
                        - mFloatBallBigView.getViewHeight() / 2;
                mBigViewParams.type = LayoutParams.TYPE_TOAST;
                mBigViewParams.flags =
                        LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL;
                mBigViewParams.format = PixelFormat.RGBA_8888;
                mBigViewParams.gravity = Gravity.LEFT | Gravity.TOP;//暂时不定
                mBigViewParams.width = mFloatBallBigView.getViewWidth();
                mBigViewParams.height = mFloatBallBigView.getViewHeight();
            }
        }
        makeSurePositionSame();
        mWindowManager.addView(mFloatBallBigView, mBigViewParams);
    }

    /**
     * 确保大小悬浮球 位置 一致
     */
    private void makeSurePositionSame() {
        if (mSmallViewParams != null) {
            if (MyWindowManager.isInLeftOfScreen(mCtx, mSmallViewParams.x)) {
                mBigViewParams.x = 0;
            } else {
                mBigViewParams.x = MyWindowManager.mScreenWidth - mBigViewParams.width;
            }
            mBigViewParams.y = mSmallViewParams.y;
        }
    }

    public void removeBigFloatView() {
        if (mFloatBallBigView != null) {
            mWindowManager.removeView(mFloatBallBigView);
        }
    }

    /**
     * 创建一个火箭发射台，位置为屏幕底部。
     */
    public void createLauncher() {
        if (mLauncherView == null) {
            mLauncherView = new RocketLauncher(mCtx);
            if (mLauncherParams == null) {
                mLauncherParams = new LayoutParams();
                mLauncherParams.x = mScreenWidth / 2 - RocketLauncher.mWidth / 2;
                mLauncherParams.y = mScreenHeight - RocketLauncher.mHeight;
                mLauncherParams.type = LayoutParams.TYPE_TOAST;
                mLauncherParams.format = PixelFormat.RGBA_8888;
                mSmallViewParams.flags =
                        LayoutParams.FLAG_NOT_FOCUSABLE;

                mLauncherParams.gravity = Gravity.LEFT | Gravity.TOP;
                mLauncherParams.width = RocketLauncher.mWidth;
                mLauncherParams.height = RocketLauncher.mHeight;
            }
        }
        mWindowManager.addView(mLauncherView, mLauncherParams);
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

    public boolean isReadyToLaunch() {
        boolean b1 = mSmallViewParams.x > mLauncherParams.x;
        boolean b2 = (mSmallViewParams.x + mSmallView.mWindowViewWidth
                <= (mLauncherParams.x + mLauncherParams.width));
        boolean b3 = (mSmallViewParams.y + mSmallViewParams.height > mLauncherParams.y);
        return b1 && b2 && b3;
    }
}
