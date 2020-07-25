package com.example.toollibs.Activity.Demo;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.toollibs.Activity.Events.RefershLrcLine;
import com.example.toollibs.Activity.Events.SentTotalLineEvent;
import com.example.toollibs.OverWriteClass.LrcView;
import com.example.toollibs.OverWriteClass.MyLrcView;
import com.example.toollibs.R;
import com.example.toollibs.Util.BitmapUtil;
import com.example.toollibs.Util.DateUtil;
import com.example.toollibs.Util.FileUtil;
import com.example.toollibs.Util.MediaUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Timer;
import java.util.TimerTask;

public class Demo_Scroll_Lrc_Activity extends AppCompatActivity {
    private ImageView imgPlay, imgBack;
    private SeekBar seekBar;
    private LinearLayout layout;
//    private LrcView lrcView;
    private MyLrcView lrcView;
    private TextView tvCurTime, tvTotalTime;
    private MediaPlayer mediaPlayer;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_scroll_lrc);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setLayout();
        init();
        setData();
        setListener();
    }

    private void setLayout() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    private void init() {
        layout = findViewById(R.id.layoutBgLrc);
        imgBack = findViewById(R.id.img_back);
        imgPlay = findViewById(R.id.img_play);
        seekBar = findViewById(R.id.seekBarPlayer);
        lrcView = findViewById(R.id.myLrcView);
        tvCurTime = findViewById(R.id.tv_current_time);
        tvTotalTime = findViewById(R.id.tv_total_time);

        mediaPlayer = MediaPlayer.create(this, R.raw.youth_meng);
        lrcView.setLrc(FileUtil.readResource(this, R.raw.shaonian, "utf-8"));
        lrcView.seekTo(0);
    }

    private void setData() {
        layout.setBackground(new BitmapDrawable(getResources(), BitmapUtil.fastBlurBitmap(this, BitmapUtil.getBitmapFromRes(this, R.drawable.img_mengran), 200)));
        tvTotalTime.setText(DateUtil.FormatTime(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
    }

    private void startTimer(){
        if(timer==null){
            timer = new Timer();
        }

        if(timerTask==null){
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        handler.sendEmptyMessage(mediaPlayer.getCurrentPosition());
                    }
                }
            };
        }

        timer.schedule(timerTask, 0, 500);
    }

    private void stopTimer(){
        timer.cancel();
        timer = null;
        timerTask.cancel();
        timerTask = null;
    }

    private void setListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
                finish();
            }
        });

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    stopTimer();
                    mediaPlayer.pause();
                    imgPlay.setImageResource(R.drawable.ic_remote_view_play);
                    lrcView.setPlaying(false);
                }else{
                    mediaPlayer.start();
                    imgPlay.setImageResource(R.drawable.ic_remote_view_pause);
                    startTimer();
                    lrcView.setPlaying(true);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                lrcView.seekTo(seekBar.getProgress());
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                lrcView.loopMode();
                lrcView.setPlaying(true);
            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            seekBar.setProgress(msg.what);
            lrcView.seekTo(msg.what);
            tvCurTime.setText(DateUtil.FormatTime(msg.what));
        }
    };

    @Subscribe
    public void onEventMainThread(RefershLrcLine event){
        mediaPlayer.seekTo((int)event.getTime());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.reset();
        mediaPlayer.release();
        stopTimer();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
