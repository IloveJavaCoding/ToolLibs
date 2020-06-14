package com.example.toollibs.Activity.UI;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.toollibs.OverWriteClass.EditTextDelIcon;
import com.example.toollibs.R;
import com.example.toollibs.Util.BitmapUtil;
import com.example.toollibs.Util.IntentUtil;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Activity_Intent extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView, imgShare, imgPlay;
    private Button bImage, bAudio, bVideo, bText;
    private TextView tvImage, tvAudio, tvVideo, tvText;

    private EditTextDelIcon etInput;
    private Button bSearch;

    private Button bDial1, bEmail, bRecord, bContract;

    private RelativeLayout laoutPlay;
    private MediaPlayer mediaPlayer;

    private static final int OPEN_IMAGE_CODE = 0x001;
    private static final int OPEN_AUDIO_CODE = 0x002;
    private static final int OPEN_VIDEO_CODE = 0x003;
    private static final int OPEN_TEXT_CODE = 0x004;

    private static final String E_MAIL = "547125836@qq.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__intent);

        init();
        setData();
        setListener();
    }

    private void init() {
        imageView = findViewById(R.id.imageView);
        imgShare = findViewById(R.id.imgShare);

        bImage = findViewById(R.id.bOpenImage);
        bAudio = findViewById(R.id.bOpenAudio);
        bVideo = findViewById(R.id.bOpenVideo);
        bText = findViewById(R.id.bOpenText);

        tvImage = findViewById(R.id.tvImgPath);
        tvAudio = findViewById(R.id.tvAudioPath);
        tvVideo = findViewById(R.id.tvVideoPath);
        tvText = findViewById(R.id.tvTextPath);

        bDial1 = findViewById(R.id.bOpenDial);
        bEmail = findViewById(R.id.bOpenEmail);
        bRecord = findViewById(R.id.bOpenRecord);
        bContract = findViewById(R.id.bOpenContract);

        etInput = findViewById(R.id.etDel);
        bSearch = findViewById(R.id.bSearch);

        imgPlay = findViewById(R.id.imgPlay);
        laoutPlay = findViewById(R.id.layoutPlay);
        mediaPlayer = new MediaPlayer();
    }

    private void setData() {
    }

    private void setListener() {
        bImage.setOnClickListener(this);
        bAudio.setOnClickListener(this);
        bVideo.setOnClickListener(this);
        bText.setOnClickListener(this);

        tvImage.setOnClickListener(this);
        tvAudio.setOnClickListener(this);
        tvVideo.setOnClickListener(this);
        tvText.setOnClickListener(this);

        bDial1.setOnClickListener(this);
        bEmail.setOnClickListener(this);
        bRecord.setOnClickListener(this);
        bContract.setOnClickListener(this);

        bSearch.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        imgPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bOpenImage:
                IntentUtil.readImageFile(this, OPEN_IMAGE_CODE);
                break;
            case R.id.bOpenAudio:
                IntentUtil.readAudioFile(this, OPEN_AUDIO_CODE);
                break;
            case R.id.bOpenVideo:
                IntentUtil.readVideoFile(this, OPEN_VIDEO_CODE);
                break;
            case R.id.bOpenText:
                IntentUtil.readTextFile(this, OPEN_TEXT_CODE);
                break;

            case R.id.tvImgPath:
                if(judge(tvImage)){
                    IntentUtil.openImageFile(this, tvImage.getText().toString());
                }
                break;
            case R.id.tvAudioPath:
                if(judge(tvAudio)){
                    IntentUtil.openAudioFile(this, tvAudio.getText().toString());
                }
                break;
            case R.id.tvVideoPath:
                if(judge(tvVideo)){
                    IntentUtil.openVideoFile(this, tvVideo.getText().toString());
                }
                break;
            case R.id.tvTextPath:
                if(judge(tvText)){
                    IntentUtil.openTextFile(this, tvText.getText().toString());
                }
                break;

            case R.id.bOpenDial:
                IntentUtil.go2Dial(this);
                break;
            case R.id.bOpenEmail:
                IntentUtil.sendEmail2(this, E_MAIL);
                break;
            case R.id.bOpenRecord:
                IntentUtil.openRecordAudio(this);
                break;
            case R.id.bOpenContract:
                IntentUtil.openContract(this);
                break;

            case R.id.bSearch:
                String key = etInput.getText().toString().trim();
                IntentUtil.searchInfo(this, key==null?"I love you":key);
                break;
            case R.id.imgShare:
                IntentUtil.shareText(this, "Share", "Hello!");
                break;
            case R.id.imgPlay:
                if(mediaPlayer.isPlaying()){
                    //pause
                    mediaPlayer.pause();
                    imgPlay.setImageResource(R.drawable.img_video_pause);
                }else{
                    //play
                    imgPlay.setImageResource(R.drawable.img_video_play);
                    mediaPlayer.start();
                }
                break;
        }
    }

    private boolean judge(TextView tv) {
        if(tv.getText().toString()==null){
            return false;
        }

        return true;
    }

    private void initPlayer(String path) {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.reset();
        }

        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Uri uri = data.getData();
        ContentResolver contentResolver = this.getContentResolver();

        String path = IntentUtil.getRealPath4Uri(this, uri, contentResolver);
        Log.d("tag", "uri: " + path);
        switch (requestCode){
            case OPEN_IMAGE_CODE:
                if(resultCode==RESULT_OK){
                    tvImage.setText(path);

                    imageView.setImageBitmap(BitmapUtil.GetBitmapFromFile(path));
                    //imageView.setImageBitmap(BitmapFactory.decodeStream(contentResolver.openInputStream(uri)));
                }
                break;
            case OPEN_AUDIO_CODE:
                if(resultCode==RESULT_OK){
                    tvAudio.setText(path);

                    //init mediaPlayer
                    initPlayer(path);
                    laoutPlay.setVisibility(View.VISIBLE);
                }
                break;
            case OPEN_VIDEO_CODE:
                if(resultCode==RESULT_OK){
                    tvVideo.setText(path);
                }
                break;
            case OPEN_TEXT_CODE:
                if(resultCode==RESULT_OK){
                    tvText.setText(path);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
