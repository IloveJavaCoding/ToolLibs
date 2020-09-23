package com.nepalese.toollibs.Activity.Component;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.MediaController;

import java.io.IOException;

/**
 * @author nepalese on 2020/9/18 08:59
 * @usage 自定义视频播放器
 */
public class VirgoVideoView extends SurfaceView implements MediaController.MediaPlayerControl {
    private static final String TAG = "VirgoVideoView";

    //播放器当前状态
    private static final int STATE_ERROR = -1;//报错
    private static final int STATE_IDLE = 0;//初始化
    private static final int STATE_PREPARING = 1;//准备
    private static final int STATE_PREPARED = 2;//已设定播放源
    private static final int STATE_PLAYING = 3;//正在播放
    private static final int STATE_PAUSED = 4;//暂停
    private static final int STATE_PLAYBACK_COMPLETED = 5;//播放完

    //监听
    private MediaPlayer.OnCompletionListener completionListener;//完成监听
    private MediaPlayer.OnPreparedListener preparedListener;//准备监听
    private MediaPlayer.OnErrorListener errorListener;//错误监听
//    private SurfaceHolder.Callback callback;//surface 监听

    private Context context;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private AudioManager audioManager;//音量控制服务

    private int videoWidth, videoHeight;
    private int curPosition;
    private Uri videoUri;//视频源

    //有初始化变量
    private int curState = STATE_IDLE;//当前状态
    private boolean isLooping = false;//循环播放

    public VirgoVideoView(Context context) {
        this(context, null);
    }

