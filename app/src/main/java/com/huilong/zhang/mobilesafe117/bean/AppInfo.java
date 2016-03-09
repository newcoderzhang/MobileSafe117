package com.huilong.zhang.mobilesafe117.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Mario on 3/5/16.
 */
public class AppInfo {
    private Drawable drawable;
    private String apkName;
    private long apksize;
    private boolean userApp;
    private boolean isRom;

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public long getApksize() {
        return apksize;
    }

    public void setApksize(long apksize) {
        this.apksize = apksize;
    }

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    public boolean isRom() {
        return isRom;
    }

    public void setIsRom(boolean isRom) {
        this.isRom = isRom;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "drawable=" + drawable +
                ", apkName='" + apkName + '\'' +
                ", apksize=" + apksize +
                ", userApp=" + userApp +
                ", isRom=" + isRom +
                '}';
    }
}
