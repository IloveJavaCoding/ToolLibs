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
    private Button bCancel;
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
        bCancel = findViewById(R.id.bCancel);
        player.create(getApplicationContext(), R.raw.xiafeng).start();
    }

    private void layout_Setting(){
        Display display = getWindowManager().getDefaultDisplay();
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        Point size = new Point();
        display.getSize(size);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width =size.x;
        layoutParams.height = (int)(size.y*0.4);
    }

    private void setListener() {
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.pause();
                exitActivity();
            }
        });
    }

    private void exitActivity(){
        this.finish();
    }
}
