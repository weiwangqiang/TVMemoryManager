package com.example.user.customView.floatBall;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.user.tvmanager.R;

public class RocketLauncher extends LinearLayout {

	/**
	 * 记录火箭发射台的宽度
	 */
	public static int mWidth;

	/**
	 * 记录火箭发射台的高度
	 */
	public static int mHeight;

	/**
	 * 火箭发射台的背景图片
	 */
	private ImageView mLauncherImageView;

	public RocketLauncher(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.view_float_ball_launcher, this);
		mLauncherImageView = (ImageView) findViewById(R.id.launcher_img);
		mWidth = mLauncherImageView.getLayoutParams().width;
		mHeight = mLauncherImageView.getLayoutParams().height;
	}

	/**
	 * 更新火箭发射台的显示状态。如果小火箭被拖到火箭发射台上，就显示发射。
	 */
	public void updateLauncherStatus(boolean isReadyToLaunch) {
		if (isReadyToLaunch) {
			mLauncherImageView.setImageResource(R.drawable.launcher_bg_fire);
		} else {
			mLauncherImageView.setImageResource(R.drawable.launcher_bg_hold);
		}
	}

}
