package com.example.user.customView.floatBall;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.bean.AppInfoBean;
import com.example.user.bean.CpuBean;
import com.example.user.bean.MemoryBean;
import com.example.user.source.IProcessTaskListener;
import com.example.user.source.ISystemDataSource;
import com.example.user.source.ProcessTaskRepository;
import com.example.user.source.SystemRepository;
import com.example.user.tvmanager.R;
import com.example.user.utils.MyWindowManager;

import java.util.List;

public class FloatBallSmallView extends
        LinearLayout implements View.OnClickListener
        , View.OnTouchListener, ISystemDataSource, IProcessTaskListener {
    private final String TAG = "FloatBallSmallView";

    private final String MEMORY_RATE = "内存使用率：";
    private final String PERCENT = "%";
    /**
     * 记录小悬浮窗的宽度
     */
    public static int mWindowViewWidth;

    /**
     * 记录小悬浮窗的高度
     */
    public static int mWindowViewHeight;

    /**
     * 用于更新小悬浮窗的位置
     */
    private WindowManager mWindowManager;

    /**
     * 小悬浮窗的布局
     */
    private LinearLayout mSmallWindowLayout;

    /**
     * 小火箭控件
     */
    private ImageView mRocketImageView;

    /**
     * 小悬浮窗的参数
     */
    private WindowManager.LayoutParams mParams;
    /**
     * 记录小火箭的宽度
     */
    private int mRocketWidth;

    /**
     * 记录小火箭的高度
     */
    private int mRocketHeight;

    /**
     * 记录当前手指是否按下
     */
    private boolean isPressed;
    /**
     * 是否正在发射火箭
     */
    private boolean isLaunchRocket;

    /**
     *记录下每次移动触摸的位置
     */
   private  float mStartX;
    private float mStartY;
    /**
     * 记录下每次点击位置
     */
    private float mTempX;
    private float mTempY;

    /**
     * 屏幕的长宽
     */
    private float mScreenWidth;
    private float mScreenHeight;
    private TextView mMemoryTextView;
    private FloatBallManager mFloatBallManager;
    private SystemRepository mSystemRepository;
    private ProcessTaskRepository mProcessTaskRepository;

    public FloatBallSmallView(Context context) {
        super(context);
        mFloatBallManager = FloatBallManager.getFloatBallManager(context.getApplicationContext());
        mWindowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
        mSmallWindowLayout = (LinearLayout) findViewById(R.id.small_window_layout);
        mWindowViewWidth = mSmallWindowLayout.getLayoutParams().width;
        mWindowViewHeight = mSmallWindowLayout.getLayoutParams().height;

        mScreenWidth = MyWindowManager.getScreenWidth(context);
        mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();
        mRocketImageView = (ImageView) findViewById(R.id.mRocketImageView);
        mRocketWidth = mRocketImageView.getLayoutParams().width;
        mRocketHeight = mRocketImageView.getLayoutParams().height;
        mMemoryTextView = (TextView) findViewById(R.id.mMemoryTextView);
        mSystemRepository = SystemRepository.getInstance();
        mProcessTaskRepository = ProcessTaskRepository.getInstance();
        setOnClickListener(this);
        setOnTouchListener(this);
        mSystemRepository.registerUpDateListener(this);
        mProcessTaskRepository.registerTaskListener(this);
    }

    /**
     * 注销listener
     */
    public void unRegisterUpDateListener() {
        mSystemRepository.unRegisterUpDateListener(this);
        mProcessTaskRepository.unRegisterTaskListener(this);
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
        isLaunchRocket = true;
        mFloatBallManager.removeLauncher();
        mProcessTaskRepository.beginKillProcessTask();
    }

    /**
     * 更新小悬浮窗在屏幕中的位置。
     */
    private void updateViewPosition() {
        Log.i(TAG, "updateViewPosition: mParama x "+mParams.x);
        mWindowManager.updateViewLayout(this, mParams);
        mFloatBallManager.updateLauncher();
    }

    /**
     * 更新View的显示状态，判断是显示悬浮窗还是小火箭。
     */
    private void updateViewStatus() {
        Log.i(TAG, "updateViewStatus: ");
        if (isPressed && mRocketImageView.getVisibility() != View.VISIBLE) {
            mParams.width = mRocketWidth;
            mParams.height = mRocketHeight;
            mWindowManager.updateViewLayout(this, mParams);
            mSmallWindowLayout.setVisibility(View.GONE);
            mRocketImageView.setVisibility(View.VISIBLE);
            mFloatBallManager.createLauncher();
        } else if (!isPressed) {
            if (isInLeftOfScreen())
                mParams.x = 0;
            else
                mParams.x = (int) (mScreenWidth - mParams.width);
            mParams.width = mWindowViewWidth;
            mParams.height = mWindowViewHeight;

            mWindowManager.updateViewLayout(this, mParams);
            mSmallWindowLayout.setVisibility(View.VISIBLE);
            mRocketImageView.setVisibility(View.GONE);
            mFloatBallManager.removeLauncher();
        }
    }

    /**
     * 悬浮球是否在屏幕左边
     *
     * @return
     */
    public boolean isInLeftOfScreen() {
        return mScreenWidth - mParams.x >= mParams.x;
    }

    @Override
    public void onClick(View v) {
        mFloatBallManager.removeSmallFloatView();
        mFloatBallManager.createBigFloatView();
    }

    @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPressed = true;
                mStartX = event.getX();
                mStartY = event.getRawY();

                mTempX = event.getRawX();
                mTempY = event.getRawY();
                updateViewStatus();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getRawX() - mStartX;
                float y = event.getRawY() - mStartY;
                //计算偏移量，刷新视图
                mParams.x += x;
                mParams.y += y;
                // 手指移动的时候更新小悬浮窗的状态和位置
                updateViewPosition();
                mStartX = event.getRawX();
                mStartY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                float endX = event.getRawX();
                float endY = event.getRawY();
                isPressed = false;
                if (Math.abs(endX - mTempX) < 6 && Math.abs(endY - mTempY) < 6) {
                    Log.i(TAG, "onTouch: to onclick ");
                    updateViewStatus();
                    return false;
                }
                boolean b = mFloatBallManager.isReadyToLaunch();
                if (b) {
                    launchRocket();
                } else {
                    updateViewStatus();
                }
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onUpDateCpuRate(CpuBean cpuBean) {

    }

    @Override
    public void onUpDateMemoryRate(MemoryBean memoryBean) {
        mMemoryTextView.setText(MEMORY_RATE + memoryBean.getRate() + PERCENT);
    }

    @Override
    public void onTaskFinish(List<AppInfoBean> list) {
        mSystemRepository.upDate();
    }

    @Override
    public void onTaskProgress(float rate) {
        if (!isLaunchRocket){
            return;
        }
        mParams.y = (int) (mParams.y * (1 - rate));
        if(mParams.y <1){
            updateViewStatus();
            isLaunchRocket = false;
        }else{
            mWindowManager.updateViewLayout(FloatBallSmallView.this, mParams);
        }

    }
}
