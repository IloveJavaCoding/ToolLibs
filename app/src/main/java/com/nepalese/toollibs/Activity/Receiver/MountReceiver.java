package com.nepalese.toollibs.Activity.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.nepalese.toollibs.Activity.Service.CoreService;

/**
 * @author nepalese on 2020/9/23 16:56
 * @usage 监听U盘 插入，拔出
 */
public class MountReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) return;

        String path = intent.getData() == null ? "" : intent.getData().getPath();
        context.startService(CoreService.getIntent(context, action, path));
    }
}