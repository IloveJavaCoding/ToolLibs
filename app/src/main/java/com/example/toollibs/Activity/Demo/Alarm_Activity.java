package com.example.toollibs.Activity.Demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.toollibs.Activity.Config.Constant;
import com.example.toollibs.Activity.Service.PlayerService;
import com.example.toollibs.R;
import com.example.toollibs.Util.DateUtil;

import java.util.Calendar;
import java.util.Date;

public class Alarm_Activity extends AppCompatActivity {
    private Button bStop, bSnooze;
    private TextView tvCurTime, tvCurDate, tvTag;

    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_2);
        //layout_Setting();

        getData();
        init();
        setDate();
        setListener();
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        tag = bundle.getString("tag");
        Log.d("tag", "get into alarm activity...");
    }

    private void init(){
        bStop = findViewById(R.id.bStopAlarm);
        bSnooze = findViewById(R.id.bSnoozeAlarm);

        tvCurTime = findViewById(R.id.tvCurTimeAlarm);
        tvCurDate = findViewById(R.id.tvCurDateAlarm);
        tvTag = findViewById(R.id.tvTagAlarm);
    }

    private void setDate(){
        Date date = Calendar.getInstance().getTime();
        int[] arr = DateUtil.getYMDHMS_Date(date);

        String hour, minute;
        if(arr[3]<10){
            hour = "0" + arr[3];
        }else{
            hour = arr[3] + "";
        }

        if(arr[4]<10){
            minute = "0" + arr[4];
        }else{
            minute = arr[4] + "";
        }
        tvCurTime.setText(hour + ":" + minute);
        tvCurDate.setText(Constant.WEEK[arr[6]-1] + ", " + arr[2] + " "+ Constant.MONTH[arr[1]-1]);
        tvTag.setText(tag+"...");
    }

    private void layout_Setting(){
        Display display = getWindowManager().getDefaultDisplay();
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        Point size = new Point();
        display.getSize(size);
        layoutParams.gravity = Gravity.TOP;
        layoutParams.width =size.x;
        layoutParams.height = size.y;
    }

    private void setListener() {
        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopServices(getApplicationContext());
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

    private void stopServices(Context context){
        Intent intent = new Intent(context, PlayerService.class);
        context.stopService(intent);
    }

    private void exitActivity(){
        //this.finish();
        System.exit(0);
    }
}
