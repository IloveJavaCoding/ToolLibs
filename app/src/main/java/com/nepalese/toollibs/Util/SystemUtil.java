package com.nepalese.toollibs.Util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nepalese.toollibs.Activity.Config.Constant;
import com.nepalese.toollibs.Activity.RuntimeExec;
import com.nepalese.toollibs.Bean.AppInfo;
import com.nepalese.toollibs.Bean.FlowInfo;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SystemUtil {
    private static final String TAG = "SystemUtil";
    //=================================system notices=====================================
    public static void showToast(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    public static void setBackground(View view, int res_id) {
        int padding_left = view.getPaddingLeft();
        int padding_right = view.getPaddingRight();
        int padding_top = view.getPaddingTop();
        int padding_bottom = view.getPaddingBottom();
        view.setBackgroundResource(res_id);
        view.setPadding(padding_left, padding_top, padding_right, padding_bottom);
    }

    //==================================同步时间==========================================
    private void timeSynchronization(Date ServiceTime){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime=sdf.format(ServiceTime);
        Log.i(TAG, "timeSynchronization: " + datetime);

        ArrayList<String> envlist = new ArrayList<>();
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            envlist.add(envName + "=" + env.get(envName));
        }
        String[] envp = envlist.toArray(new String[0]);
        String command;
        command = "date -s\""+datetime+"\"";
        try {
            Runtime.getRuntime().exec(new String[] { "su", "-c", command }, envp);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    //重启系统
    public static void reBoot(){
        String cmd = "reboot";
        RuntimeExec.getInstance().executeCommand(cmd);
    }

    //===================================permission check==================================
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

    //=======================================vibrator=============================
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

    /**
     * 获取apk包的信息：版本号，名称，图标等
     * @param absPath apk包的绝对路径
     * @param context
     */
    public static AppInfo getApkInfo(String absPath, Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES);
        if (pkgInfo != null) {
            ApplicationInfo appInfo = pkgInfo.applicationInfo;
            appInfo.sourceDir = absPath;
            appInfo.publicSourceDir = absPath;
            AppInfo app = new AppInfo();
            app.setName(pm.getApplicationLabel(appInfo).toString());
            app.setPackageName(appInfo.packageName);
            app.setVersionName(pkgInfo.versionName);
            app.setVersionCode(pkgInfo.versionCode);
            app.setIcon(pm.getApplicationIcon(appInfo));
            return app;
        }
        return null;
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

    //===============================获取本地IP==================================
    public static String getIpAddress(Context context) {
        ConnectivityManager conMann = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetworkInfo = conMann.getNetworkInfo(0);
        NetworkInfo wifiNetworkInfo = conMann.getNetworkInfo(1);

        try {
            Enumeration enumerationNi = NetworkInterface.getNetworkInterfaces();

            label43:
            while(true) {
                NetworkInterface networkInterface;
                String interfaceName;
                do {
                    if (!enumerationNi.hasMoreElements()) {
                        break label43;
                    }

                    networkInterface = (NetworkInterface)enumerationNi.nextElement();
                    interfaceName = networkInterface.getDisplayName();
                } while(!interfaceName.equals("eth0"));

                Enumeration enumIpAddr = networkInterface.getInetAddresses();

                while(enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        }

        String ip;
        if (wifiNetworkInfo.isConnected()) {
            WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = intToIp(ipAddress);
        } else if (mobileNetworkInfo.isConnected()) {
            ip = getLocalIpAddress();
        } else {
            ip = "unknown";
        }

        return ip;
    }

    private static String getLocalIpAddress() {
        try {
            ArrayList<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
            Iterator var1 = nilist.iterator();

            while(var1.hasNext()) {
                NetworkInterface ni = (NetworkInterface)var1.next();
                ArrayList<InetAddress> ialist = Collections.list(ni.getInetAddresses());
                Iterator var4 = ialist.iterator();

                while(var4.hasNext()) {
                    InetAddress address = (InetAddress)var4.next();
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (SocketException var6) {
        }

        return "unknown";
    }

    private static String intToIp(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 255).append(".");
        sb.append(ipInt >> 8 & 255).append(".");
        sb.append(ipInt >> 16 & 255).append(".");
        sb.append(ipInt >> 24 & 255);
        return sb.toString();
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

    //==========================静默安装apk============================================
    public static void installApkSilence(final String filePath){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RuntimeExec.getInstance().executeRootCommand(RuntimeExec.INSTALL + filePath);
            }
        }).start();
    }

    public static void installApk(Context context, File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
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
