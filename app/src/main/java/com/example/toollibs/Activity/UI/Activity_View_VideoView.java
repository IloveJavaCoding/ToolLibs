package com.example.toollibs.Activity.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.toollibs.R;
import com.example.toollibs.SelfClass.VideoFile;
import com.example.toollibs.Util.DateUtil;
import com.example.toollibs.Util.MediaUtil;
import com.example.toollibs.Util.SystemUtil;

public class Activity_View_VideoView extends AppCompatActivity implements View.OnClickListener {
    private Button bChooseFile;
    public VideoView videoView;
    private TextView tvCurrentTime, tvDuration;
    private ImageView imgControl, imgVoice, imgFullScreen;
    private SeekBar seekBar;
    private LinearLayout layoutControl;
    private RelativeLayout videoLayout;

    private Thread thread;
    private boolean isFull;

    private static final int HIDE_CONTROL_CODE = 0x001;
    private static final int SEND_TIME_CODE = 0x002;
    private static final int OPEN_VIDEO_FILE_CODE = 0x003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__view__video_view);

        init();
        setData();
        setListener();
    }

    private void init() {
        bChooseFile = findViewById(R.id.bChooseFile);
        videoView = findViewById(R.id.videoView);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        tvDuration = findViewById(R.id.tvDuration);

        imgControl = findViewById(R.id.ivControl);
        imgVoice = findViewById(R.id.ivSound);
        imgFullScreen = findViewById(R.id.ivFullScreen);

        seekBar = findViewById(R.id.seekBar);
        layoutControl = findViewById(R.id.layout_control);
        videoLayout = findViewById(R.id.videoLayout);

        isFull = false;
    }

    private void setData() {
        videoView.setVideoURI(Uri.parse("android.resource://com.example.toollibs/"+R.raw.yys));
        long duration = MediaUtil.getDuration(this, R.raw.yys);
        setBackground(null);
        seekBar.setMax((int)duration);
        tvDuration.setText(DateUtil.FormatTime(duration));

        thread = new Thread(new videoThread());
        thread.start();
    }

    private void setBackground(Bitmap bitmap){
        if(bitmap==null){
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.yy_video_default);
        }
        videoView.setBackground(new BitmapDrawable(getResources(), bitmap));
    }

    private void setListener() {
        bChooseFile.setOnClickListener(this);

        imgControl.setOnClickListener(this);
        imgVoice.setOnClickListener(this);
        imgFullScreen.setOnClickListener(this);

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("Tag","click screen");
                if(videoView.isPlaying()){
                    showControl();
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                if(videoView.isPlaying()){
                                    handler.sendEmptyMessage(HIDE_CONTROL_CODE);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
                return false;
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imgControl.setImageResource(R.drawable.img_video_triangle);
                showControl();
                Log.d("Tag", "finish play");
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
                videoView.seekTo(seekBar.getProgress());
            }
        });
    }

    private void hideControl(){
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    Log.d("Tag", "hide control bar");
                    handler.sendEmptyMessage(HIDE_CONTROL_CODE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void showControl(){
        layoutControl.setVisibility(View.VISIBLE);
        imgControl.setVisibility(View.VISIBLE);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==HIDE_CONTROL_CODE){
                layoutControl.setVisibility(View.INVISIBLE);
                imgControl.setVisibility(View.INVISIBLE);
            }else if(msg.what==SEND_TIME_CODE){
                seekBar.setProgress((int) msg.obj);
                tvCurrentTime.setText(DateUtil.FormatTime((int) msg.obj));
            }
        }
    };

    private void playVideo(){
        imgControl.setImageResource(R.drawable.img_video_equal);
        videoView.setBackground(null);
        videoView.start();
        layoutControl.setVisibility(View.VISIBLE);
        hideControl();
        Log.d("Tag", "playing...");
    }

    private void pauseVideo(){
        imgControl.setImageResource(R.drawable.img_video_triangle);
        videoView.pause();
    }

    private void openLocalFile(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,OPEN_VIDEO_FILE_CODE);
        Log.d("Tag", "open local file");
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.bChooseFile:
                openLocalFile();
                break;
             case R.id.ivControl:
                if(videoView.isPlaying()){
                    //pause
                    pauseVideo();
                }else{
                    //play
                    playVideo();
                }
                break;
            case R.id.ivSound:

                break;
            case R.id.ivFullScreen:
                landScreen();
                break;
        }
    }

    private void landScreen(){
        //int rot = getWindowManager().getDefaultDisplay().getRotation();
        if(!isFull){
            isFull = true;
            bChooseFile.setVisibility(View.INVISIBLE);

            RelativeLayout.LayoutParams layoutParams =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            videoLayout.setLayoutParams(layoutParams);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }else{
            RelativeLayout.LayoutParams lp = new  RelativeLayout.LayoutParams(SystemUtil.GetScreenDM(getApplicationContext()).widthPixels,220);
            lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            videoLayout.setLayoutParams(lp);
            isFull = false;
            bChooseFile.setVisibility(View.VISIBLE);
        }
    }

    public class videoThread implements Runnable{
        @Override
        public void run() {
            while(videoView!=null){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = SEND_TIME_CODE;
                msg.obj = videoView.getCurrentPosition();
                handler.sendMessage(msg);
            }
        }
    }

    private void resetVideoView(String path) {
        VideoFile videoFile = MediaUtil.getVideoFileInformation(path);
        videoView.setVideoPath(videoFile.getPath());
        setBackground(null);
        seekBar.setMax((int)videoFile.getDuration());
        tvDuration.setText(DateUtil.FormatTime(videoFile.getDuration()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==OPEN_VIDEO_FILE_CODE && resultCode==RESULT_OK){
            Log.d("Tag", "Feedback.......");
            Uri uri = data.getData();
            String videoPath = uri.getPath();
            Log.d("Tag", "......."+ videoPath);

            resetVideoView(videoPath);
            showControl();
        }
    }
}
