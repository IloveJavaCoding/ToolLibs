package com.example.toollibs.Util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.example.toollibs.Activity.Config.SettingData;

import java.util.Locale;

public class LanguageHelper {
    public static final int CHANGE_LANGUAGE_CHINA = 0;
    public static final int CHANGE_LANGUAGE_ENGLISH = 1;
    private static Resources mResources;
    private static Configuration mConfiguration;

    public static void init(Context context){
        mResources = context.getResources();
        mConfiguration = mResources.getConfiguration();
        changeLanguage(SettingData.getLanguageIndex(context));
    }

    public static void changeLanguage(int languageId){
        switch (languageId){
            case CHANGE_LANGUAGE_CHINA:
                mConfiguration.locale = Locale.SIMPLIFIED_CHINESE;
                mResources.updateConfiguration(mConfiguration,mResources.getDisplayMetrics());
                break;
            case CHANGE_LANGUAGE_ENGLISH:
                mConfiguration.locale = Locale.US;
                mResources.updateConfiguration(mConfiguration,mResources.getDisplayMetrics());
                break;
        }
    }
}
