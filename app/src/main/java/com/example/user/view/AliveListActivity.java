package com.example.user.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.user.bean.AppInfoBean;
import com.example.user.source.ProcessAliveRepository;
import com.example.user.tvmanager.R;
import com.example.user.adapter.AppRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class AliveListActivity extends Activity {
    private RecyclerView mRecyclerView;
    private AppRecyclerViewAdapter mAdapter;
    private List<AppInfoBean> mList = new ArrayList<>();
    private ProcessAliveRepository mProcessAliveRepository = ProcessAliveRepository.getProcessAlive();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alive_list);
        initView();
    }

    private void initView() {
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
        mList.addAll(mProcessAliveRepository.getAliveList());
        mAdapter.upDate(mList);
    }
}
