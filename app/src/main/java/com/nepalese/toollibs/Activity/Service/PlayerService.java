package com.nepalese.toollibs.Activity.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.nepalese.toollibs.R;

import java.io.IOException;
import java.util.List;

public class PlayerService extends Service {
    private static final String TAG = "PlayerService";
    private PlayerBinder binder = new PlayerBinder();
    private MediaPlayer mediaPlayer;
    private List<String> list;
    private int currentIndex;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "on bind...");
        return binder;
    }

    public class PlayerBinder extends Binder{
        public PlayerService getPlayerService(){
            return PlayerService.this;
        }
    }
    //=================================API=====================================
    public void play(){
        Log.d(TAG, "playing...");
        playDefault();
    }

    public void playByPath(String path){
        Log.d(TAG, "playing..." + path);
        playSingle(path);
    }

    public void pause(){
        Log.d(TAG, "pause...");
        pausePlayer();
    }

    public void continues(){
        Log.d(TAG, "continue...");
        continuePlay();
    }

    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }
    //=======================================================================
    //play default music
    private void playDefault(){
        if (mediaPlayer!=null){
            mediaPlayer.reset();
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.bloom_of_youth);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    //single source
    private void playSingle(String path){
        if(mediaPlayer!=null){
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        playMusic();
    }

    //multi sources
    private void setDataList(List<String> list){
        this.list = list;
    }

    private void playLoop(){
        if(mediaPlayer!=null){
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setDataSource(list.get(currentIndex));
        } catch (IOException e) {
            e.printStackTrace();
        }
        playMusic();
    }

    private void playMusic(){
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    private void continuePlay(){
        mediaPlayer.start();
    }

    private void pausePlayer(){
        mediaPlayer.pause();
    }

    private void releasePlayer(){
        mediaPlayer.reset();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    private void setListener(){
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(currentIndex<list.size()){
                    currentIndex++;
                }else{
                    currentIndex=0;
                }
                playLoop();
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "on create...");
        //mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(this, R.raw.bloom_of_youth);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "on start command...");
        mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "on destroy...");
        super.onDestroy();
        releasePlayer();
    }
}
