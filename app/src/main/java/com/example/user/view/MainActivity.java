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
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.bean.AppInfoBean;
import com.example.user.bean.CpuBean;
import com.example.user.bean.MemoryBean;
import com.example.user.core.BaseApplication;
import com.example.user.customView.ClearProcessWaitDialog;
import com.example.user.source.ProcessAliveRepository;
import com.example.user.service.FloatBallService;
import com.example.user.source.IProcessTaskListener;
import com.example.user.source.ISystemDataSource;
import com.example.user.source.ProcessTaskRepository;
import com.example.user.source.SystemRepository;
import com.example.user.tvmanager.R;
import com.example.user.adapter.AppRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements ISystemDataSource,IProcessTaskListener {
    private final static String TAG = "MainActivity";
    private final  String CPU_RATE = "cpu 使用率：";
    private final String MEMORY_RATE = "内存使用率：";
    private final String PERCENT = "%";

    private int    mBeforeSize = 0;
    private boolean mClearing = false;
    private TextView mCpuTextView;
    private TextView mMemoryTextView;
    private RecyclerView mRecyclerView;
    private ProcessAliveRepository mProcessAliveRepository = ProcessAliveRepository.getProcessAlive();
    private AppRecyclerViewAdapter mAdapter;
    private  List<AppInfoBean> mList = new ArrayList<>();
    private Context mCtx = BaseApplication.getmCtx();
    private SystemRepository mSystemRepository ;
    private ProcessTaskRepository mProcessTaskRepository ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSystemRepository.registerUpDateListener(this);
        mProcessTaskRepository.registerTaskListener(this);
        mProcessTaskRepository.upDateProcessTaskList();
        startService(new Intent(this, FloatBallService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSystemRepository.unRegisterUpDateListener(this);
        mProcessTaskRepository.unRegisterTaskListener(this);
    }

    @Override
    public void onUpDateCpuRate(CpuBean cpuBean) {
        mCpuTextView.setText(CPU_RATE+cpuBean.getRate()+PERCENT);
    }

    @Override
    public void onUpDateMemoryRate(MemoryBean memoryBean) {
        mMemoryTextView.setText(MEMORY_RATE + memoryBean.getRate()+PERCENT);
    }

    private void initView() {
        mCpuTextView = (TextView) findViewById(R.id.cpuText);
        mMemoryTextView = (TextView) findViewById(R.id.memory);
        mSystemRepository = SystemRepository.getInstance() ;
        mProcessTaskRepository = ProcessTaskRepository.getInstance();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new AppRecyclerViewAdapter(this, R.layout.item_background_manager);
        mAdapter.setOnLockListener(new AppRecyclerViewAdapter.onLockClickListener() {

            @Override
            public void OnLockClick(AppInfoBean appInfoBean) {
                if (appInfoBean.isLock()) {
                    mProcessAliveRepository.removeAlive(appInfoBean);
                } else {
                    mProcessAliveRepository.addAlive(appInfoBean);
                }
                appInfoBean.setLock(!appInfoBean.isLock());
                mAdapter.notifyDataSetChanged();
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.upDate(mList);
        mRecyclerView.setFocusable(true)  ;
    }

    ClearProcessWaitDialog mClearProcessWaitDialog ;
    public void clear(View view) {
        if(mClearing){
            return;
        }
        mClearing = true;
        mClearProcessWaitDialog = new ClearProcessWaitDialog(this);
        mClearProcessWaitDialog.show();
        mBeforeSize = mList.size();
        mProcessTaskRepository.beginKillProcessTask();
    }

    private void upDate() {
        if(mClearing)
        Toast.makeText(mCtx, "清除前进程数：" + mBeforeSize + "\n " +
                "清除后进程数 ：" + mList.size(), Toast.LENGTH_SHORT).show();
        mAdapter.upDate(mList);
        mClearing = false;
        if(mClearProcessWaitDialog != null){
            mClearProcessWaitDialog.cancel() ;
        }
    }
    public void aliveList(View view){
        startActivity(new Intent(this,AliveListActivity.class));
    }
    @Override
    public void onTaskFinish(List<AppInfoBean> list) {
        mSystemRepository.upDate();
        mList.clear();
        mList.addAll(list);
        upDate();
    }

    @Override
    public void onTaskProgress(float rate) {

    }
}
