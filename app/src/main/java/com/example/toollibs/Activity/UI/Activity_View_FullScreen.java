package com.example.toollibs.Activity.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.toollibs.R;

public class Activity_View_FullScreen extends AppCompatActivity {
    public VideoView videoView;
    private ImageView ivBack;

    private int curTime, totalTime;
    private String path;
    public static final String KEY_FEEDBACK = "result";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__view__full_screen);

        getDate();
        init();
        setData();
        setListener();
    }

    private void getDate() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Activity_View_VideoView.KEY_BUNDLE);
        path = bundle.getString(Activity_View_VideoView.KEY_PATH);
        curTime = bundle.getInt(Activity_View_VideoView.KEY_CUR_TIME);
        totalTime = bundle.getInt(Activity_View_VideoView.KEY_TOTAL_TIME);
    }

    private void init() {
        videoView = findViewById(R.id.videoView);
        ivBack = findViewById(R.id.ivBack);
    }

    private void setData() {
        if(path==null){
            videoView.setVideoURI(Uri.parse("android.resource://com.example.toollibs/"+R.raw.video));
        }else{
            videoView.setVideoPath(path);
        }

        videoView.seekTo(curTime);
        videoView.start();
    }

    private void setListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //back to last view
                Intent intent = new Intent();
                intent.putExtra(KEY_FEEDBACK, videoView.getCurrentPosition());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
