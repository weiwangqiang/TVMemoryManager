package com.example.user.bean;

/**
 * Created by user on 2017/11/20.
 */

public class AppInfoBean {
    private String packageName;
    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    private String processName;
    private boolean lock;//是否上锁

    private boolean system  ;

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

    @Override
    public boolean equals(Object o) {
        if( o == null) return false;
        if(o instanceof AppInfoBean){
            AppInfoBean appInfoBean = (AppInfoBean) o;
            return appInfoBean.packageName .equals(this.packageName );
        }
        return false ;
    }

    public boolean getSystem() {
        return this.system;
    }

}
