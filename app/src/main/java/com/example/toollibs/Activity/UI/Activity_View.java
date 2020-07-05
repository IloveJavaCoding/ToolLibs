package com.example.toollibs.Activity.UI;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.toollibs.R;

public class Activity_View extends AppCompatActivity implements View.OnClickListener {
    private Button bImageView, bWebView, bCalendarView, bTextureView, bVideoView, bMapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__view);

        setLayout();
        init();
        setListener();
    }

    private void setLayout() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void init() {
        bImageView = findViewById(R.id.bImageView);
        bWebView = findViewById(R.id.bWebView);
        bCalendarView = findViewById(R.id.bCalendarView);
        bVideoView = findViewById(R.id.bVideoView);
        bTextureView = findViewById(R.id.bTextureView);
        bMapView = findViewById(R.id.bMapView);
    }

    private void setListener() {
        bImageView.setOnClickListener(this);
        bWebView.setOnClickListener(this);
        bCalendarView.setOnClickListener(this);
        bVideoView.setOnClickListener(this);
        bTextureView.setOnClickListener(this);
        bMapView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.bImageView:
                intent = new Intent(this, Activity_View_ImageView.class);
                break;
            case R.id.bWebView:
                intent = new Intent(this, Activity_View_WebView.class);
                break;
            case R.id.bCalendarView:
                intent = new Intent(this, Activity_View_Calendar.class);
                break;
            case R.id.bVideoView:
                intent = new Intent(this, Activity_View_VideoView.class);
                break;
            case R.id.bTextureView:
                intent = new Intent(this, Activity_View_DualScreen.class);
                break;
            case R.id.bMapView:
                intent = new Intent(this, Activity_View_MapView.class);
                break;
        }
        startActivity(intent);
    }
}
