package com.example.toollibs.Activity.Config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * use sharedPreference
 */
public class SettingData {
    private static SharedPreferences getShared(Context context, String fileName){
        return context.getSharedPreferences(fileName, Constant.MODE_PRIVATE);
    }

    public static  boolean getBoolean(Context context, String fileName, String key, boolean defValue) {
        return getShared(context, fileName).getBoolean(key, defValue);
    }

    public static void setBoolean(Context context, String fileName, String key, boolean value) {
        SharedPreferences sp = getShared(context, fileName);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String getString(Context context, String fileName, String key, String defValue){
        return getShared(context, fileName).getString(key, defValue);
    }

    public static void setString(Context context, String fileName, String key, String value) {
        SharedPreferences sp = getShared(context, fileName);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static int getInt(Context context, String fileName, String key, int defValue){
        return getShared(context, fileName).getInt(key, defValue);
    }

    public static void setInt(Context context, String fileName, String key, int value) {
        SharedPreferences sp = getShared(context, fileName);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }
}
