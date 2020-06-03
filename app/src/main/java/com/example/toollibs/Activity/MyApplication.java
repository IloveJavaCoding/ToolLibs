package com.example.toollibs.Activity;

import android.app.Application;
import android.content.Context;

import com.example.toollibs.Activity.Config.Constant;
import com.example.toollibs.Activity.Config.SettingData;

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
