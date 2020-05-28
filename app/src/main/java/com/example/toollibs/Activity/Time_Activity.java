package com.example.toollibs.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.toollibs.Activity.Receiver.AlarmReceiver;
import com.example.toollibs.R;
import com.example.toollibs.Util.DateUtil;
import com.example.toollibs.Util.SystemUtil;

import java.util.Calendar;

public class Time_Activity extends AppCompatActivity {
    private EditText etHour, etMins;
    private TextView tvTime;
    private Button rbStart;
    private int hour, minute;
    private Thread thread;
    private ToggleButton tbSwitch;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private final String action = "com.example.ToolLibs.alarm";;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_);

        init();
        setData();
        setListener();
    }

    private void init() {
        tvTime = findViewById(R.id.tvTime);
        etHour = findViewById(R.id.etHour);
        etMins = findViewById(R.id.etMinute);

        rbStart = findViewById(R.id.bConfirm);
        tbSwitch = findViewById(R.id.tbSwitch);

        initAlarm();
    }

    private void initAlarm() {
        pendingIntent=PendingIntent.getBroadcast(this,0,getMsgIntent(),0);
        alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    private Intent getMsgIntent() {
        Intent intent=new Intent(this, AlarmReceiver.class);
        intent.setAction(action);
        intent.putExtra("msg","闹钟开启");//tag
        return intent;
    }

    private void setData() {
        thread = new Thread(new TimeThread());
        thread.start();
    }

    private void setListener() {
        rbStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(judge()){
                    //start
                    rbStart.setTextColor(Color.RED);
                    SystemUtil.ShowToast(getApplicationContext(),"Start alarm.");
                    activeAlarm();
                }else{
                    SystemUtil.ShowToast(getApplicationContext(),"Invalidate!!!");
                }
            }
        });

    }

    private boolean judge() {
        String H = etHour.getText().toString();
        String M = etMins.getText().toString();

        if(H!=null && M!=null){
            hour = Integer.parseInt(H);
            minute = Integer.parseInt(M);

            return true;
        }else{
            return false;
        }
    }

    private void activeAlarm() {
        if(Build.VERSION.SDK_INT<19){
            alarmManager.set(AlarmManager.RTC_WAKEUP,getTimeDiff(hour,minute,0),pendingIntent);
        }else{
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,getTimeDiff(hour,minute,0),pendingIntent);
        }
    }

    private long getTimeDiff(int h, int m, int s) {
        Calendar ca=Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY,h);
        ca.set(Calendar.MINUTE,m);
        ca.set(Calendar.SECOND,s);
        return ca.getTimeInMillis();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                Bundle bundle = msg.getData();
                tvTime.setText(bundle.getString("time"));
            }
        }
    };

    public class TimeThread implements Runnable{
        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = Message.obtain();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("time", DateUtil.getCurTime());
                message.setData(bundle);

                handler.sendMessage(message);
            }
        }
    }
}
