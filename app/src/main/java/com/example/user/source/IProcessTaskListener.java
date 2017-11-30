package com.example.user.source;

import com.example.user.bean.AppInfoBean;

import java.util.List;

/**
 * Created by user on 2017/11/23.
 * 管理进程信息的接口
 */

public interface IProcessTaskListener {
    /**
     * 杀进程后剩余进程信息
     * @param list
     */
    void onTaskFinish(List<AppInfoBean> list);

    /**
     * 杀进程进度
     * @param rate
     */
    void onTaskProgress(float rate);
}
