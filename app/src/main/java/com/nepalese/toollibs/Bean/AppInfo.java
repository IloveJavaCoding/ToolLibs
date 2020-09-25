package com.nepalese.toollibs.Bean;

import android.graphics.drawable.Drawable;

/**
 * @author nepalese on 2020/9/25 14:00
 * @usage
 */
public class AppInfo extends BaseBean {
    private String name;
    private String packageName;
    private String versionName;
    private Drawable icon;
    private int versionCode;
    private int position;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    String getString() {
        return null;
    }
}
