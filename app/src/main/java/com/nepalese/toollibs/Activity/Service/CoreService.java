package com.nepalese.toollibs.Activity.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.nepalese.toollibs.Activity.Events.DownloadResourceEvent;
import com.nepalese.toollibs.Activity.MainActivity;
import com.nepalese.toollibs.Activity.Manager.AmapLocationManager;
import com.nepalese.toollibs.Bean.DownloadItem;
import com.nepalese.toollibs.Util.DateUtil;
import com.nepalese.toollibs.Util.Helper.DownloadHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;

/**
 * @author nepalese on 2020/9/23 15:10
 * @usage 核心服务，贯穿整个程序
 */
public class CoreService extends Service {
    private static final String TAG = "CoreService";
    private static final String ACTION_KRY = "extra_action";
    private static final String DATA_KRY = "extra_data";

    private Context context;
    private MyHandler myHandler;
    private AmapLocationManager locationManager;

    private double mLng;//经度
    private double mLat;//纬度
    private String mAddress = "";
    private int mLocationFailCount = 0;

    /**
     * 供外部调用开启服务接口: context.startService(CoreService.getIntent(context, Intent.ACTION_BOOT_COMPLETED, ""));
     * @param context
     * @param action 行为识别
     * @param data 传递的数据
     * @return intent
     */
    public static Intent getIntent(Context context, String action, String data){
        Intent intent = new Intent();
        intent.setClass(context, CoreService.class);
        intent.putExtra(ACTION_KRY, action);
        intent.putExtra(DATA_KRY, data);
        return  intent;
    }

    public class CoreServiceBinder extends Binder{
        public CoreService getService(){
            return CoreService.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new CoreServiceBinder();
    }

    /**
     * 仅在首次启动该服务时调用
     */
    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        myHandler = new MyHandler(this);

        registerEventBus();
        startLocation();
    }

    /**
     * 每次调用startService（this）调用
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent == null ? null : intent.getExtras();
        if (bundle != null) {
            judgeAction(bundle.getString(ACTION_KRY), bundle.getString(DATA_KRY));
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterEventBus();
        stopLocation();
    }

    //=====================================private methods=========================================
    //根据ACTION值来区分处理对应事件
    private void judgeAction(String action, String data) {
        if(TextUtils.isEmpty(action)){
            return;
        }

        switch (action){
            case Intent.ACTION_BOOT_COMPLETED://开机自启
                startMainActivity();
                break;
            case Intent.ACTION_MEDIA_MOUNTED://U盘插入
                onMounted(data);
                break;
            case Intent.ACTION_MEDIA_UNMOUNTED://U盘拔出
            case Intent.ACTION_MEDIA_REMOVED:
            case Intent.ACTION_MEDIA_EJECT:
                onUnMounted(data);
                break;
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    //@param data 插入 U 盘根目录
    private void onMounted(String data) {
        Log.d(TAG, "onMounted: " + data);
        //todo
    }

    private void onUnMounted(String data) {
        //todo
    }

    //开启定位
    private void startLocation() {
        if (true) {
            if (locationManager == null) {
                locationManager = new AmapLocationManager(context);
            }
            locationManager.startLocation(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    if (aMapLocation != null) {
                        if (aMapLocation.getErrorCode() == 0) {//定位成功。
                            //同步时间
                            long time = aMapLocation.getTime();
                            if (time != 0) {
                                Log.i(TAG, "onLocationChanged: GPS SET TIME-------time=" + time);
                                DateUtil.setTime(time);
                            }

                            //即时更新位置信息
                            if (aMapLocation.getLatitude() != mLat || aMapLocation.getLongitude() != mLng) {
                                mLat = aMapLocation.getLatitude();
                                mLng = aMapLocation.getLongitude();
                                Log.i(TAG, "Lat: " + mLat + "\tlng: " + mLng);
                            }

                            mAddress = aMapLocation.getAddress();
                            Log.d(TAG, "address: " + mAddress);
                        } else {
                            //error code == 7
                            mLocationFailCount++;
                            if (mLocationFailCount >= 10) {
                                mLocationFailCount = 0;
                                //定位失败，那么关闭
                                stopLocation();
                            }
                            Log.e(TAG, "location Error, ErrCode: " + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                        }
                    }
                }
            });
        }
    }

    private void stopLocation() {
        try {
            if (locationManager != null) {
                locationManager.stopLocation();
                locationManager.destroyLocation();
                locationManager = null;
            }
        } catch (Throwable e) {
            Log.e(TAG, "停止定位异常: " + e);
        }
    }

    //下载资源
    private void downloadFile(DownloadItem item){
        if(item!=null){
            DownloadHelper.downloadFile(item.getUrl(), item.getFileName(), item.getSavePath());
        }
    }

    //=====================================main thread=============================================
    private void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void unRegisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void post(Object object) {
        EventBus.getDefault().post(object);
    }

    @Subscribe
    public void onEventMainThread(DownloadResourceEvent event){
        downloadFile(event.getItem());
    }

    //====================================handler==================================================
    private static class MyHandler extends Handler{
        WeakReference<CoreService> reference;
        public MyHandler(CoreService coreService){
            reference = new WeakReference<>(coreService);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CoreService coreService = reference.get();
            if(coreService != null){
                switch (msg.what){
                    case 0:
                        break;
                }
            }
        }
    }
}
