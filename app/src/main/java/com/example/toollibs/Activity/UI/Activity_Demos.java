package com.example.toollibs.Activity.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.toollibs.Activity.Demo.Demo_PosterView_Activity;
import com.example.toollibs.Activity.Demo.Demo_Time_Activity;
import com.example.toollibs.Activity.Demo.Demo_TimerSelector_Activity;
import com.example.toollibs.R;

public class Activity_Demos extends AppCompatActivity implements View.OnClickListener {
    private Button bAlarm, bPoster, bTimeSelector, bLive;
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
    }

    private void setListener() {
        bAlarm.setOnClickListener(this);
        bPoster.setOnClickListener(this);
        bTimeSelector.setOnClickListener(this);
        bLive.setOnClickListener(this);
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
                intent = new Intent(this, Demo_Time_Activity.class);
                break;
        }
        startActivity(intent);
    }
}