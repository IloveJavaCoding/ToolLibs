package com.example.toollibs.Activity;

import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.toollibs.R;

public class Alarm_Activity extends AppCompatActivity {
    private Button bStop, bSnooze;
    private MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_);
        layout_Setting();
        init();
        setListener();
    }

    private void init(){
        bStop = findViewById(R.id.bStop);
        bSnooze = findViewById(R.id.bSnooze);
        player.create(getApplicationContext(), R.raw.bloom_of_youth).start();
    }

    private void layout_Setting(){
        Display display = getWindowManager().getDefaultDisplay();
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        Point size = new Point();
        display.getSize(size);
        layoutParams.gravity = Gravity.TOP;
        layoutParams.width =size.x;
        //layoutParams.height = size.y;
    }

    private void setListener() {
        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.pause();
                exitActivity();
            }
        });

        bSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
    }

    private void exitActivity(){
        this.finish();
        //System.exit(0);
    }
}
