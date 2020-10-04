package com.nepalese.toollibs.Util;

import android.content.Context;

/**
 * Created by wuyf on 2020/5/12.
 */

public class ResUtil {
    //获取资源文件的id
    public static int getId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "id", context.getPackageName());
    }

    //获取资源文件中string的id
    public static int getStringId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "string", context.getPackageName());
    }

    // 获取资源文件drable的id
    public static int getDrableId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
    }

    // 获取资源文件layout的id
    public static int getLayoutId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "layout", context.getPackageName());
    }

    //获取资源文件style的id
    public static int getStyleId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "style", context.getPackageName());
    }

    // 获取资源文件color的id
    public static int getColorId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "color", context.getPackageName());
    }

    //获取资源文件dimen的id
    public static int getDimenId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "dimen", context.getPackageName());
    }

    //获取资源文件ainm的id
    public static int getAnimId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "anim", context.getPackageName());
    }

    // 获取资源文件menu的id
    public static int getMenuId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "menu", context.getPackageName());
    }

    // 获取资源文件raw的id
    public static int getRawId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "raw", context.getPackageName());
    }
}
