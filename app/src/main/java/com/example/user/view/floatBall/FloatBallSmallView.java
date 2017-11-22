package com.example.user.view.floatBall;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.tvmanager.R;

import java.lang.reflect.Field;

public class FloatBallSmallView extends LinearLayout implements View.OnClickListener {
    private String TAG = "FloatBallSmallView";
    /**
     * 记录小悬浮窗的宽度
     */
    public static int windowViewWidth;

    /**
     * 记录小悬浮窗的高度
     */
    public static int windowViewHeight;

    /**
     * 记录系统状态栏的高度
     */
    private static int statusBarHeight;

    /**
     * 用于更新小悬浮窗的位置
     */
    private WindowManager windowManager;

    /**
     * 小悬浮窗的布局
     */
    private LinearLayout smallWindowLayout;

    /**
     * 小火箭控件
     */
    private ImageView rocketImg;

    /**
     * 小悬浮窗的参数
     */
    private WindowManager.LayoutParams mParams;

    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private float xInScreen;

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private float yInScreen;

    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private float xDownInScreen;

    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private float yDownInScreen;

    /**
     * 记录手指按下时在小悬浮窗的View上的横坐标的值
     */
    private float xInView;

    /**
     * 记录手指按下时在小悬浮窗的View上的纵坐标的值
     */
    private float yInView;

    /**
     * 记录小火箭的宽度
     */
    private int rocketWidth;

    /**
     * 记录小火箭的高度
     */
    private int rocketHeight;

    /**
     * 记录当前手指是否按下
     */
    private boolean isPressed;

    //屏幕的长宽
    private float screenWidth;
    private float screenHeight;
    private FloatBallManager floatBallManager;

    public FloatBallSmallView(Context context) {
        super(context);
        floatBallManager = FloatBallManager.getFloatBallManager(context.getApplicationContext());
        windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
        smallWindowLayout = (LinearLayout) findViewById(R.id.small_window_layout);
        windowViewWidth = smallWindowLayout.getLayoutParams().width;
        windowViewHeight = smallWindowLayout.getLayoutParams().height;


        screenWidth = windowManager.getDefaultDisplay().getWidth();
        screenHeight = windowManager.getDefaultDisplay().getHeight();

        rocketImg = (ImageView) findViewById(R.id.rocket_img);
        rocketWidth = rocketImg.getLayoutParams().width;
        rocketHeight = rocketImg.getLayoutParams().height;
        TextView percentView = (TextView) findViewById(R.id.percent);
        percentView.setText("内存使用情况 ： ");
        setOnClickListener(this);
    }

    float startX;
    float startY;
    float tempX;
    float tempY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent: ");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent: down ");
                isPressed = true;
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                startX = event.getRawX();
                startY = event.getRawY();

                tempX = event.getRawX();
                tempY = event.getRawY();
                updateViewStatus();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouchEvent: move ");
                float x = event.getRawX() - startX;
                float y = event.getRawY() - startY;
                //计算偏移量，刷新视图
                mParams.x += x;
                mParams.y += y;
                // 手指移动的时候更新小悬浮窗的状态和位置
                updateViewPosition();
                startX = event.getRawX();
                startY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onTouchEvent: up ");
                float endX = event.getRawX();
                float endY = event.getRawY();
                isPressed = false;
                if (Math.abs(endX - tempX) < 6 && Math.abs(endY - tempY) < 6) {
                    Log.i(TAG, "onTouchEvent:  to click  ");
                    return false;
                }
                boolean b = floatBallManager.isReadyToLaunch();
                if (b) {
                    launchRocket();
                } else {
                    updateViewStatus();
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
     *
     * @param params 小悬浮窗的参数
     */
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    /**
     * 用于发射小火箭。
     */
    private void launchRocket() {
        floatBallManager.removeLauncher();
        new LaunchTask().execute();
    }

    /**
     * 更新小悬浮窗在屏幕中的位置。
     */
    private void updateViewPosition() {
        windowManager.updateViewLayout(this, mParams);
        floatBallManager.updateLauncher();
    }

    /**
     * 更新View的显示状态，判断是显示悬浮窗还是小火箭。
     */
    private void updateViewStatus() {
        if (isPressed && rocketImg.getVisibility() != View.VISIBLE) {
            mParams.width = rocketWidth;
            mParams.height = rocketHeight;
            windowManager.updateViewLayout(this, mParams);
            smallWindowLayout.setVisibility(View.GONE);
            rocketImg.setVisibility(View.VISIBLE);
            floatBallManager.createLauncher();
        } else if (!isPressed) {
            if (isInLeftOfScreen())
                mParams.x = 0;
            else
                mParams.x = (int) (screenWidth - mParams.width);
            mParams.width = windowViewWidth;
            mParams.height = windowViewHeight;

            windowManager.updateViewLayout(this, mParams);
            smallWindowLayout.setVisibility(View.VISIBLE);
            rocketImg.setVisibility(View.GONE);
            floatBallManager.removeLauncher();
        }
    }

    /**
     * 悬浮球是否在屏幕左边
     *
     * @return
     */
    public boolean isInLeftOfScreen() {
        return screenWidth - mParams.x >= mParams.x;
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick:  -------------- ");
        floatBallManager.createBigFloatView();
        floatBallManager.removeSmallFloatView();
    }

    /**
     * 开始执行发射小火箭的任务。
     *
     * @author guolin
     */
    class LaunchTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // 在这里对小火箭的位置进行改变，从而产生火箭升空的效果
            while (mParams.y > 0) {
                mParams.y = mParams.y - 10;
                publishProgress();
                try {
                    Thread.sleep(8);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            windowManager.updateViewLayout(FloatBallSmallView.this, mParams);
        }

        @Override
        protected void onPostExecute(Void result) {
            // 火箭升空结束后，回归到悬浮窗状态
            updateViewStatus();
//			mParams.x = (int) (xDownInScreen - xInView);
//			mParams.y = (int) (yDownInScreen - yInView);
//			windowManager.updateViewLayout(FloatBallSmallView.this, mParams);
        }

    }

}
