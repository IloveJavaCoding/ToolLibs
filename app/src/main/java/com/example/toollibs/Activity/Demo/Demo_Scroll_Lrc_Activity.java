package com.example.toollibs.Activity.Demo;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.toollibs.OverWriteClass.LrcView;
import com.example.toollibs.R;
import com.example.toollibs.Util.BitmapUtil;
import com.example.toollibs.Util.DateUtil;
import com.example.toollibs.Util.FileUtil;
import com.example.toollibs.Util.MediaUtil;

public class Demo_Scroll_Lrc_Activity extends AppCompatActivity {
    private ImageView imgPlay, imgBack;
    private SeekBar seekBar;
    private LrcView lrcView;
    private TextView tvCurTime, tvTotalTime;
    private MediaPlayer mediaPlayer;
    private Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_scroll_lrc);

        init();
        setData();
        setListener();
    }

    private void init() {
        imgBack = findViewById(R.id.img_back);
        imgPlay = findViewById(R.id.img_play);
        seekBar = findViewById(R.id.seekBarPlayer);
        lrcView = findViewById(R.id.lrcView);
        tvCurTime = findViewById(R.id.tv_current_time);
        tvTotalTime = findViewById(R.id.tv_total_time);

        mediaPlayer = MediaPlayer.create(this, R.raw.youth_meng);
        lrcView.setLrc(FileUtil.readResource(this, R.raw.shaonian, "utf-8"));
        lrcView.setBackground(BitmapUtil.getBitmapFromRes(this, R.drawable.img_mengran));
    }

    private void setData() {
        tvTotalTime.setText(DateUtil.FormatTime(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
        thread = new Thread(new MusicThread());
        thread.start();
    }

    private void setListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    imgPlay.setImageResource(R.drawable.ic_remote_view_play);
                }else{
                    mediaPlayer.start();
                    imgPlay.setImageResource(R.drawable.ic_remote_view_pause);
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
            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            seekBar.setProgress(msg.what);
            lrcView.onProgress(msg.what);
            tvCurTime.setText(DateUtil.FormatTime(msg.what));
        }
    };

    public class MusicThread implements Runnable{
        @Override
        public void run() {
            while(mediaPlayer !=null){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(mediaPlayer.getCurrentPosition());
            }
        }
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.reset();
        mediaPlayer.release();
        super.onDestroy();
    }
}
