package com.example.user.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by user on 2017/11/20.
 */

@Entity

public class AppInfo {
    @Id

    private String packageName;
    private String name;
    private String memory;
    private boolean lock;//是否上锁

    private boolean system  ;

    @Generated(hash = 1297690314)
    public AppInfo(String packageName, String name, String memory, boolean lock,
            boolean system) {
        this.packageName = packageName;
        this.name = name;
        this.memory = memory;
        this.lock = lock;
        this.system = system;
    }

    @Generated(hash = 1656151854)
    public AppInfo() {
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    @Override
    public boolean equals(Object o) {
        if( o == null) return false;
        if(o instanceof AppInfo){
            AppInfo appInfo = (AppInfo) o;
            return appInfo.packageName .equals(this.packageName );
        }
        return false ;
    }

    public boolean getSystem() {
        return this.system;
    }

    public boolean getLock() {
        return this.lock;
    }
}