    public VirgoVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VirgoVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        videoWidth = 0;
        videoHeight = 0;

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        getHolder().addCallback(callback);

        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Log.i("@@@@", "onMeasure(" + MeasureSpec.toString(widthMeasureSpec) + ", "
        //        + MeasureSpec.toString(heightMeasureSpec) + ")");

        int width = getDefaultSize(videoWidth, widthMeasureSpec);
        int height = getDefaultSize(videoHeight, heightMeasureSpec);
        if (videoWidth > 0 && videoHeight > 0) {

            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

            if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
                // the size is fixed
                width = widthSpecSize;
                height = heightSpecSize;

                // for compatibility, we adjust size based on aspect ratio
                if ( videoWidth * height  < width * videoHeight ) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * videoWidth / videoHeight;
                } else if ( videoWidth * height  > width * videoHeight ) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * videoHeight / videoWidth;
                }
            } else if (widthSpecMode == MeasureSpec.EXACTLY) {
                // only the width is fixed, adjust the height to match aspect ratio if possible
                width = widthSpecSize;
                height = width * videoHeight / videoWidth;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    height = heightSpecSize;
                }
            } else if (heightSpecMode == MeasureSpec.EXACTLY) {
                // only the height is fixed, adjust the width to match aspect ratio if possible
                height = heightSpecSize;
                width = height * videoWidth / videoHeight;
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    width = widthSpecSize;
                }
            } else {
                // neither the width nor the height are fixed, try to use actual video size
                width = videoWidth;
                height = videoHeight;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // too tall, decrease both width and height
                    height = heightSpecSize;
                    width = height * videoWidth / videoHeight;
                }
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // too wide, decrease both width and height
                    width = widthSpecSize;
                    height = width * videoHeight / videoWidth;
                }
            }
        } else {
            // no size yet, just adopt the given spec sizes
        }
        setMeasuredDimension(width, height);
    }

    //===========================================get/set============================================
    public void setCompletionListener(MediaPlayer.OnCompletionListener completionListener) {
        this.completionListener = completionListener;
    }

    public void setPreparedListener(MediaPlayer.OnPreparedListener preparedListener) {
        this.preparedListener = preparedListener;
    }

    public void setErrorListener(MediaPlayer.OnErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    public void setVideoPath(String path) {
        setVideoUri(Uri.parse(path));
    }

    public void setVideoUri(Uri videoUri) {
        this.videoUri = videoUri;
        openVideo();
    }

    public void setLooping(boolean looping) {
        isLooping = looping;
    }

    public void stopPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            curState = STATE_IDLE;
        }
    }

    public void continuePlay(){
        if(curState == STATE_PAUSED){
            mediaPlayer.start();
            mediaPlayer.seekTo(curPosition);
            curState = STATE_PLAYING;
        }
    }

    //=========================================private==============================================
    /**
     * @return 是否处于可播放状态
     */
    private boolean isInPlaybackState() {
        return this.mediaPlayer!= null && this.curState != -1 && this.curState != 0 && this.curState != 1;
    }

    /**
     * 初始化player, 设立监听
     */
    private void openVideo() {
        if (videoUri == null || surfaceHolder == null) {
            // not ready for playback just yet, will try again later
            return;
        }
        release();

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(onPreparedListener);
            mediaPlayer.setOnCompletionListener(onCompletionListener);
            mediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
            mediaPlayer.setOnErrorListener(onErrorListener);

            mediaPlayer.setDataSource(context, videoUri);
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.prepareAsync();
        }catch (IOException e) {
            e.printStackTrace();
            curState = STATE_ERROR;
            errorListener.onError(mediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        }

        curState = STATE_PREPARING;
    }

    private void release(){
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;

            curState = STATE_IDLE;
        }
    }
    //============================================override==========================================
    @Override
    public void start() {
        if(isInPlaybackState()){
            mediaPlayer.start();
            mediaPlayer.setLooping(isLooping);
            curState = STATE_PLAYING;
        }
    }

    @Override
    public void pause() {
        if(curState==STATE_PLAYING){
            mediaPlayer.pause();
            curState = STATE_PAUSED;
            curPosition = mediaPlayer.getCurrentPosition();
        }
    }

    @Override
    public int getDuration() {
        if (isInPlaybackState()) {
            return mediaPlayer.getDuration();
        }

        return -1;
    }

    @Override
    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        if (isInPlaybackState()) {
            mediaPlayer.seekTo(pos);
        }
    }

    @Override
    public boolean isPlaying() {
        return isInPlaybackState() && mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        boolean isKeyCodeSupported = keyCode != KeyEvent.KEYCODE_BACK &&
                keyCode != KeyEvent.KEYCODE_VOLUME_UP &&
                keyCode != KeyEvent.KEYCODE_VOLUME_DOWN &&
                keyCode != KeyEvent.KEYCODE_VOLUME_MUTE &&
                keyCode != KeyEvent.KEYCODE_MENU &&
                keyCode != KeyEvent.KEYCODE_CALL &&
                keyCode != KeyEvent.KEYCODE_ENDCALL;
        if (isInPlaybackState() && isKeyCodeSupported) {
            if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK ||
                    keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                if (mediaPlayer.isPlaying()) {
                    pause();
                } else {
                    start();
                }
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
                if (!mediaPlayer.isPlaying()) {
                    start();
                }
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                    || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
                if (mediaPlayer.isPlaying()) {
                    pause();
                }
                return true;
            } else {
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    //==============================================================================================
    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            curState = STATE_PREPARED;
            if (preparedListener != null) {
                preparedListener.onPrepared(mediaPlayer);
            }
        }
    };

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            curState = STATE_PLAYBACK_COMPLETED;
            if (completionListener != null) {
                completionListener.onCompletion(mediaPlayer);
            }
        }
    };

    private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
            curPosition = STATE_ERROR;

            /* If an error handler has been supplied, use it and finish. */
            if (errorListener != null) {
                if (errorListener.onError(mediaPlayer, framework_err, impl_err)) {
                    return true;
                }
            }
            return true;
        }
    };

    private MediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            videoWidth = mp.getVideoWidth();
            videoHeight = mp.getVideoHeight();

            if (videoWidth != 0 && videoHeight != 0) {
                getHolder().setFixedSize(videoWidth, videoHeight);
                requestLayout();
            }
        }
    };

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            surfaceHolder = holder;
            openVideo();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            surfaceHolder = null;
            release();
        }
    };
}
