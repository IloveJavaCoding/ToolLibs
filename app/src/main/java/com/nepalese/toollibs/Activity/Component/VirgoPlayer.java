package com.nepalese.toollibs.Activity.Component;

import android.media.MediaPlayer;
import android.util.Log;

import com.nepalese.toollibs.Activity.Interface.IPlayback;
import com.nepalese.toollibs.Bean.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VirgoPlayer implements IPlayback, MediaPlayer.OnCompletionListener{
    private static final String TAG = "Player";

    private static volatile VirgoPlayer instance;
    private MediaPlayer mediaPlayer;
    private List<Song> songs;
    private List<Callback> mCallbacks = new ArrayList<>(2);

    // Player status
    private boolean isPaused = false;
    private int currentIndex = 0;

    private VirgoPlayer() {
        mediaPlayer = new MediaPlayer();
        songs = new ArrayList<>();
        mediaPlayer.setOnCompletionListener(this);
    }

    public static VirgoPlayer getInstance() {
        if (instance == null) {
            synchronized (VirgoPlayer.class) {
                if (instance == null) {
                    instance = new VirgoPlayer();
                }
            }
        }
        return instance;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // There is only one limited play mode which is list, player should be stopped when hitting the list end
        currentIndex++;
        play();

        notifyComplete(songs.get(currentIndex));
    }

    @Override
    public void setPlayList(List<Song> songs) {
        this.songs = songs;
    }

    @Override
    public boolean play() {
        if (isPaused) {
            mediaPlayer.start();
            notifyPlayStatusChanged(true);
            return true;
        }
        if (songs.size()>0) {
            if(currentIndex>=songs.size()){
                currentIndex = 0;
            }
            Song song = songs.get(currentIndex);
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(song.getPath());
                mediaPlayer.prepare();
                mediaPlayer.start();
                notifyPlayStatusChanged(true);
            } catch (IOException e) {
                Log.e(TAG, "play: ", e);
                notifyPlayStatusChanged(false);
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean play(int index) {
        isPaused = false;
        currentIndex = index;
        return play();
    }

    @Override
    public boolean play(List<Song> songs, int startIndex) {
        if (songs == null || startIndex < 0 || startIndex >= songs.size()) return false;

        isPaused = false;
        currentIndex = startIndex;
        setPlayList(songs);
        return play();
    }

    @Override
    public boolean play(Song song) {
        if (song == null) return false;

        isPaused = false;
        songs.clear();
        songs.add(song);
        return play();
    }

    @Override
    public boolean playLast() {
        isPaused = false;
        if(currentIndex>0){
            currentIndex--;
            play();
            notifyPlayLast(songs.get(currentIndex));
            return true;
        }
        return false;
    }

    @Override
    public boolean playNext() {
        isPaused = false;
        currentIndex++;
        play();
        notifyPlayNext(songs.get(currentIndex));
        return true;
    }

    @Override
    public boolean pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPaused = true;
            notifyPlayStatusChanged(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getProgress() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public Song getPlayingSong() {
        return songs.get(currentIndex);
    }

    @Override
    public boolean seekTo(int progress) {
        if (songs.isEmpty()) return false;

        Song currentSong = songs.get(currentIndex);
        if (currentSong != null) {
            if (currentSong.getDuration() <= progress) {
                onCompletion(mediaPlayer);
            } else {
                mediaPlayer.seekTo(progress);
            }
            return true;
        }
        return false;
    }

    @Override
    public void registerCallback(Callback callback) {
        mCallbacks.add(callback);
    }

    @Override
    public void unregisterCallback(Callback callback) {
        mCallbacks.remove(callback);
    }

    @Override
    public void removeCallbacks() {
        mCallbacks.clear();
    }

    @Override
    public void releasePlayer() {
        songs = null;
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
        instance = null;
    }

    private void notifyPlayStatusChanged(boolean isPlaying) {
        for (Callback callback : mCallbacks) {
            callback.onPlayStatusChanged(isPlaying);
        }
    }

    private void notifyPlayLast(Song song) {
        for (Callback callback : mCallbacks) {
            callback.onSwitchLast(song);
        }
    }

    private void notifyPlayNext(Song song) {
        for (Callback callback : mCallbacks) {
            callback.onSwitchNext(song);
        }
    }

    private void notifyComplete(Song song) {
        for (Callback callback : mCallbacks) {
            callback.onComplete(song);
        }
    }
}
