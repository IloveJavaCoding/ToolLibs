package com.nepalese.toollibs.Activity.Demo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nepalese.toollibs.Activity.Config.SettingData;
import com.nepalese.toollibs.Activity.Receiver.AlarmReceiver;
import com.nepalese.toollibs.R;
import com.nepalese.toollibs.Activity.ComponentThird.TimeSelector;
import com.nepalese.toollibs.Util.DateUtil;
import com.nepalese.toollibs.Util.IntentUtil;

import java.util.Calendar;
import java.util.Date;

public class Demo_Time_Activity extends AppCompatActivity {
    private TextView tvTime, tvAlarm, tvSound;
    private RadioButton rbStart;//control the statue of alarm
    private Thread thread;//update time

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Date date;

    private String soundName;
    private String alarmTime;

    private final String action = "com.example.ToolLibs.alarm";;
    private final String maxTime = "2020-12-30 23:59";
    private final int OPEN_AUDIO_CODE = 0x001;

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
        tvAlarm = findViewById(R.id.tvAlarm);
        tvSound = findViewById(R.id.tvSound);

        rbStart = findViewById(R.id.rbConfirm);
        alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    private Intent getMsgIntent() {
        Intent intent=new Intent(this, AlarmReceiver.class);
        intent.setAction(action);
        intent.putExtra("tag","闹钟开启");//tag
        intent.putExtra("time", alarmTime);
        intent.putExtra("sound", soundName);
        return intent;
    }

    private void setData() {
        thread = new Thread(new TimeThread());
        thread.start();

        //read history setting
        tvAlarm.setText(SettingData.getAlarmTime(this));
        boolean state = SettingData.getAlarmState(this);
        rbStart.setEnabled(state);
        rbStart.setChecked(state);
        soundName = SettingData.getAlarmSound(this);
        tvSound.setText(getSoundName(soundName));
    }

    private void setListener() {
        tvAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeSelector();
            }
        });

        rbStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),0,getMsgIntent(),0);
                    int[] temp = DateUtil.getYMDHMS_Date(date);
                    activeAlarm(temp[2],temp[3],temp[4]);
                    Log.d("tag", "Start alarm...");
                    SettingData.saveAlarmState(getApplicationContext(), true);
                }else{
                    alarmManager.cancel(pendingIntent);
                    SettingData.saveAlarmState(getApplicationContext(), false);
                }
            }
        });

        tvSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //judge to pick music
                IntentUtil.readAudioFile(Demo_Time_Activity.this, OPEN_AUDIO_CODE);
            }
        });
    }

    private void timeSelector(){
        TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(Date time) {
                date = time;
                alarmTime = DateUtil.date2String(time, "yyyy-MM-dd HH:mm");
                tvAlarm.setText(alarmTime);
                Log.d("tag", "save time......");
                SettingData.saveAlarmTime(getApplicationContext(), alarmTime);
                rbStart.setEnabled(true);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Uri uri = data.getData();
        ContentResolver contentResolver = this.getContentResolver();

        if(requestCode==OPEN_AUDIO_CODE && resultCode==RESULT_OK){
            soundName = IntentUtil.getRealPath4Uri(this, uri, contentResolver);
            Log.d("tag", "choose sound " + soundName);
            tvSound.setText(getSoundName(soundName));
            SettingData.saveAlarmSound(getApplicationContext(), soundName);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getSoundName(String path) {
        return path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("."));
    }
}
