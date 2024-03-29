package com.nepalese.toollibs.Activity.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.nepalese.toollibs.Activity.Config.Constant;
import com.nepalese.toollibs.Activity.Config.SettingData;
import com.nepalese.toollibs.Activity.Demo.Demo_Alarm_Activity;
import com.nepalese.toollibs.Activity.Service.AlarmService;
import com.nepalese.toollibs.Util.NotificationUtil;
import com.nepalese.toollibs.Util.SystemUtil;

public class AlarmReceiver extends BroadcastReceiver {
    private Context context;
    private String sound;
    private final int START_SERVICE_CODE = 0x001;
    private final int STOP_SERVICE_CODE = 0x002;
    private final int AUTO_CLOSE_CODE = 0x003;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        showNotification(context, intent);
        SettingData.saveAlarmState(context, false);
        //handler.sendEmptyMessage(AUTO_CLOSE_CODE);
    }

    private void showNotification(Context context, Intent intent){
        Bundle bundle = intent.getExtras();
        String time = bundle.getString("time");
        sound = bundle.getString("sound");

        intent = new Intent(context, Demo_Alarm_Activity.class);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        NotificationUtil.sendNotification(context, "channel1", intent, "Alarm", time, 1);
        //play music
        handler.sendEmptyMessage(START_SERVICE_CODE);
    }

    private void startServices(Context context){
        context.startService(AlarmService.getIntent(context, Constant.ACTION_RESET_SOUND, sound));
    }

    private void stopServices(Context context){
        Intent intent = new Intent(context, AlarmService.class);
        context.stopService(intent);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case START_SERVICE_CODE:
                    startServices(context);
                    break;
                case STOP_SERVICE_CODE:
                    stopServices(context);
                    break;
                case AUTO_CLOSE_CODE:
                    autoClose(2);
                    break;
            }
        }
    };

    private void autoClose(final int minute){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(minute*1000*60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(STOP_SERVICE_CODE);
            }
        }.start();
    }
}
