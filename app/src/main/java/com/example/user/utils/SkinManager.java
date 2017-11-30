package com.example.user.utils;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author wangqiang
 * @project jiangdajiuye
 * @since 2017-08-03
 * view 资源获取
 */
public class SkinManager {
    private String TAG = "SkinManager";

    public static View inflater(Context mCtx, @LayoutRes int layout) {
        return inflater(mCtx, layout, null, false);
    }

    /**
     * @param mCtx         上下文
     * @param layout       资源
     * @param parent       parent ViewGroup
     * @param attachToRoot 是否将view贴到parent上
     * @return view
     */
    public static View inflater(Context mCtx, @LayoutRes int layout, ViewGroup parent, boolean attachToRoot) {
        LayoutInflater mLayoutInflater = (LayoutInflater) mCtx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = mLayoutInflater.inflate(layout, parent, attachToRoot);
        return root;
    }
}
