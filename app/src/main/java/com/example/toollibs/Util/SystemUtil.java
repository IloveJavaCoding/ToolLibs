package com.example.toollibs.Util;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SystemUtil {
    private static InputMethodManager inputMethodManager;

    public static final int ORIENTATION_LANDSCAPE = 0;
    public static final int ORIENTATION_PORTRAIT = 1;

    //system notices
    public static void ShowToast(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static void ShowLongToast(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    //screen cap
    public static void screenCap(Activity activity)
    {
        View dView = activity.getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bmp = dView.getDrawingCache();
        if (bmp != null)
        {
            String sdCardPath = FileUtil.GetAppRootPth(activity);
            BitmapUtil.Bitmap2Local(bmp, sdCardPath,"screenshot.png");
        }
    }

    public static void reBoot(Context context){
        PowerManager pManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        pManager.reboot("recovery");
    }

    /**
     * 1.PARTIAL_WAKE_LOCK：保证CPU保持高性能运行，而屏幕和键盘背光（也可能是触摸按键的背光）关闭。一般情况下都会使用这个WakeLock。
     * 2.ACQUIRE_CAUSES_WAKEUP：这个WakeLock除了会使CPU高性能运行外还会导致屏幕亮起，即使屏幕原先处于关闭的状态下。
     * 3.ON_AFTER_RELEASE：如果释放WakeLock的时候屏幕处于亮着的状态，则在释放WakeLock之后让屏幕再保持亮一小会。如果释放WakeLock的时候屏幕本身就没亮，则不会有动作
     * ————————————————
     * 版权声明：本文为CSDN博主「jianning-wu」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
     * 原文链接：https://blog.csdn.net/weixin_37730482/article/details/80108786
     * @param context
     * @return
     */
    @SuppressLint("InvalidWakeLockTag")
    public static PowerManager.WakeLock getWakeLock(Context context){
        PowerManager pManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "IncallUI");
        wakeLock.acquire();

        return wakeLock;
    }

    public static void releaseWakeLock(PowerManager.WakeLock wakeLock){
        wakeLock.release();
    }

    //keyboard control
    public static void ShowKeyBoard(EditText et){
        et.setFocusable(true);
        et.requestFocus();
        inputMethodManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(et,InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void CloseKeyBoard(EditText et){
        inputMethodManager.hideSoftInputFromWindow(et.getWindowToken(),0);
    }

    //window attributes
    public static DisplayMetrics GetScreenDM(Context context) {
        WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    //rotate ImageView
    @SuppressLint("WrongConstant")
    public static ObjectAnimator rotateIV(ImageView imgView, int dur){
        ObjectAnimator animator = ObjectAnimator.ofFloat(imgView, "rotation", 0f,360f);
        animator.setDuration(dur);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ObjectAnimator.INFINITE);//infinity

        return animator;
    }

    //permission check
    public static boolean CheckPermission(Context context, String[] permissions){
        if (permissions == null || permissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : permissions) {
            allGranted &= ContextCompat.checkSelfPermission(context, neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }

    //vibrator
    public static void vibrator(Context context){
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = am.getRingerMode();
        if (ringerMode != 0) {
            Vibrator vv = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
            vv.vibrate(500L);
        }
    }

    //横、竖屏
    /*
    public static void setOrientation(int orientation){
        switch (orientation){
            case ORIENTATION_LANDSCAPE:
                RuntimeExec.getInstance().executeRootCommand("setprop persist.sys.screenorientation landscape");
                RuntimeExec.getInstance().executeRootCommand("setprop persist.sys.win.rotation 0");
                RuntimeExec.getInstance().executeRootCommand("reboot");
                break;
            case ORIENTATION_PORTRAIT:
                RuntimeExec.getInstance().executeRootCommand("setprop persist.sys.screenorientation portrait");
                RuntimeExec.getInstance().executeRootCommand("setprop persist.sys.win.rotation 90");
                RuntimeExec.getInstance().executeRootCommand("reboot");
                break;
        }
    }
     */
}
