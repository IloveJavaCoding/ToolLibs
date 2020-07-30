package com.example.toollibs.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.toollibs.Activity.Config.Constant;
import com.example.toollibs.Activity.RuntimeExec;
import com.example.toollibs.R;
import com.example.toollibs.SelfClass.FlowInfo;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class SystemUtil {
    private static InputMethodManager inputMethodManager;

    //=================keyboard control===============================
    public static void ShowKeyBoard(EditText et){
        et.setFocusable(true);
        et.requestFocus();
        inputMethodManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(et,InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void CloseKeyBoard(EditText et){
        inputMethodManager.hideSoftInputFromWindow(et.getWindowToken(),0);
    }

    //======================system notices======================
    public static void ShowToast(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static void ShowLongToast(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    //======================window attributes=========================
    public static DisplayMetrics GetScreenDM(Context context) {
        WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);

        return dm;  //dm.widthPixels;  //dm.heightPixels;
    }

    //======================screen cap===========================
    public static void screenCap(Activity activity, String fileName){
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

    public static void screenShot(Activity activity, String fileName){
        String path = FileUtil.GetAppRootPth(activity) + File.separator + fileName;
        RuntimeExec.takeScreenShot(path);
    }

    //===============================reboot========================================
    public static void reBoot(Context context){
        PowerManager pManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        pManager.reboot("recovery");
    }

    public static void reBoot(){
        String cmd = "reboot";
        RuntimeExec.getInstance().executeCommand(cmd);
    }

    //=============================permission check=======================
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

    //=================================vibrator=============================
    public static void vibrator(Context context){
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = am.getRingerMode();
        if (ringerMode != 0) {
            Vibrator vv = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
            vv.vibrate(500L);
        }
    }

    //=========================network type==============================
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

    //=================get system memory info======================
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

    public static long getSystemMemory(Context context, String type){
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(info);

        switch (type) {
            case Constant.TYPE_TOTAL:
                return info.totalMem;
            case Constant.TYPE_FREE:
                return info.availMem;
            case Constant.TYPE_USED:
                return info.totalMem - info.availMem;
        }
        return -1;
    }

    public static boolean isLowMemory(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(info);
        return info.lowMemory;
    }

    //=====================set app language==========================
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

    //==============================横、竖屏 app===================================
    public static void setOrientation(int orientation){
        switch (orientation){
            case Constant.ORIENTATION_LANDSCAPE:
                RuntimeExec.getInstance().executeCommand("setprop persist.sys.screenorientation landscape");
                RuntimeExec.getInstance().executeCommand("reboot");
                break;
            case Constant.ORIENTATION_PORTRAIT:
                RuntimeExec.getInstance().executeCommand("setprop persist.sys.screenorientation portrait");
                RuntimeExec.getInstance().executeCommand("reboot");
                break;
        }
    }

    //=============================================set screen brightness================================================
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

    //=================================================notification=================================================
    public static void sendNotification(Context context, String channelId, Intent intent, String title, String msg, int id){
        //1. get notification service
        NotificationManager manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //2. create notification channel version>26   android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelName = "Default Name";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            //channel.setSound();
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(1);

            manager.createNotificationChannel(channel);
        }

        //3. set intent(active when click the notification)
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //4. init notification, get instance
        Notification notification = new NotificationCompat.Builder(context,channelId)
                .setContentTitle(title) //title
                .setContentText(msg) //message
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)//intent
                .setAutoCancel(true)//cancel the notification when click head
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setFullScreenIntent(pendingIntent, true)  //hang the notification
                //set a picture
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(BitmapUtil.getBitmapFromRes(context,R.drawable.img_bg))
                .bigLargeIcon(null))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.img_bg))

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

    public static void sendPictureNotification(Context context, String channelId, Intent intent, String title, String msg, int id, int resId){
        //1. get notification service
        NotificationManager manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //2. create notification channel version>26   android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelName = "Default Name";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);

            manager.createNotificationChannel(channel);
        }

        //3. set intent(active when click the notification)
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //4. init notification, get instance
        Notification notification = new NotificationCompat.Builder(context,channelId)
                .setContentTitle(title) //title
                .setContentText(msg) //message
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)//intent
                .setAutoCancel(true)//cancel the notification when click head
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setFullScreenIntent(pendingIntent, true)  //hang the notification
                //set a picture
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(BitmapUtil.getBitmapFromRes(context,resId))
                        .bigLargeIcon(null))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), resId))
                .build();

        //5. send notification
        manager.notify(id,notification);
    }


    public static void sendSelfViewNotification(Context context, String channelId, Intent intent, int id, RemoteViews small, RemoteViews big){
        //1. get notification service
        NotificationManager manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //2. create notification channel version>26   android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelName = "Default Name";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);

            manager.createNotificationChannel(channel);
        }

        //3. set intent(active when click the notification)
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //4. init notification, get instance
        Notification notification = new NotificationCompat.Builder(context,channelId)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)//intent
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCustomContentView(small)
                .setCustomBigContentView(big)
                .build();

        //5. send notification
        manager.notify(id,notification);
    }

    public static void showMusicNotification(Context context, String channelId, Intent intent, int id, RemoteViews small, RemoteViews big) {
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)  // the status icon
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .setCustomContentView(small)
                .setCustomBigContentView(big)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true)
                .build();

        // Send the notification.
        //startForeground(id, notification);
    }

    //=================================get apk code and name===============================
    private static PackageInfo getPackageInfo(Context context){
        // 获取package管理者  需要上下文
        PackageManager packageManager = context.getPackageManager();
        //获取包名的方法
        String packageName = context.getPackageName();
        PackageInfo packageInfo = null;
        try {
            //参数一是获取哪一个包名的package的信息 (application包括了activity所以也会包括activity的信息)
            //参数二的信息就是设置是不是之后去
            packageInfo = packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }

    public static String getVersionName(Context context){
        return  getPackageInfo(context).versionName;
    }

    //api > 21
    public static long  getVersionCode(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return getPackageInfo(context).getLongVersionCode();
        }
        return -1;
    }

    //==========================获取APP当月流量消耗情况================================
    public static FlowInfo getAppFlowInfo(String pakageName, Context context) {
        PackageManager pms = context.getPackageManager();
        List<PackageInfo> packinfos = pms.getInstalledPackages(PackageManager.GET_PERMISSIONS);

        FlowInfo flowInfo = new FlowInfo();
        for (PackageInfo packinfo : packinfos) {
            String packageName = packinfo.packageName;
            if (!TextUtils.isEmpty(packageName)) {
                if (packageName.equals(pakageName)) {
                    //用于封装具有Internet权限的应用程序信息
                    //封装应用信息
                    flowInfo.setPackageName(packinfo.packageName);
                    flowInfo.setAppNAme(packinfo.applicationInfo.loadLabel(pms).toString());
                    //获取到应用的uid（user id）
                    int uid = packinfo.applicationInfo.uid;
                    //TrafficStats对象通过应用的uid来获取应用的下载、上传流量信息
                    //发送的 上传的流量byte
                    flowInfo.setUpKb(TrafficStats.getUidRxBytes(uid));
                    //下载的流量 byte
                    flowInfo.setDownKb(TrafficStats.getUidTxBytes(uid));
                    break;
                }
            }
        }
        return flowInfo;
    }

    //==========================静默安装apk============================================
    public static void installApkSilence(final String filePath){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RuntimeExec.getInstance().executeRootCommand(RuntimeExec.INSTALL + filePath);
            }
        }).start();
    }

    //=====================================================================================
    /**
     * 1.PARTIAL_WAKE_LOCK：保证CPU保持高性能运行，而屏幕和键盘背光（也可能是触摸按键的背光）关闭。一般情况下都会使用这个WakeLock。
     * 2.ACQUIRE_CAUSES_WAKEUP：这个WakeLock除了会使CPU高性能运行外还会导致屏幕亮起，即使屏幕原先处于关闭的状态下。
     * 3.ON_AFTER_RELEASE：如果释放WakeLock的时候屏幕处于亮着的状态，则在释放WakeLock之后让屏幕再保持亮一小会。如果释放WakeLock的时候屏幕本身就没亮，则不会有动作
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

}
