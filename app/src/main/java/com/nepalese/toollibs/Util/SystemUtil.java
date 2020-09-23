package com.nepalese.toollibs.Util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.nepalese.toollibs.Activity.Config.Constant;
import com.nepalese.toollibs.Activity.RuntimeExec;
import com.nepalese.toollibs.Bean.FlowInfo;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class SystemUtil {
    //======================system notices======================
    public static void showToast(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    //===============================reboot========================================
    //重启应用
    public static void reBoot(Context context){
        PowerManager pManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        pManager.reboot("recovery");
    }

    //重启应用
    public static void restartApp(Context context) {
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//Intent.FLAG_ACTIVITY_CLEAR_TASK;
        context.startActivity(intent);
    }

    //重启系统
    public static void reBoot(){
        String cmd = "reboot";
        RuntimeExec.getInstance().executeCommand(cmd);
    }

    //=============================permission check=======================
    public static boolean checkPermission(Context context, String[] permissions){
        if (permissions == null || permissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : permissions) {
            allGranted &= ContextCompat.checkSelfPermission(context, neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }

    //==========================================vibrator=============================
    public static void vibrator(Context context){
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = am.getRingerMode();
        if (ringerMode != 0) {
            Vibrator vv = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
            vv.vibrate(500L);
        }
    }

    //================================get system memory info====================================
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
    public static FlowInfo getDataFlowInfo(String packageName, Context context) {
        PackageManager pms = context.getPackageManager();
        List<PackageInfo> packinfos = pms.getInstalledPackages(PackageManager.GET_PERMISSIONS);

        FlowInfo flowInfo = new FlowInfo();
        for (PackageInfo packinfo : packinfos) {
            String packName = packinfo.packageName;
            if (!TextUtils.isEmpty(packName)) {
                if (packName.equals(packageName)) {
                    //用于封装具有Internet权限的应用程序信息
                    //封装应用信息
                    flowInfo.setPackageName(packName);
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
