package com.example.user.view.floatBall;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import com.example.user.core.BaseApplication;
import com.example.user.tvmanager.R;

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

    public void setLayoutParams(WindowManager.LayoutParams layoutParams) {
        this.layoutParams = layoutParams;
    }
    FloatBallManager floatBallManager ;
    private WindowManager windowManager ;
    private WindowManager.LayoutParams layoutParams ;
    public FloatBallBigView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.float_ball_big_view, this);
        floatBallManager = FloatBallManager.getFloatBallManager(context) ;
        windowManager = floatBallManager.getmWindowManager() ;
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
    }

    @Override
    public void onClick(View v) {
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
            default:
                break;
        }
    }

    private void back() {
        Log.i(TAG, "back: ");
        FloatBallManager manager = FloatBallManager.getFloatBallManager(BaseApplication.getmCtx());
        manager.removeBigFloatView();
        manager.createSmallFloatView();
        Log.i(TAG, "back: create small view ");
    }

    private void home() {
        Log.i(TAG, "home: ");
    }

    private void menu() {
        Log.i(TAG, "menu: ");
    }

    private void down() {
        Log.i(TAG, "down: ");
    }

    private void right() {
        Log.i(TAG, "right: ");
    }

    private void left() {
        Log.i(TAG, "left: ");
    }

    private void up() {

    }
    public int getViewWidth() {
        return viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }

}
