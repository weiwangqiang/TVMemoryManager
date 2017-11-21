/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.example.user.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.bean.AppInfo;
import com.example.user.core.BaseApplication;
import com.example.user.model.ProcessAlive;
import com.example.user.tvmanager.R;
import com.example.user.utils.CpuManager;
import com.example.user.utils.MemoryManager;
import com.example.user.utils.TaskManager;
import com.example.user.view.adapter.BgAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.example.user.utils.MemoryManager.getUsedPercentValue;

/*
 * MainActivity class that loads MainFragment
 */
public class MainActivity extends Activity {
    private static String TAG = "MainActivity" ;
    private TextView cup  ;
    private TextView memory ;
    private RecyclerView recyclerView ;
    private ProcessAlive processAlive = ProcessAlive.getProcessAlive() ;
    private BgAdapter adapter ;
    private     Timer timer ;
    List<AppInfo> list = new ArrayList<>() ;
    private Context mCtx = BaseApplication.getmCtx();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer  =new Timer();
        timer.scheduleAtFixedRate(new RunTask(),0,10*1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    private class  RunTask extends TimerTask{

        @Override
        public void run() {
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    cup.setText("cup使用率："+CpuManager.getProcessCpuRate()+"%");
                    memory.setText("内存使用率："+MemoryManager.getUsedPercentValue(mCtx));
                }
            });
        }
    }
    private void init() {
        cup= (TextView) findViewById(R.id.cpuText);
        memory = (TextView) findViewById(R.id.memory);
        cup.setText("cup使用率："+CpuManager.getProcessCpuRate()+"%");
        memory.setText("内存使用率："+ getUsedPercentValue(mCtx));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new BgAdapter(this ,R.layout.activity_back_manager_item) ;
        adapter.setListener(new BgAdapter.onItemClickListener(){

            @Override
            public void OnItemClick(AppInfo appInfo) {
                if(appInfo.isLock()){
                    processAlive.removeAlive(appInfo);
                }else{
                    processAlive.addAlive(appInfo);
                }
                appInfo.setLock(!appInfo.isLock());
                adapter.notifyDataSetChanged();
            }
        });
        LinearLayoutManager manager =  new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        list.addAll(TaskManager.getAppInfoList(getApplication()));
        adapter.upDate(list);
    }
    int beforeSize = 0 ;
    public void clear(View view){
        beforeSize = list.size() ;
        Log.i(TAG, " start clear: ----- "+list.size());
         Observable.from(list)
                .observeOn(Schedulers.newThread())
                .filter(new Func1<AppInfo,Boolean>(){
                    @Override
                    public Boolean call(AppInfo appInfo) {
                        if(appInfo.isLock()) return false ;
                        return TaskManager.killProcess(mCtx,appInfo.getPackageName());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AppInfo>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: complete");
                        upDate();
                    }

                    @Override
                    public void onError(Throwable e) {
                        upDate();
                    }

                    @Override
                    public void onNext(AppInfo appInfo) {
                        //被杀掉的进程
                        Log.i(TAG, "onNext: be killed  "+appInfo.getName());
                    }
                } );

    }

    private void upDate() {
        list.clear();
        list.addAll(TaskManager.getAppInfoList(getApplication()));
        adapter.upDate(list);
        cup.setText("cup使用率："+ CpuManager.getProcessCpuRate()+"%");
        memory.setText("内存使用率："+ MemoryManager.getUsedPercentValue(mCtx));
        Toast.makeText(mCtx,"清除前进程数："+beforeSize+"\n " +
                "清除后进程数 ："+list.size(),Toast.LENGTH_SHORT).show();
    }
}
