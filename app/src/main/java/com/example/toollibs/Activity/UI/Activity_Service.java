package com.example.toollibs.Activity.UI;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.toollibs.Activity.Service.PlayerService;
import com.example.toollibs.R;
import com.example.toollibs.Util.FileUtil;

import java.io.IOException;

public class Activity_Service extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "service_activity";
    private ToggleButton tbStatue;
    private ImageView player1, player2;
    private TextView tvLog;

    private String log;
    private PlayerService service;
    private ServiceConnection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__service);

        init();
        setData();
        setListener();
    }

    private void init() {
        tbStatue = findViewById(R.id.tbStatue);
        player1 = findViewById(R.id.imgPlay);
        player2 = findViewById(R.id.imgPlay2);
        tvLog = findViewById(R.id.tvLog);
    }

    private void setData() {
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder iBinder) {
                Log.d(TAG, "service connected...");
                showLog("service connected...");
                PlayerService.PlayerBinder binder = (PlayerService.PlayerBinder) iBinder;
                service = binder.getPlayerService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "service disconnected...");
                showLog("service disconnected...");
                service = null;
            }
        };
    }

    private void setListener() {
        tbStatue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            Intent intent = new Intent(getApplicationContext(), PlayerService.class);
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //bind service
                    Log.d(TAG, "bind service...");
                    showLog("bind service...");
                    bindService(intent, connection, Service.BIND_AUTO_CREATE);
                }else{
                    //unbind service
                    Log.d(TAG, "unbind service...");
                    showLog("unbind service...");
                    unbindService(connection);
                }
            }
        });

        player1.setOnClickListener(this);
        player2.setOnClickListener(this);
    }

    private void showLog(String str){
        log += str+ "\n";
        tvLog.setText(log);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imgPlay:
                player1.setImageResource(R.drawable.img_video_play);
                service.play();
//                Intent intent = new Intent(getApplicationContext(), PlayerService.class);
//                startService(intent);
                break;
            case R.id.imgPlay2:
                Intent intent1 = new Intent(getApplicationContext(), PlayerService.class);
                stopService(intent1);
                break;
        }
    }
}