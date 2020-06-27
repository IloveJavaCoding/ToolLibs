package com.example.toollibs.Activity.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.toollibs.Activity.Config.Constant;
import com.example.toollibs.R;

import java.io.IOException;

public class AlarmService extends Service {
    private static final String TAG = "AlarmServices";
    private MediaPlayer mediaPlayer;

    private static final String ACTION_KEY = "extra_action";
    private static final String DATA_KEY = "extra_date";

    public static Intent getIntent(Context context, String action, String extras) {
        Intent intent = new Intent();
        intent.setClass(context, AlarmService.class);
        intent.putExtra(ACTION_KEY, action);
        intent.putExtra(DATA_KEY, extras);
        return intent;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "start service...");
        super.onCreate();
        //init the media player
        //mediaPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent == null ? null : intent.getExtras();
        if (extras != null) {
            doCheck(extras.getString(ACTION_KEY), extras.getString(DATA_KEY));
        }
        return START_STICKY;
    }

    private void doCheck(String action, String data) {
        if (TextUtils.isEmpty(action)) return;
        if (action.equals(Constant.ACTION_RESET_SOUND)){
            Log.d(TAG, "reset sound path...");
            if(data.equals(Constant.ALARM_SOUND_DEFAULT)){
                mediaPlayer = MediaPlayer.create(this, R.raw.bloom_of_youth);
            }else{
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(data);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mediaPlayer.start();
        }
    }

    @Override
    public void onDestroy() {
        if(mediaPlayer!=null){
            mediaPlayer.reset();
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Log.d(TAG, "end service...");
        super.onDestroy();
    }
}
