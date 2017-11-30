package com.example.user.source;

import android.os.AsyncTask;
import android.util.Log;

import com.example.user.bean.AppInfoBean;
import com.example.user.core.BaseApplication;
import com.example.user.utils.ProcessTaskManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/11/23.
 * 管理进程的厂库
 *
 */

public class ProcessTaskRepository implements IProcessTaskListener {
    private String TAG = "ProcessTaskRepository" ;
    private List<IProcessTaskListener> mList = new ArrayList<>();
    private List<AppInfoBean> mAppInfoDate = new ArrayList<>() ; //缓存当前进程列表
    private boolean mStarting = false;
    private boolean mKillProcess = false; //是否杀进程
    public   static ProcessTaskRepository mInstance = new ProcessTaskRepository();

    private ProcessTaskRepository(){
    }
    public static ProcessTaskRepository getInstance(){
        return mInstance ;
    }
    public void registerTaskListener(IProcessTaskListener listener){
       mList.add(listener);
    }
    public void unRegisterTaskListener(IProcessTaskListener listener){
        if(mList.contains(listener)){
            mList.remove(listener);
        }
    }
    public void beginKillProcessTask(){
        if(mStarting){
            return;
        }
        mStarting = true;
        mKillProcess = true ;
        new ClearTask().execute();
    }

    public void upDateProcessTaskList(){
        if(mStarting){
            return;
        }
        mKillProcess = false ;
        mStarting = true ;
        new ClearTask().execute();
    }
    @Override
    public void onTaskFinish(List<AppInfoBean> list) {
        for(IProcessTaskListener listener : mList){
            listener.onTaskFinish(list);
        }
        mKillProcess = false;
        mStarting = false;
    }

    @Override
    public void onTaskProgress(float rate) {
        for(IProcessTaskListener listener : mList){
            listener.onTaskProgress(rate);
        }
    }

    private class ClearTask extends AsyncTask<Void ,Float,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if(mKillProcess)
                doKillProcessTask();
            mAppInfoDate.clear();
            mAppInfoDate.addAll(ProcessTaskManager.getAppInfoList(BaseApplication.getmCtx()));
            return null;
        }

        private void doKillProcessTask() {
            int i = 0 ;
            if(mAppInfoDate.size() == 0){
                publishProgress(new Float(1.0));
                return;
            }
            for(AppInfoBean bean :mAppInfoDate){
                i++ ;
                if(bean.isLock())
                    continue;
                ProcessTaskManager.killProcess(BaseApplication.getmCtx()
                        , bean.getPackageName());
                publishProgress((float)(i / (mAppInfoDate.size()*1.0)) );
            }
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            onTaskProgress(values[0]);
            Log.i(TAG, "onProgressUpdate: ============== "+values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            onTaskFinish(mAppInfoDate);
            mStarting = false ;
        }
    }
}
