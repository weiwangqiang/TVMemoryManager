package com.example.user.source;

import com.example.user.bean.CpuBean;
import com.example.user.bean.MemoryBean;
import com.example.user.core.BaseApplication;
import com.example.user.utils.MySystemManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import static com.example.user.core.BaseApplication.handler;
import static com.example.user.core.BaseApplication.mCtx;

/**
 * Created by user on 2017/11/23.
 * 管理 cpu 与内存数据的厂库
 */

public class SystemRepository implements ISystemDataSource {
    private final String TAG = "SystemRepository " ;

    public static SystemRepository instance;
    private List<ISystemDataSource> mList = new ArrayList<>();
    private Handler mHandler ;
    private CpuBean mCpuBean ;
    private MemoryBean mMemoryBean ;
    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(3); ;
    public static SystemRepository getInstance() {
        if (instance == null)
            instance = new SystemRepository();
        return instance;
    }

    private SystemRepository() {
        handler = BaseApplication.getHandler() ;
        scheduledThreadPool.scheduleAtFixedRate(new RunTask(),1, 10, TimeUnit.SECONDS);
    }

    public void registerUpDateListener(ISystemDataSource listener){
        mList.add(listener);
    }
    public void unRegisterUpDateListener(ISystemDataSource listener){
        if(mList.contains(listener))
            mList.remove(listener);
    }
    @Override
    public void onUpDateCpuRate(CpuBean cpuBean) {
        for(ISystemDataSource tasksDataSource : mList){
            tasksDataSource.onUpDateCpuRate(cpuBean);
        }
    }

    @Override
    public void onUpDateMemoryRate(MemoryBean memoryBean) {
        for(ISystemDataSource tasksDataSource : mList){
            tasksDataSource.onUpDateMemoryRate(memoryBean);
        }
    }

    /**
     * 更新内存 cpu使用率
     */
    public void upDate() {
        scheduledThreadPool.execute(new RunTask());
    }

    private class RunTask implements Runnable {

        @Override
        public void run() {
            mCpuBean = MySystemManager.getProcessCpuBean();
            mMemoryBean = MySystemManager.getMemoryBean(mCtx);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onUpDateCpuRate(mCpuBean);
                    onUpDateMemoryRate(mMemoryBean);
                }
            });
        }
    }
}
