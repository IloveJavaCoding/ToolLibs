package com.example.toollibs.Activity.Config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * use sharedPreference
 */
public class SettingData {
    private static final String CONFIG_FILE = "config";
    private static final int MODE_PRIVATE = 0X0000;

    public static final String AUTO_START_KEY = "auto_start";

    public static  boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG_FILE, MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
