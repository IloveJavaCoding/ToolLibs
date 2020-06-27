package com.example.toollibs.Activity.Assist;

import android.app.Presentation;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.TextureView;

import com.example.toollibs.R;

import java.io.IOException;

public class VideoDisplay extends Presentation implements TextureView.SurfaceTextureListener {
    private TextureView textureView;
    private MediaPlayer mediaPlayer;
    private String path;

    public VideoDisplay(Context outerContext, Display display, String path) {
        super(outerContext, display);
        this.path = path;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dual_screen_second);

        init();
        setData();
        setListener();
    }

    private void init() {
        textureView = findViewById(R.id.textureViewDual);
        mediaPlayer=new MediaPlayer();
    }

    private void setData() {
        try {
            mediaPlayer.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
            return ;
        }

        mediaPlayer.setLooping(true);
    }

    private void setListener() {
        textureView.setSurfaceTextureListener(this);
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
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }
}
