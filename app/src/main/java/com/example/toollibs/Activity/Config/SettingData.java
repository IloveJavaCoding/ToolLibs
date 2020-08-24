package com.example.toollibs.Activity.Config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * use sharedPreference
 */
public class SettingData {
    public static final String CONFIG_FILE = "config";
    public static final String URL_FILE = "url";

    //key value
    public static final String AUTO_START_KEY = "auto_start";
    public static final String URL_KEY = "url_history";
    public static final String LANGUAGE_KEY = "language";
    public static final String ALARM_TIME_KEY = "alarm_time";
    public static final String ALARM_STATE_KEY = "alarm_state";
    public static final String ALARM_SOUND_KEY = "alarm_sound";
    public static final String AUDIO_DIR_KEY = "audio_dir";
    public static final String READ_MODE_KEY = "read_mode";

    //setting language index
    public static int getLanguageIndex(Context context) {
        return getInt(context, CONFIG_FILE, LANGUAGE_KEY, Constant.LANGUAGE_DEFAULT);
    }

    public static void saveLanguageIndex(Context context, int value) {
        setInt(context, CONFIG_FILE, LANGUAGE_KEY, value);
    }

    //setting auto start
    public static boolean isAutoStart(Context context) {
        return getBoolean(context, CONFIG_FILE, AUTO_START_KEY, Constant.AUTO_START_DEFAULT);
    }

    public static void saveAutoStart(Context context, boolean value) {
        setBoolean(context, CONFIG_FILE, AUTO_START_KEY, value);
    }

    //

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
