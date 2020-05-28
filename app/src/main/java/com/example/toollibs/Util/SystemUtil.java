package com.example.toollibs.Util;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.PowerManager;
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
            BitmapUtil.Bitmap2Local(bmp,sdCardPath,"screenshot.png");
        }
    }

    public static void reBoot(Context context){
        PowerManager pManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        pManager.reboot("");
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
    public static ObjectAnimator RotateIV(ImageView imgView, int dur){
        ObjectAnimator animator = ObjectAnimator.ofFloat(imgView, "rotation", 0f,360f);
        animator.setDuration(dur);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ObjectAnimator.RESTART);//infinity

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
    //
}
