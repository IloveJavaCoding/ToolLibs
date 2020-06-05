package com.example.toollibs.Activity.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.toollibs.Activity.Config.Constant;
import com.example.toollibs.Activity.Config.SettingData;
import com.example.toollibs.Activity.MainActivity;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) &&
                SettingData.getBoolean(context, Constant.CONFIG_FILE, Constant.AUTO_START_KEY, Constant.AUTO_START_DEFAULT)){
            startApp(context);
        }
    }

    private void startApp(Context context){
        Intent thisIntent = new Intent(context, MainActivity.class);
        thisIntent.setAction("android.intent.action.MAIN");
        thisIntent.addCategory("android.intent.category.LAUNCHER");
        thisIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(thisIntent);
    }
}
