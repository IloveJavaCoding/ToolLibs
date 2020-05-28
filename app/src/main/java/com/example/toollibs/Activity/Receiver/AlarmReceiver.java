package com.example.toollibs.Activity.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.toollibs.Activity.Alarm_Activity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //SystemUtil.ShowToast(context, "Alarming");
        showDialog(context,intent);
    }

    private void showDialog(Context context, Intent intent){
        Bundle bundle = intent.getExtras();
        intent = new Intent(context, Alarm_Activity.class);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
