package com.example.toollibs.Util;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.toollibs.Activity.Config.Constant;
import com.example.toollibs.R;

import java.io.File;
import java.util.Locale;

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
    public static void screenCap(Activity activity, String fileName)
    {
        View dView = activity.getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bmp = dView.getDrawingCache();
        if (bmp != null)
        {
            String sdCardPath = FileUtil.GetAppRootPth(activity);
            BitmapUtil.Bitmap2Local(bmp, sdCardPath, fileName);
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

    //network type
    public static String getNetWorkTypeString(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null){
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo!=null){
                int type = networkInfo.getType();
                switch (type){
                    case ConnectivityManager.TYPE_MOBILE:
                        return "\n(数据)";
                    case ConnectivityManager.TYPE_WIFI:
                        return "\n(WIFI)";
                    case ConnectivityManager.TYPE_ETHERNET:
                        return "\n(有线)";
                }
            }
        }
        return "";
    }

    //get system storage info
    public static long getSystemStore(Context context, String type){
        File file = Environment.getExternalStorageDirectory();
        if (file.exists()){
            switch (type){
                case Constant.TYPE_TOTAL:
                    return file.getTotalSpace();
                case Constant.TYPE_FREE:
                    return file.getFreeSpace();
                case Constant.TYPE_USED:
                    return file.getTotalSpace()-file.getFreeSpace();
            }
        }
        return -1;
    }

    //set app language
    public static void setLanguage(Context context, int languageId){
        Resources mResources = context.getResources();
        Configuration mConfiguration = mResources.getConfiguration();
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

    //set screen brightness
    public static int getSystemScreenBrightness(Activity activity) {
        try {
            return Settings.System.getInt(activity.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean isAutoBrightness(Activity activity) {
        try {
            int autoBrightness = Settings.System.getInt(
                    activity.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
            if (autoBrightness == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                return true;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setScreenBrightness(Activity activity, int brightness) {//0-255
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            if (brightness < 10) {
                brightness = 10;
            }
            lp.screenBrightness = Float.valueOf(brightness / 255f);
            Settings.System.putInt(activity.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS, brightness);
        }
        activity.getWindow().setAttributes(lp);
    }

    //========
    public void setBrightness(Activity activity, float brightness){
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness;
        window.setAttributes(lp);
    }

    public static void closeAutoBrightness(Activity activity) {
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    public static void openAutoBrightness(Activity activity) {
        setScreenBrightness(activity, -1);
        Settings.System.putInt(activity.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }

    //=====================notification=============================
    public static void sendNotification(Context context, String channelId, Intent intent, String title, String msg, int id){
        //1. get notification service
        NotificationManager manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //2. create notification  channel version>26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelName = "Default Name";
            manager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
        }

        //3. set intent(active when click the notification)
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        //4. init notification, get instance
        Notification notification = new NotificationCompat.Builder(context,channelId)
                .setContentTitle(title) //title
                .setContentText(msg) //message
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)//intent
                .setAutoCancel(true)//cancel the notification when click head
                .setFullScreenIntent(pendingIntent, true)  //hang the notification
                .build();

        //.setCategory(Notification.CATEGORY_MESSAGE) //version>21
        //.setVisibility(Notification.VISIBILITY_PUBLIC)// can be show in the Locker page >21

        /**
         * 1.VISIBILITY_PRIVATE : 显示基本信息，如通知的图标，但隐藏通知的全部内容；
         * 2.VISIBILITY_PUBLIC : 显示通知的全部内容；
         * 3.VISIBILITY_SECRET : 不显示任何内容，包括图标。
         */

        //5. send notification
        manager.notify(id,notification);
    }
}
