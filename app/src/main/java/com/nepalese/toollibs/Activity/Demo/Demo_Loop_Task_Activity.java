package com.nepalese.toollibs.Activity.Demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nepalese.toollibs.R;
import com.nepalese.toollibs.Util.DateUtil;

import java.util.Timer;
import java.util.TimerTask;

public class Demo_Loop_Task_Activity extends AppCompatActivity implements View.OnClickListener {
    private static final long INTERVAL_TIMER = 3 * 1000l;
    private static final long INTERVAL_THREAD = 5 * 1000l;
    private static final long INTERVAL_HANDLER = 7 * 1000l;
    private static final int MSG_CODE_TASK = 0;
    private static final int MSG_CODE_ADD_LOG = 1;

    private TextView tvLog;
    private Button bStartTimer, bStartThread, bStartHandler;
    private Button bStopTimer, bStopThread, bStopHandler;

    private Timer timer;
    private TimerTask timerTask;

    private boolean cancelThread = true;
    private StringBuilder builder;

    private final String FORMAT = "hh:MM:ss";
    private final String[] cont = {"冒个泡", "踢个球", "摸个头", "遛个狗", "吹个牛"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo__loop__task_);

        init();
        setListener();
    }

    private void init() {
        builder = new StringBuilder();

        tvLog = findViewById(R.id.tvLog);

        bStartTimer = findViewById(R.id.bStartTimer);
        bStartThread = findViewById(R.id.bStartThread);
        bStartHandler = findViewById(R.id.bStartHandler);

        bStopTimer = findViewById(R.id.bStopTimer);
        bStopThread = findViewById(R.id.bStopThread);
        bStopHandler = findViewById(R.id.bStopHandler);
    }

    private void setListener() {
        bStartTimer.setOnClickListener(this);
        bStartThread.setOnClickListener(this);
        bStartHandler.setOnClickListener(this);

        bStopTimer.setOnClickListener(this);
        bStopThread.setOnClickListener(this);
        bStopHandler.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bStartTimer:
                startTimer();
                bStartTimer.setEnabled(false);
                bStopTimer.setEnabled(true);
                break;
            case R.id.bStopTimer:
                stopTimer();
                bStartTimer.setEnabled(true);
                bStopTimer.setEnabled(false);
                break;
            case R.id.bStartThread:
                startThread();
                bStartThread.setEnabled(false);
                bStopThread.setEnabled(true);
                break;
            case R.id.bStopThread:
                stopThread();
                bStartThread.setEnabled(true);
                bStopThread.setEnabled(false);
                break;
            case R.id.bStartHandler:
                startHandler();
                bStartHandler.setEnabled(false);
                bStopHandler.setEnabled(true);
                break;
            case R.id.bStopHandler:
                stopHandler();
                bStartHandler.setEnabled(true);
                bStopHandler.setEnabled(false);
                break;
        }
    }

    private void addLog(String log){
        builder.append(log);
        builder.append("\n");

        Message message = Message.obtain();
        message.what = MSG_CODE_ADD_LOG;
        message.obj = builder.toString();
        handler.sendMessage(message);
    }

    //获取随机数: [0,4]
    private int getRandom() {
        return (int) (Math.random() * 4);
    }

    private void startTimer() {
        addLog("Start Timer Task...");
        if(timer==null){
            timer = new Timer();
        }

        if(timerTask==null){
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    //do thing
                    addLog("Timer: " + cont[getRandom()] + ", " + DateUtil.long2String(System.currentTimeMillis(), FORMAT));
                }
            };
        }

        timer.schedule(timerTask, 0, INTERVAL_TIMER);
    }

    private void stopTimer() {
        if(timer!=null || timerTask!=null){
            timer.cancel();
            timer = null;
            timerTask.cancel();
            timerTask = null;
            addLog("Stop Timer Task...");
        }else{
            addLog("The Timer Task hasn't started!");
        }
    }

    private void startThread() {
        addLog("Start Thread Task...");
        if(cancelThread){
            cancelThread = false;
        }
        //无法在非ui线程中直接控制ui
        new Thread(){
            @Override
            public void run() {
                super.run();
                while(!cancelThread){
                    try {
                        addLog("Thread: " + cont[getRandom()] + ", " + DateUtil.long2String(System.currentTimeMillis(), FORMAT));
                        Thread.sleep(INTERVAL_THREAD);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void stopThread() {
        if(!cancelThread){
            cancelThread = true;
            addLog("Stop Thread Task...");
        }else{
            addLog("The Thread Task hasn't started!");
        }
    }

    private void startHandler() {
        handler.post(task);
        addLog("Start Handler Task...");
    }

    private void stopHandler() {
        handler.removeCallbacks(task);
        addLog("Stop Handler Task...");
    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            handler.sendEmptyMessage(MSG_CODE_TASK);
            handler.postDelayed(task, INTERVAL_HANDLER);
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_CODE_TASK:
                    addLog("Handler: " + cont[getRandom()] + ", " + DateUtil.long2String(System.currentTimeMillis(), FORMAT));
                    break;
                case MSG_CODE_ADD_LOG:
                    tvLog.setText((String)msg.obj);
                    break;
            }
        }
    };
}
