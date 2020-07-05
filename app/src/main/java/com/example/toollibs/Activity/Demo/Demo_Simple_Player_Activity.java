package com.example.toollibs.Activity.Demo;

import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.toollibs.Activity.Adapters.ListView_Player_Adapter;
import com.example.toollibs.Activity.Config.Constant;
import com.example.toollibs.Activity.Config.SettingData;
import com.example.toollibs.Activity.Service.PlayBackService;
import com.example.toollibs.R;
import com.example.toollibs.SelfClass.Song;
import com.example.toollibs.Util.IntentUtil;
import com.example.toollibs.Util.MediaUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class Demo_Simple_Player_Activity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SIMPLE PLAYER";
    private ImageView imgBack, imgCheck, imgAlbum, imgPlay, imgPlayLast, imgPlayNext;
    private TextView tvName, tvArtist, tvNotice;
    private ListView listView;
    private ListView_Player_Adapter adapter;

    private List<Song> songs;
    private String dir;
    private PlayBackService playBackService;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "service connected...");
            playBackService = ((PlayBackService.LocalBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "service disconnected...");
            playBackService = null;
        }
    };

    private final int SELECTOR_DIR_CODE = 0x001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_simple_player);

        init();
        setData();
        setListener();
    }

    private void init() {
        imgBack = findViewById(R.id.image_back);
        imgCheck = findViewById(R.id.image_check);

        imgAlbum = findViewById(R.id.image_album);
        imgPlay = findViewById(R.id.image_play);
        imgPlayLast = findViewById(R.id.image_play_last);
        imgPlayNext = findViewById(R.id.image_play_next);

        tvName = findViewById(R.id.text_name);
        tvArtist = findViewById(R.id.text_artist);
        tvNotice = findViewById(R.id.tv_notices);

        listView = findViewById(R.id.listView_player);

        bindPlayBackService();
    }

    private void setData() {
        songs = new ArrayList<>();
        dir =  SettingData.getString(getApplicationContext(), Constant.CONFIG_FILE, Constant.AUDIO_DIR_KEY, Constant.AUDIO_DIR_DEFAULT);
        readSongs();

        adapter = new ListView_Player_Adapter(this, songs);
        listView.setAdapter(adapter);
    }

    private void readSongs() {
        File file = new File(dir);
        songs.clear();
        if(file.exists()){
            File[] files = file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return !pathname.isDirectory() && pathname.toString().toLowerCase().endsWith(".mp3");
                }
            });
            for (int i=0; i<files.length; i++){
                songs.add(MediaUtil.getMusic4File(files[i]));
            }
        }
        if(songs.size()<1){
            listView.setVisibility(View.INVISIBLE);
            tvNotice.setVisibility(View.VISIBLE);
        }else{
            listView.setVisibility(View.VISIBLE);
            tvNotice.setVisibility(View.INVISIBLE);
            playBackService.setPlayList(songs);
        }
    }

    private void setListener() {
        imgBack.setOnClickListener(this);
        imgCheck.setOnClickListener(this);

        imgPlay.setOnClickListener(this);
        imgPlayLast.setOnClickListener(this);
        imgPlayNext.setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playBackService.play(position);
                setSongInfo();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_back:
                this.finish();
                break;
            case R.id.image_check:
                selectDir();
                readSongs();
                adapter.notifyDataSetChanged();
                break;
            case R.id.image_play:
                if(songs.size()>0){
                    if(playBackService.isPlaying()){
                        playBackService.pause();
                    }else{
                        playBackService.play();
                        setSongInfo();
                    }
                }
                break;
            case R.id.image_play_last:
                if(songs.size()>0){
                    playBackService.playLast();
                    setSongInfo();
                }
                break;
            case R.id.image_play_next:
                if(songs.size()>0){
                    playBackService.playNext();
                    setSongInfo();
                }
                break;
        }
    }

    private void setSongInfo(){
        Song song = playBackService.getPlayingSong();
        tvName.setText(song.getTitle());
        tvArtist.setText(song.getArtist());
        imgAlbum.setImageBitmap(MediaUtil.parseAlbum(this, song));
    }

    private void selectDir() {
        IntentUtil.readFile(this, SELECTOR_DIR_CODE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBindPlayBackService();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECTOR_DIR_CODE) {
                Uri uri = data.getData();
                ContentResolver contentResolver = this.getContentResolver();
                //String path = IntentUtil.getRealPath4Uri(this, uri, contentResolver);
                Log.d(TAG, "selector : " + uri.getPath());
                Log.d(TAG, "selector : " + uri.toString());

                //Log.d(TAG, "selector dir: " + path.substring(0,path.lastIndexOf("/")));
                //dir = path.substring(0,path.lastIndexOf("/"));
                //SettingData.setString(getApplicationContext(), Constant.CONFIG_FILE, Constant.AUDIO_DIR_KEY, dir);
            }
        }
    }

    private void bindPlayBackService(){
        Log.d(TAG, "bind service...");
        Intent intent = new Intent(getApplicationContext(), PlayBackService.class);
        bindService(intent, connection, Service.BIND_AUTO_CREATE);
    }

    private void unBindPlayBackService(){
        Log.d(TAG, "unbind service...");
        unbindService(connection);
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
