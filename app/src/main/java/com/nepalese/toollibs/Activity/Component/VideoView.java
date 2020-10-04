package com.nepalese.toollibs.Activity.Component;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * @author nepalese on 2020/9/18 11:11
 * @usage
 */
public class VideoView extends VirgoVideoView {
    private static final String TAG = "VideoView";

    private Context context;

    private List<String> url = null;
    private int currentIndex = 0;
    private boolean hasSetUrl = false;
    private boolean hasPause = false;

    public VideoView(Context context) {
        this(context, null);
    }

    public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init(){
        setLooping(false);
        setCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion: complete");
                load();
            }
        });

        setErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                load();
                return true;
            }
        });
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == GONE && isPlaying()) {
            hasPause = true;
            pause();
        } else {
            if (hasPause) {
                start();
            }
        }
    }

    public VideoView setUrl(List<String> urls) {
        if (urls != null && !urls.isEmpty()) {
            url = urls;
            hasSetUrl = true;
            currentIndex = 0;
        }
        return this;
    }

    public void play() {
        if (!hasSetUrl || url == null || url.isEmpty()) {
            return;
        }

        load();
        hasPause = false;
    }

    private void load() {
        if (url == null || url.isEmpty()) return;
        if (currentIndex >= url.size()) {
            currentIndex = 0;
        }

        Log.d(TAG, "play: " + url.get(currentIndex));
        setVideoPath(url.get(currentIndex));
        currentIndex++;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopPlay();
        clearFocus();
    }
}
