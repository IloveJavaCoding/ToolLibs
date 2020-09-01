package com.example.toollibs.Activity.UI;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.toollibs.R;
import com.example.toollibs.Util.SystemUtil;

public class Activity_Bar_ProcessBar extends AppCompatActivity {
    private ProgressBar progressBar1, progressBar2;
    private RatingBar ratingBar1, ratingBar2;
    private TextView tvRating1, tvRating2;
    private Toolbar toolBar;

    private final int PROCESS_BAR_NUM = 0x002;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__bar__process_bar);

        init();
        setData();
        setListener();
    }

    private void init() {
        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        progressBar1 = findViewById(R.id.processBar1);
        progressBar2 = findViewById(R.id.processBar2);

        ratingBar1 = findViewById(R.id.ratingBar1);
        ratingBar2 = findViewById(R.id.ratingBar2);
        tvRating1 = findViewById(R.id.tvRating1);
        tvRating2 = findViewById(R.id.tvRating2);

        start();
    }

    private void setData() {
        Drawable drawable = getResources().getDrawable(R.drawable.ic_remote_view_close);
        drawable.setBounds(0,0,30,30);
        toolBar.setNavigationIcon(drawable);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle(getString(R.string.app_name));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.item1:
                SystemUtil.showToast(getApplicationContext(), "item1");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROCESS_BAR_NUM:
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
        ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                tvRating1.setText(rating+"");
            }
        });

        ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                tvRating2.setText(rating+"");
            }
        });

        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int max = progressBar2.getMax();
                int temp = 0;
                try {
                    while (temp < max) {
                        temp += 10;
                        Message msg = new Message();
                        msg.what = PROCESS_BAR_NUM;
                        msg.obj = temp;
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
