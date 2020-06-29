package com.example.toollibs.Activity.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.toollibs.Activity.Demo.Demo_Simple_Player_Activity;
import com.example.toollibs.Activity.Demo.Player;
import com.example.toollibs.Activity.Interface.IPlayback;
import com.example.toollibs.R;
import com.example.toollibs.SelfClass.Song;
import com.example.toollibs.Util.MediaUtil;

import java.util.List;

public class PlayBackService extends Service implements IPlayback, IPlayback.Callback{
    private static final String ACTION_PLAY = "com.example.toollibs.Activity.Service.ACTION.PLAY";
    private static final String ACTION_PLAY_LAST = "com.example.toollibs.Activity.Service.ACTION.PLAY_LAST";
    private static final String ACTION_PLAY_NEXT = "com.example.toollibs.Activity.Service.ACTION.PLAY_NEXT";
    private static final String ACTION_STOP_SERVICE = "com.example.toollibs.Activity.Service.ACTION.STOP_SERVICE";

    private static final int NOTIFICATION_ID = 2;
    private RemoteViews mContentViewBig, mContentViewSmall;
    private Player mPlayer;
    private final Binder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public PlayBackService getService() {
            return PlayBackService.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = Player.getInstance();
        mPlayer.registerCallback(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_PLAY.equals(action)) {
                if (isPlaying()) {
                    pause();
                } else {
                    play();
                }
            } else if (ACTION_PLAY_NEXT.equals(action)) {
                playNext();
            } else if (ACTION_PLAY_LAST.equals(action)) {
                playLast();
            } else if (ACTION_STOP_SERVICE.equals(action)) {
                if (isPlaying()) {
                    pause();
                }
                stopForeground(true);
                unregisterCallback(this);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }

    @Override
    public boolean stopService(Intent name) {
        stopForeground(true);
        unregisterCallback(this);
        return super.stopService(name);
    }

    @Override
    public void setPlayList(List<Song> songs) {
        mPlayer.setPlayList(songs);
    }

    @Override
    public boolean play() {
        return mPlayer.play();
    }

    @Override
    public boolean play(int index) {
        return mPlayer.play(index);
    }

    @Override
    public boolean play(List<Song> songs, int startIndex) {
        return mPlayer.play(songs, startIndex);
    }

    @Override
    public boolean play(Song song) {
        return mPlayer.play(song);
    }

    @Override
    public boolean playLast() {
        return mPlayer.playLast();
    }

    @Override
    public boolean playNext() {
        return mPlayer.playNext();
    }

    @Override
    public boolean pause() {
        return mPlayer.pause();
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public int getProgress() {
        return mPlayer.getProgress();
    }

    @Override
    public Song getPlayingSong() {
        return mPlayer.getPlayingSong();
    }

    @Override
    public boolean seekTo(int progress) {
        return mPlayer.seekTo(progress);
    }

    @Override
    public void registerCallback(Callback callback) {
        mPlayer.registerCallback(callback);
    }

    @Override
    public void unregisterCallback(Callback callback) {
        mPlayer.unregisterCallback(callback);
    }

    @Override
    public void removeCallbacks() {
        mPlayer.removeCallbacks();
    }

    @Override
    public void releasePlayer() {
        mPlayer.releasePlayer();
        super.onDestroy();
    }

    @Override
    public void onSwitchLast(@Nullable Song last) {
        showNotification();
    }

    @Override
    public void onSwitchNext(@Nullable Song next) {
        showNotification();
    }

    @Override
    public void onComplete(@Nullable Song next) {
        showNotification();
    }

    @Override
    public void onPlayStatusChanged(boolean isPlaying) {
        showNotification();
    }

    private void showNotification2() {
        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, Demo_Simple_Player_Activity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)  // the status icon
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .setCustomContentView(getSmallContentView())
                .setCustomBigContentView(getBigContentView())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true)
                .build();

        // Send the notification.
        startForeground(NOTIFICATION_ID, notification);
    }

    public void showNotification(){
        String channelId = "player";
        //1. get notification service
        NotificationManager manager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //2. create notification channel version>26   android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelName = "Default Name";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);

            manager.createNotificationChannel(channel);
        }

        //3. set intent(active when click the notification)
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, Demo_Simple_Player_Activity.class), 0);

        //4. init notification, get instance
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)//intent
                .setCustomContentView(getSmallContentView())
                .setCustomBigContentView(getBigContentView())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true)
                .build();

        //5. send notification
        manager.notify(NOTIFICATION_ID,notification);
    }

    private RemoteViews getSmallContentView() {
        if (mContentViewSmall == null) {
            mContentViewSmall = new RemoteViews(getPackageName(), R.layout.remote_view_music_player_small);
            setUpRemoteView(mContentViewSmall);
        }
        updateRemoteViews(mContentViewSmall);
        return mContentViewSmall;
    }

    private RemoteViews getBigContentView() {
        if (mContentViewBig == null) {
            mContentViewBig = new RemoteViews(getPackageName(), R.layout.remote_view_music_player_big);
            setUpRemoteView(mContentViewBig);
        }
        updateRemoteViews(mContentViewBig);
        return mContentViewBig;
    }

    private void setUpRemoteView(RemoteViews remoteView) {
        remoteView.setImageViewResource(R.id.image_view_close, R.drawable.ic_remote_view_close);
        remoteView.setImageViewResource(R.id.image_view_play_last, R.drawable.ic_remote_view_play_last);
        remoteView.setImageViewResource(R.id.image_view_play_next, R.drawable.ic_remote_view_play_next);

        remoteView.setOnClickPendingIntent(R.id.image_view_close, getPendingIntent(ACTION_STOP_SERVICE));
        remoteView.setOnClickPendingIntent(R.id.image_view_play_last, getPendingIntent(ACTION_PLAY_LAST));
        remoteView.setOnClickPendingIntent(R.id.image_view_play_next, getPendingIntent(ACTION_PLAY_NEXT));
        remoteView.setOnClickPendingIntent(R.id.image_view_play, getPendingIntent(ACTION_PLAY));
    }

    private void updateRemoteViews(RemoteViews remoteView) {
        Song currentSong = mPlayer.getPlayingSong();
        if (currentSong != null) {
            remoteView.setTextViewText(R.id.text_view_name, currentSong.getDisplayName());
            remoteView.setTextViewText(R.id.text_view_artist, currentSong.getArtist());
        }
        remoteView.setImageViewResource(R.id.image_view_play, isPlaying()
                ? R.drawable.ic_remote_view_pause : R.drawable.ic_remote_view_play);

        Bitmap album = MediaUtil.parseAlbum(this, getPlayingSong());
        if (album == null) {
            remoteView.setImageViewResource(R.id.image_view_album, R.mipmap.ic_launcher);
        } else {
            remoteView.setImageViewBitmap(R.id.image_view_album, album);
        }
    }

    private PendingIntent getPendingIntent(String action) {
        return PendingIntent.getService(this, 0, new Intent(action), 0);
    }
}
