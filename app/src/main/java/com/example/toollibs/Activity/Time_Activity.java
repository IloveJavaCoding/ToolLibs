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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.toollibs.Activity.Receiver.AlarmReceiver;
import com.example.toollibs.OverWriteClass.PosterView;
import com.example.toollibs.R;
import com.example.toollibs.SelfClass.TimeSelector;
import com.example.toollibs.Util.DateUtil;
import com.example.toollibs.Util.SystemUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.zip.Inflater;

public class Time_Activity extends AppCompatActivity {
    private TextView tvTime, tvClock;
    private RadioButton rbStart;
    private Thread thread;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private final String action = "com.example.ToolLibs.alarm";;
    private final String maxTime = "2020-12-30 23:59";
    private boolean isSet = false;
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
        tvClock = findViewById(R.id.tvClock);

        rbStart = findViewById(R.id.rbConfirm);

        initAlarm();
    }

    private void initAlarm() {
        pendingIntent=PendingIntent.getBroadcast(this,0,getMsgIntent(),0);
        alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    private Intent getMsgIntent() {
        Intent intent=new Intent(this, AlarmReceiver.class);
        intent.setAction(action);
        intent.putExtra("tag","闹钟开启");//tag
        intent.putExtra("time", tvTime.getText().toString());
        return intent;
    }

    private void setData() {
        thread = new Thread(new TimeThread());
        thread.start();
    }

    private void setListener() {
        rbStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    timeSelector();
                }else{
                    alarmManager.cancel(pendingIntent);
                    tvClock.setText(" ");
                }
            }
        });
    }

    private void timeSelector(){
        TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(Date time) {
                tvClock.setText(DateUtil.Date2String(time, "yyyy-MM-dd HH:mm"));

                int[] temp = DateUtil.getYMDHMS_Date(time);
                SystemUtil.ShowToast(getApplicationContext(),"Start alarm.");
                activeAlarm(temp[2],temp[3],temp[4]);
                isSet = true;
            }
        },DateUtil.getCurTime(), maxTime);
        timeSelector.setMode(TimeSelector.MODE.YMDHM);
        timeSelector.show();
        timeSelector.setIsLoop(false);
    }

    private void activeAlarm(int d, int h, int m) {
        if(Build.VERSION.SDK_INT<19){
            alarmManager.set(AlarmManager.RTC_WAKEUP,getTimeDiff(d,h,m),pendingIntent);
        }else{
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,getTimeDiff(d,h,m),pendingIntent);
        }
    }

    private long getTimeDiff(int d, int h, int m) {
        Calendar ca=Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, d);
        ca.set(Calendar.HOUR_OF_DAY,h);
        ca.set(Calendar.MINUTE,m);
        ca.set(Calendar.SECOND, 0);
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
