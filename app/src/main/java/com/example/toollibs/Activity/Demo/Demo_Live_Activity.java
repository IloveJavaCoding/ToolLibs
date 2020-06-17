package com.example.toollibs.Activity.Demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.example.toollibs.Activity.Config.Constant;
import com.example.toollibs.R;
import com.example.toollibs.Util.SystemUtil;

public class Demo_Live_Activity extends AppCompatActivity {
    private SurfaceView surfaceView;
    private Button bStart, bStop;

    private int wight, high;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo__live);

        init();
        setData();
        setListener();
    }

    private void init() {
        surfaceView = findViewById(R.id.surfaceView);
        bStart = findViewById(R.id.bStart);
        bStop = findViewById(R.id.bStop);
    }

    private void setData() {
        DisplayMetrics dm = SystemUtil.GetScreenDM(getApplicationContext());
        wight = dm.widthPixels;
        high = dm.heightPixels;
    }

    private void setListener() {
        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlayDTMB();
            }
        });

        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlayDTMB();
            }
        });
    }

    private void startPlayDTMB(){
        HdiPlayer.initPlay();
        LiveSource source = null;
        if (source==null){
            source = Constant.DEFAULT_LIVE_SOURCE;
        }
        Log.i("tag", "startPlayDTMB: source="+source);
        HdiPlayer.startPlay(1,
                source.getFrequency(),source.getAudPid(),source.getVidPid(),source.getVidPid(),source.getVidType(),source.getAudType(),
                0,0, wight, high);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemUtil.bootResetVolume(getApplicationContext());
            }
        }).start();
    }

    private void stopPlayDTMB(){
        HdiPlayer.stopPlay(0);
        HdiPlayer.termPlay();
    }
}
