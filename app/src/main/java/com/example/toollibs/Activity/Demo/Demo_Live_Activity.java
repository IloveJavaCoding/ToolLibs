package com.example.toollibs.Activity.Demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.toollibs.Activity.Config.Constant;
import com.example.toollibs.R;
import com.example.toollibs.Util.ScreenUtil;
import com.example.toollibs.Util.SystemUtil;
import com.example.toollibs.Util.VolumeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Demo_Live_Activity extends AppCompatActivity {
    private Button bStart, bStop, bNext;
    private TextView tvCity;

    private List<LiveSource> list;
    private int curIndex;
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
        bStart = findViewById(R.id.bStart);
        bStop = findViewById(R.id.bStop);
        bNext = findViewById(R.id.bNext);
        tvCity = findViewById(R.id.tvCity);
    }

    private void setData() {
        DisplayMetrics dm = ScreenUtil.getScreenDM(getApplicationContext());
        wight = dm.widthPixels;
        high = dm.heightPixels;

        list = new ArrayList<>(Arrays.asList(Constant.LIVE_SOURCES));
        curIndex = 0;
    }

    private void setListener() {
        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlayDTMB(list.get(curIndex));
            }
        });

        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlayDTMB();
                if(curIndex>=list.size()){
                    curIndex=0;
                }else{
                    curIndex++;
                }
                startPlayDTMB(list.get(curIndex));
            }
        });

        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlayDTMB();
            }
        });
    }

    private void startPlayDTMB(LiveSource source){
        tvCity.setText(source.getCity());
        HdiPlayer.initPlay();
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
                VolumeUtil.bootResetVolume(getApplicationContext());
            }
        }).start();
    }

    private void stopPlayDTMB(){
        tvCity.setText("");
        HdiPlayer.stopPlay(0);
        HdiPlayer.termPlay();
    }
}
