package com.nepalese.toollibs.Activity.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.MediaRouter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;

import com.nepalese.toollibs.Activity.Assist.VideoDisplay;
import com.nepalese.toollibs.R;
import com.nepalese.toollibs.Util.FileUtil;

import java.io.IOException;

public class Activity_View_DualScreen extends AppCompatActivity implements TextureView.SurfaceTextureListener{
    private TextureView textureView;
    private MediaPlayer mediaPlayer;

    private String[] videoFiles;

    private VideoDisplay presentation;
    private MediaRouter mMediaRouter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dual_screen);
        fullScreen();

        init();
        setDate();
        setListener();
    }

    private void fullScreen() {
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(option);
    }

    private void init() {
        textureView = findViewById(R.id.textureViewMain);
        mediaPlayer = new MediaPlayer();

        mMediaRouter = (MediaRouter)getSystemService(Context.MEDIA_ROUTER_SERVICE);
        updatePresentation();
    }

    private void setDate() {
        String path = FileUtil.getRootPath();

        videoFiles = new String[2];
        videoFiles[0] = path +"/video1.mp4";
        videoFiles[1] = path +"/video2.mp4";

        try {
            mediaPlayer.setDataSource(videoFiles[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        mediaPlayer.setLooping(true);
    }

    private void updatePresentation() {
        // Get the current route and its presentation display.
        MediaRouter.RouteInfo route = mMediaRouter.getSelectedRoute(MediaRouter.ROUTE_TYPE_LIVE_VIDEO);
        Display presentationDisplay = route != null ? route.getPresentationDisplay() : null;

        // Dismiss the current presentation if the display has changed.
        if (presentation != null && presentation.getDisplay() != presentationDisplay) {
            Log.i("tag", "Dismissing presentation because the current route no longer "
                    + "has a presentation display.");
            presentation.dismiss();
            presentation = null;
        }

        // Show a new presentation if needed.
        if (presentation == null && presentationDisplay != null) {
            Log.i("tag", "Showing presentation on display: " + presentationDisplay);
            presentation = new VideoDisplay(this, presentationDisplay, videoFiles[1]);
            presentation.setOnDismissListener(mOnDismissListener);
            try {
                presentation.show();
            } catch (WindowManager.InvalidDisplayException ex) {
                Log.w("tag", "Couldn't show presentation!  Display was removed in "
                        + "the meantime.", ex);
                presentation = null;
            }
        }
    }

    private final DialogInterface.OnDismissListener mOnDismissListener =
            new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (dialog == presentation) {
                        Log.i("tag", "Presentation was dismissed.");
                        presentation = null;
                    }
                }
            };


    private void setListener() {
        textureView.setSurfaceTextureListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.reset();
        mediaPlayer.stop();
        mediaPlayer.release();

        if(presentation!=null){
            presentation.dismiss();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        mediaPlayer.setSurface(new Surface(surfaceTexture));
        try {
           mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            return ;
        }
        mediaPlayer.start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture){
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }
}
