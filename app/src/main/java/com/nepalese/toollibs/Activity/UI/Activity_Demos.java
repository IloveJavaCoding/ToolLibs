package com.nepalese.toollibs.Activity.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.nepalese.toollibs.Activity.Demo.Demo_QR_Code_Activity;
import com.nepalese.toollibs.Activity.Demo.Demo_TimingTask_Activity;
import com.nepalese.toollibs.Activity.Demo.Demo_Effect_View_Activity;
import com.nepalese.toollibs.Activity.Demo.Demo_File_Selector_Activity;
import com.nepalese.toollibs.Activity.Demo.Demo_Get_Weather_Activity;
import com.nepalese.toollibs.Activity.Demo.Demo_Live_Activity;
import com.nepalese.toollibs.Activity.Demo.Demo_Loop_Task_Activity;
import com.nepalese.toollibs.Activity.Demo.Demo_PosterView_Activity;
import com.nepalese.toollibs.Activity.Demo.Demo_Scroll_Lrc_Activity;
import com.nepalese.toollibs.Activity.Demo.Demo_Simple_Player_Activity;
import com.nepalese.toollibs.Activity.Demo.Demo_Simple_Reader_Activity;
import com.nepalese.toollibs.Activity.Demo.Demo_Time_Activity;
import com.nepalese.toollibs.Activity.Demo.Demo_TimerSelector_Activity;
import com.nepalese.toollibs.Demo_Text_2_Voice;
import com.nepalese.toollibs.R;

public class Activity_Demos extends AppCompatActivity implements View.OnClickListener {
    private Button bAlarm, bTimingTask, bLoopTask, bPoster, bTimeSelector, bLive, bPlayer,
            bLrc, bEffect, bReader, bFileSelector, bWeather, bQRCode, bText2Voice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__demos);

        init();
        setListener();
    }

    private void init() {
        bAlarm = findViewById(R.id.bAlarm);
        bTimingTask = findViewById(R.id.bTimingTask);
        bLoopTask = findViewById(R.id.bLoopTask);
        bPoster = findViewById(R.id.bPoster);
        bTimeSelector = findViewById(R.id.bTimeSelectors);
        bLive = findViewById(R.id.bLive);

        bPlayer = findViewById(R.id.bSimplePlayer);
        bLrc = findViewById(R.id.bLrc);
        bEffect = findViewById(R.id.bEffectView);
        bReader = findViewById(R.id.bReader);
        bFileSelector = findViewById(R.id.bFileSelector);
        bWeather = findViewById(R.id.bWeather);
        bQRCode = findViewById(R.id.bQRCode);
        bText2Voice = findViewById(R.id.bText2Voice);
    }

    private void setListener() {
        bAlarm.setOnClickListener(this);
        bTimingTask.setOnClickListener(this);
        bLoopTask.setOnClickListener(this);
        bPoster.setOnClickListener(this);
        bTimeSelector.setOnClickListener(this);
        bLive.setOnClickListener(this);

        bPlayer.setOnClickListener(this);
        bLrc.setOnClickListener(this);
        bEffect.setOnClickListener(this);
        bReader.setOnClickListener(this);
        bFileSelector.setOnClickListener(this);
        bWeather.setOnClickListener(this);
        bQRCode.setOnClickListener(this);
        bText2Voice.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()){
            case R.id.bPoster:
                intent = new Intent(this, Demo_PosterView_Activity.class);
                break;
            case R.id.bTimingTask:
                intent = new Intent(this, Demo_TimingTask_Activity.class);
                break;
            case R.id.bLoopTask:
                intent = new Intent(this, Demo_Loop_Task_Activity.class);
                break;
            case R.id.bTimeSelectors:
                intent = new Intent(this, Demo_TimerSelector_Activity.class);
                break;
            case R.id.bAlarm:
                intent = new Intent(this, Demo_Time_Activity.class);
                break;

            case R.id.bLive:
                intent = new Intent(this, Demo_Live_Activity.class);
                break;
            case R.id.bSimplePlayer:
                intent = new Intent(this, Demo_Simple_Player_Activity.class);
                break;
            case R.id.bLrc:
                intent = new Intent(this, Demo_Scroll_Lrc_Activity.class);
                break;
            case R.id.bFileSelector:
                intent = new Intent(this, Demo_File_Selector_Activity.class);
                intent.putExtra("flag", 0);
                break;
            case R.id.bEffectView:
                intent = new Intent(this, Demo_Effect_View_Activity.class);
                break;
            case R.id.bReader:
                intent = new Intent(this, Demo_Simple_Reader_Activity.class);
                break;
            case R.id.bWeather:
                intent = new Intent(this, Demo_Get_Weather_Activity.class);
                break;
            case R.id.bQRCode:
                intent = new Intent(this, Demo_QR_Code_Activity.class);
                break;
            case R.id.bText2Voice:
                intent = new Intent(this, Demo_Text_2_Voice.class);
                break;

        }
        startActivity(intent);
    }
}