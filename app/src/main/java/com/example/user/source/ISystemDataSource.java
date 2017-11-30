package com.example.user.source;

import com.example.user.bean.CpuBean;
import com.example.user.bean.MemoryBean;

/**
 * Created by user on 2017/11/23.
 * 获取系统信息的接口
 */

public interface ISystemDataSource {
    void onUpDateCpuRate(CpuBean cpuBean);
    void onUpDateMemoryRate(MemoryBean memoryBean);
}
