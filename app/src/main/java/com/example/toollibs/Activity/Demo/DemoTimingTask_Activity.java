package com.example.toollibs.Activity.Demo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.toollibs.R;
import com.example.toollibs.Util.DateUtil;
import com.example.toollibs.Util.DialogUtil;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class DemoTimingTask_Activity extends AppCompatActivity implements View.OnClickListener {
    private final String ACTION_TASK = "com.example.toollibs.alarm.task";
    private final String OUTCOME = "完成任务！";
    private final String FORMAT = "yyyy-mm-dd hh:MM:ss";

    private final int MSG_CODE_TASK = 0;
    private final int REQUEST_CODE_ALARM = 1;

    private Context context;
    private TextView tvTime, tvLog;
    private Button bTimer, bThread, bHandler, bAlarm;

    private int hour, min;
    private StringBuilder builder;
    private boolean isRegister = false;//记录是否开启receiver
    private boolean cancelTask = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_timing_task);

        init();
        setListener();
        registerReceiver();//开启alarm监听
    }

    private void init() {
        context = getApplicationContext();
        builder = new StringBuilder();

        tvTime = findViewById(R.id.tvTime);
        tvLog = findViewById(R.id.tvLog);

        bTimer = findViewById(R.id.bTimer);
        bThread = findViewById(R.id.bThread);
        bHandler = findViewById(R.id.bHandler);
        bAlarm = findViewById(R.id.bAlarm);
    }

    private void setListener() {
        tvTime.setOnClickListener(this);
        bTimer.setOnClickListener(this);
        bThread.setOnClickListener(this);
        bHandler.setOnClickListener(this);
        bAlarm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvTime:
                pickTime();
                break;
            case R.id.bTimer:
                startTimer();
                //避免再次点击
                bTimer.setEnabled(false);
                break;
            case R.id.bThread:
                startThread();
                bThread.setEnabled(false);
                break;
            case R.id.bHandler:
                startHandler();
                bHandler.setEnabled(false);
                break;
            case R.id.bAlarm:
                startAlarm();
                bAlarm.setEnabled(false);
                break;
        }
    }

    private void pickTime() {
        TimePicker timePicker = new TimePicker(context);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                addLog("Pick Time: "+ hourOfDay+" : "+ minute);
                hour = hourOfDay;
                min = minute;
            }
        });
        DialogUtil.showViewDialog(context, "Pick Time", timePicker, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        tvTime.setText(hour + " : " + min);
                        break;
                }
            }
        });
    }

    private void startTimer() {
        long diff = getTimeDiff(hour, min);
        if(diff<0){
            addLog("The chosen time has passed!");
            return;
        }

        addLog("Start Timer Task, diff: " + diff/1000 +"s");
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                addLog("Timer: " + OUTCOME + ", " + DateUtil.long2String(System.currentTimeMillis(), FORMAT));
            }
        };

        timer.schedule(timerTask, diff);//仅执行一次

        //取消任务
//        timer.cancel();
//        timer = null;
//        timerTask.cancel();
//        timerTask = null;
    }

    private void startThread() {
        final long diff = getTimeDiff(hour, min);
        if(diff<0){
            addLog("The chosen time has passed!");
            return;
        }

        addLog("Start Thread Task, diff: " + diff/1000 +"s");
        //新开子线程
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(diff);
                    if(!cancelTask){
                        addLog("Thread: " + OUTCOME + ", " + DateUtil.long2String(System.currentTimeMillis(), FORMAT));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        //取消任务
//        cancelTask = true;
    }

    private void startHandler() {
        long diff = getTimeDiff(hour, min);
        if(diff<0){
            addLog("The chosen time has passed!");
            return;
        }

        addLog("Start Handler Task, diff: " + diff/1000 +"s");
        handler.sendEmptyMessageDelayed(MSG_CODE_TASK, diff);

        //取消任务
//        handler.removeMessages(MSG_CODE_TASK);
    }

    private void startAlarm() {
        long diff = getTimeDiff(hour, min);
        if(diff<0){
            addLog("The chosen time has passed!");
            return;
        }

        addLog("Start Alarm Task, diff: " + diff/1000 +"s");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ACTION_TASK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE_ALARM,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(alarmManager!=null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, diff, pendingIntent);
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, diff, pendingIntent);
            }else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, diff, pendingIntent);
            }
        }

        //取消alarm
//        alarmManager.cancel(PendingIntent.getBroadcast(context, REQUEST_CODE_ALARM,
//                new Intent(ACTION_TASK), PendingIntent.FLAG_UPDATE_CURRENT));
    }

    private long getTimeDiff(int hour, int min){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);//24-hour clock
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);

        long mm = calendar.getTimeInMillis();
        long now = System.currentTimeMillis();

        return mm - now;
    }

    private void addLog(String log){
        builder.append(log);
        builder.append("\n");

        tvLog.setText(builder.toString());
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_CODE_TASK:
                    addLog("Handler: " + OUTCOME + ", " + DateUtil.long2String(System.currentTimeMillis(), FORMAT));
                    break;
            }
        }
    };

    //==========================动态注册alarm监听==============================
    private void registerReceiver() {
        if (!isRegister) {
            isRegister = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_TASK);
            registerReceiver(alarmTask, filter);
        }
    }

    //取消监听
    public void unRegisterReceiver() {
        if (isRegister) {
            isRegister = false;
            unregisterReceiver(alarmTask);
        }
    }

    private BroadcastReceiver alarmTask = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            addLog("onReceive: alarmTask");
            String action = intent.getAction();
            if (ACTION_TASK.equals(action)) {
                addLog("Alarm: " + OUTCOME + ", " + DateUtil.long2String(System.currentTimeMillis(), FORMAT));
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();
    }
}
