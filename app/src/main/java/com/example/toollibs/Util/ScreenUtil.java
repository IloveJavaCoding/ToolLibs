package com.example.toollibs.Util;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Method;

/**
 * @author nepalese on 2020/9/4 17:22
 * @usage
 */
public class ScreenUtil {
    private static final String TAG = "ScreenUtil";
    /**
     * 获取屏幕显示指标对象
     * @param context
     * @return DisplayMetrics //dm.widthPixels;  //dm.heightPixels;
     */
    public static DisplayMetrics getScreenDM(Context context) {
        WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);

        return dm;
    }

    public static int getScreenWidth(Context context){
        return getScreenDM(context).widthPixels;
    }

    public static int getScreenHeight(Context context){
        return getScreenDM(context).heightPixels;
    }

    /**
     * 启调键盘   (两种启调方式, 在不同机型存在启调差异)
     */
    public static void showKeyboard(final Context mContext) {
        if (mContext == null) return;
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    /**
     * 启动键盘
     *
     * @param context  活动页
     * @param view 被指定视图
     */
    public static void showKeyboard(Context context, EditText view) {
        if (context == null || view == null) return;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            view.findFocus();
            view.requestFocus();
            imm.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    /**
     * 隐藏键盘
     *
     * @param activity 活动页
     */
    public static void hintSoftInput(Activity activity) {
        if (activity == null || activity.isFinishing()) return;
        View parent = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        hideKeyboard(activity, parent);
    }

    /**
     * 隐藏键盘
     *
     * @param act 活动页
     */
    public static void hideKeyboard(Activity act) {
        if (act == null || act.isFinishing()) return;
        final View v = act.getWindow().peekDecorView();
        hideKeyboard(act, v);
    }

    /**
     * 隐藏键盘
     *
     * @param context 活动页
     * @param view    视图
     */
    public static void hideKeyboard(Context context, View view) {
        if (context == null || view == null) return;
        IBinder mWindowToken = view.getWindowToken();
        if (mWindowToken != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(mWindowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN);
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);//解决三星不能隐藏键盘的问题, 等遇到了再处理,
//                  (注: 现在如果使用, 则存在未被开启键盘的时候, 调用该方法, 会启动键盘)
            }
        }
    }

    /**
     * 禁止Edittext弹出软件盘，光标依然正常显示。
     */
    public static void prohibitShowSoftInput(EditText editText) {
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(editText, false);
        } catch (Exception e) {
            Log.e(TAG, "阻止软件盘弹窗异常"+e);
        }
    }

    public static int getScreenBrightness(Activity act) {
        try {
            return Settings.System.getInt(act.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "获取屏幕亮度异常"+e);
        }
        return 0;
    }

    /**
     * 设置当前屏幕亮度值 0--255，并使之生效
     * @param act 活动页
     * @param value 亮度值   0--255 之间
     */
    public static void setScreenBrightness(Activity act, float value) {
        value = value>255.0f? 255.0f: value< 0.0f? 0.0f: value;
        WindowManager.LayoutParams lp = act.getWindow().getAttributes();
        lp.screenBrightness = lp.screenBrightness + (value) / 255.0f;
        if (lp.screenBrightness > 1) {
            lp.screenBrightness = 1;
        } else if (lp.screenBrightness < 0.2) {
            lp.screenBrightness = (float) 0.2;
        }
        act.getWindow().setAttributes(lp);

        // 保存设置的屏幕亮度值
        Settings.System.putInt(act.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int) value);
    }



}
