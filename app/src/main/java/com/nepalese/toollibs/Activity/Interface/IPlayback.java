package com.nepalese.toollibs.Activity.Interface;

import android.support.annotation.Nullable;

import com.nepalese.toollibs.Bean.Song;

import java.util.List;

public interface IPlayback {
    void setPlayList(List<Song> songs);

    boolean play();

    boolean play(int index);

    boolean play(List<Song> songs, int startIndex);

    boolean play(Song song);

    boolean playLast();

    boolean playNext();

    boolean pause();

    boolean isPlaying();

    int getProgress();

    Song getPlayingSong();

    boolean seekTo(int progress);


    void registerCallback(Callback callback);

    void unregisterCallback(Callback callback);

    void removeCallbacks();

    void releasePlayer();

    interface Callback {

        void onSwitchLast(@Nullable Song last);

        void onSwitchNext(@Nullable Song next);

        void onComplete(@Nullable Song next);

        void onPlayStatusChanged(boolean isPlaying);
    }
}
