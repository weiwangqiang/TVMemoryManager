package com.example.user.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by user on 2017/11/20.
 */

public class AppInfo {

    private String packageName;
    private Drawable icon;
    private String name;
    private String memory;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
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
}
