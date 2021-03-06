package com.nepalese.toollibs.Activity.Demo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nepalese.toollibs.R;
import com.nepalese.toollibs.Util.DateUtil;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class Demo_TimingTask_Activity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Demo_TimingTask_Activit";
    private final String ACTION_TASK = "com.nepalese.toollibs.alarm.task";
    private final String OUTCOME = "完成任务！";
    private final String FORMAT = "yyyy-MM-dd hh:mm:ss";

    private final int MSG_CODE_TASK = 0;
    private final int REQUEST_CODE_ALARM = 1;
    private final int MSG_CODE_ADD_LOG = 1;

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

        registerReceiver();//开启alarm监听
        init();
        setListener();
    }

    private void init() {
        context = getApplicationContext();
        builder = new StringBuilder();

        tvTime = findViewById(R.id.tvSelectTime);
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
            case R.id.tvSelectTime:
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

    //简单时间选择对话框，仅hour 和 minute;
    private void pickTime() {
        Calendar calendar = Calendar.getInstance();
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                hour = i;
                min = i1;
                tvTime.setText(hour+" : " + min);
            }
        },h,m,true);
        timePickerDialog.show();
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

    //计算目标时间与当前时差
    private long getTimeDiff(int hour, int min){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);//24-hour clock
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);

        long mm = calendar.getTimeInMillis();
        long now = System.currentTimeMillis();

        return mm - now;
    }

    //由于在子线程无法更新UI, 一致使用handler发送UI更新消息；
    private void addLog(String log){
        builder.append(log);
        builder.append("\n");

        Message message = Message.obtain();
        message.what = MSG_CODE_ADD_LOG;
        message.obj = builder.toString();
        handler.sendMessage(message);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_CODE_TASK:
                    addLog("Handler: " + OUTCOME + ", " + DateUtil.long2String(System.currentTimeMillis(), FORMAT));
                    break;
                case MSG_CODE_ADD_LOG:
                    //log 更新
                    tvLog.setText((String)msg.obj);
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
            Log.i(TAG, "registerReceiver");
        }
    }

    //取消监听
    public void unRegisterReceiver() {
        if (isRegister) {
            isRegister = false;
            unregisterReceiver(alarmTask);
            Log.i(TAG, "unRegisterReceiver");
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
