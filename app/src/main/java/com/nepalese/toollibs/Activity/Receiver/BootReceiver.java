package com.nepalese.toollibs.Activity.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.nepalese.toollibs.Activity.Config.SettingData;
import com.nepalese.toollibs.Activity.Service.CoreService;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(TextUtils.isEmpty(intent.getAction())) return;
        if(SettingData.isAutoStart(context)){
            //延迟启动activity
            Message obMessage = handler.obtainMessage();
            obMessage.obj = context;
            handler.sendMessageDelayed(obMessage, 3000);
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Context context = (Context) msg.obj;
            context.startService(CoreService.getIntent(context, Intent.ACTION_BOOT_COMPLETED, ""));
            super.handleMessage(msg);
        }
    };
}
