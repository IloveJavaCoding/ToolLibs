package com.nepalese.toollibs.Util.Helper;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.nepalese.toollibs.Activity.Config.Constant;
import com.nepalese.toollibs.Activity.Config.SettingData;

import java.util.Locale;

public class LanguageHelper {
    private static Resources mResources;
    private static Configuration mConfiguration;

    public static void init(Context context){
        mResources = context.getResources();
        mConfiguration = mResources.getConfiguration();
        changeLanguage(SettingData.getLanguageIndex(context));
    }

    public static void changeLanguage(int languageId){
        switch (languageId){
            case Constant.LANGUAGE_CHINA:
                mConfiguration.locale = Locale.SIMPLIFIED_CHINESE;
                mResources.updateConfiguration(mConfiguration,mResources.getDisplayMetrics());
                break;
            case Constant.LANGUAGE_ENGLISH:
                mConfiguration.locale = Locale.US;
                mResources.updateConfiguration(mConfiguration,mResources.getDisplayMetrics());
                break;
        }
    }
}
