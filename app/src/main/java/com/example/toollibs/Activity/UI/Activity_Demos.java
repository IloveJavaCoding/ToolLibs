package com.example.toollibs.Activity.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.toollibs.Activity.Demo.Demo_Effect_View_Activity;
import com.example.toollibs.Activity.Demo.Demo_File_Selector_Activity;
import com.example.toollibs.Activity.Demo.Demo_Live_Activity;
import com.example.toollibs.Activity.Demo.Demo_PosterView_Activity;
import com.example.toollibs.Activity.Demo.Demo_Scroll_Lrc_Activity;
import com.example.toollibs.Activity.Demo.Demo_Simple_Player_Activity;
import com.example.toollibs.Activity.Demo.Demo_Time_Activity;
import com.example.toollibs.Activity.Demo.Demo_TimerSelector_Activity;
import com.example.toollibs.R;

public class Activity_Demos extends AppCompatActivity implements View.OnClickListener {
    private Button bAlarm, bPoster, bTimeSelector, bLive, bPlayer, bLrc, bEffect, bFileSelector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__demos);

        init();
        setListener();
    }

    private void init() {
        bAlarm = findViewById(R.id.bAlarm);
        bPoster = findViewById(R.id.bPoster);
        bTimeSelector = findViewById(R.id.bTimeSelectors);
        bLive = findViewById(R.id.bLive);
        bPlayer = findViewById(R.id.bSimplePlayer);
        bLrc = findViewById(R.id.bLrc);
        bEffect = findViewById(R.id.bEffectView);
        bFileSelector = findViewById(R.id.bFileSelector);
    }

    private void setListener() {
        bAlarm.setOnClickListener(this);
        bPoster.setOnClickListener(this);
        bTimeSelector.setOnClickListener(this);
        bLive.setOnClickListener(this);
        bPlayer.setOnClickListener(this);
        bLrc.setOnClickListener(this);
        bEffect.setOnClickListener(this);
        bFileSelector.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()){
            case R.id.bPoster:
                intent = new Intent(this, Demo_PosterView_Activity.class);
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

        }
        startActivity(intent);
    }
}