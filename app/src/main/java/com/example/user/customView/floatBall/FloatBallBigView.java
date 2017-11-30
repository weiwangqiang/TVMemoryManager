package com.example.user.customView.floatBall;

import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import com.example.user.core.BaseApplication;
import com.example.user.tvmanager.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/11/22.
 * 大view 菜单
 */

public class FloatBallBigView extends RelativeLayout
        implements View.OnClickListener{

    private String TAG= "FloatBallBigView" ;
    /**
     * 记录大悬浮窗的宽度
     */
    private  int viewWidth;

    /**
     * 记录大悬浮窗的高度
     */
    private  int viewHeight;
    private GridLayout gridLayout ;
    FloatBallManager floatBallManager ;
    public FloatBallBigView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_float_ball_big, this);
        floatBallManager = FloatBallManager.getFloatBallManager(context) ;
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        viewHeight = gridLayout.getLayoutParams().height;
        viewWidth = gridLayout.getLayoutParams().width ;
        findViewById(R.id.up).setOnClickListener(this);
        findViewById(R.id.left).setOnClickListener(this);
        findViewById(R.id.right).setOnClickListener(this);
        findViewById(R.id.down).setOnClickListener(this);
        findViewById(R.id.menu).setOnClickListener(this);
        findViewById(R.id.home).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.exchange).setOnClickListener(this);
        findViewById(R.id.main).setOnClickListener(this);
        Log.i(TAG, "FloatBallBigView: ");
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: ");
        switch (v.getId()){
            case  R.id.up:
                up();
                break;
            case  R.id.left:
                left();
                break;
            case  R.id.right:
                right();
                break;
            case  R.id.down:
                down();
                break;
            case  R.id.menu:
                menu();
                break;
            case  R.id.home:
                home();
                break;
            case  R.id.back:
                back();
                break;
            case R.id.exchange:
                exchange();
                break;
            case R.id.main:
                break;
            default:
                break;
        }
    }

    private void exchange() {
        FloatBallManager manager =
                FloatBallManager.getFloatBallManager(BaseApplication.getmCtx());
        manager.removeBigFloatView();
        manager.createSmallFloatView();
    }

    private void back() {
        simulateKeystroke(KeyEvent.KEYCODE_BACK);
    }

    public  void simulateKeystroke(final int KeyCode) {
        new Thread(new Runnable() {

            public void run() {
                // TODO Auto-generated method stub
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendCharacterSync(KeyCode);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }).start();
    }
    private void home() {
        if(isHome()){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        BaseApplication.getmCtx().startActivity(intent);
    }
    /**
     * 判断当前界面是否是桌面
     */
    private boolean isHome() {
        ActivityManager mActivityManager = (ActivityManager) BaseApplication.getmCtx()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return getHomes().contains(rti.get(0).topActivity.getPackageName());
    }
    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有包名的字符串列表
     */
    private List<String> getHomes() {
        List<String> names = new ArrayList<>();
        PackageManager packageManager = BaseApplication.getmCtx().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }
    private void menu() {
        simulateKeystroke(KeyEvent.KEYCODE_HOME);
    }

    private void down() {
        simulateKeystroke(KeyEvent.KEYCODE_DPAD_DOWN);
    }

    private void right() {
        simulateKeystroke(KeyEvent.KEYCODE_DPAD_RIGHT);
    }

    private void left() {
        simulateKeystroke(KeyEvent.KEYCODE_DPAD_LEFT);
    }

    private void up() {
        simulateKeystroke(KeyEvent.KEYCODE_DPAD_UP);
    }

    private void execAdbCode(int code) {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("input keyevent " + code);
        } catch (IOException e) { // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public int getViewWidth() {
        return viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }

}
