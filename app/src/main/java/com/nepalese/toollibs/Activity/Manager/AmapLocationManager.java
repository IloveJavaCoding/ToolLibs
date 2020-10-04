package com.nepalese.toollibs.Activity.Manager;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * @author nepalese on 2020/9/23 16:29
 * @usage
 */
public class AmapLocationManager {
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption;
    private boolean hasStart = false;
    private Context mContext;

    public AmapLocationManager(Context mContext) {
        this.mContext = mContext;
        initLocation();
    }
    /**
     * 初始化定位
     */
    private void initLocation(){
        if (locationClient == null) {
            //初始化client
            locationClient = new AMapLocationClient(mContext);
        }
    }
    /**
     * 开始定位
     */
    public void startLocation(AMapLocationListener locationListener){
        if (locationClient != null && !hasStart){
            if (locationOption == null) {
                locationOption = getDefaultOption();
            }
            //设置定位参数
            locationClient.setLocationOption((locationOption));
            // 设置定位监听
            locationClient.setLocationListener(locationListener);
            // 启动定位
            locationClient.startLocation();
            hasStart = true;
        }
    }

    /**
     * 停止定位
     */
    public void stopLocation(){
        if (hasStart && locationClient != null){
            // 停止定位
            locationClient.stopLocation();
            hasStart = false;
        }
    }

    /**
     * 销毁定位
     */
    public void destroyLocation(){
        if (null != locationClient) {
            /*
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(3000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        mOption.setMockEnable(false);//设置是否允许模拟位置,默认为false，不允许模拟位置
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

}
