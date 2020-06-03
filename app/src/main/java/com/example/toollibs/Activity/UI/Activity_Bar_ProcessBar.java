package com.example.toollibs.Activity.UI;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.example.toollibs.R;

public class Activity_Bar_ProcessBar extends AppCompatActivity {
    private ProgressBar progressBar1, progressBar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__bar__process_bar);

        init();
        setData();
        setListener();
    }

    private void init() {
        progressBar1 = findViewById(R.id.processBar1);
        progressBar2 = findViewById(R.id.processBar2);

        start();
    }

    private void setData() {
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    progressBar2.setProgress((int) msg.obj);
                    if(progressBar2.getProgress()==progressBar2.getMax()){
                        progressBar2.setVisibility(View.INVISIBLE);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void setListener() {
    }

    private void start()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int max = progressBar2.getMax();
                try {
                    //子线程循环间隔消息
                    int pro = 0;
                    while (pro < max) {
                        pro += 10;
                        Message msg = new Message();
                        msg.what = 2;
                        msg.obj = pro;
                        mHandler.sendMessage(msg);
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
