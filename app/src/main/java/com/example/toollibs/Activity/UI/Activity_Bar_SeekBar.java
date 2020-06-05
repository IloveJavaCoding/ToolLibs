package com.example.toollibs.Activity.UI;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.toollibs.OverWriteClass.VerticalSeekBar;
import com.example.toollibs.R;
import com.example.toollibs.Util.SystemUtil;

public class Activity_Bar_SeekBar extends AppCompatActivity {
    private EditText r,g,b;
    private View view;
    private TextView notice, tvVSB;
    private SeekBar barR, barG, barB;
    private VerticalSeekBar verticalSeekBar;
    private Integer iR, iG, iB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_bar);

        init();
        setListener();
    }

    private void init() {
        r = findViewById(R.id.Rin);
        g = findViewById(R.id.Gin);
        b = findViewById(R.id.Bin);

        view = findViewById(R.id.color);
        notice = findViewById(R.id.tv_notice);
        tvVSB = findViewById(R.id.tvVSB);

        barR = findViewById(R.id.bar_r);
        barG = findViewById(R.id.bar_g);
        barB = findViewById(R.id.bar_b);
        verticalSeekBar = findViewById(R.id.vSeekBar);

        setData();
    }

    private void setData() {
        iR = 0;
        iG = 0;
        iB = 0;

        barR.setMax(255);
        barG.setMax(255);
        barB.setMax(255);

        verticalSeekBar.setMax(100);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            barR.getThumb().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);//滑块
            barR.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);//进度条

            barG.getThumb().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            barG.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);

            barB.getThumb().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
            barB.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void setListener() {
        barR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                notice.setTextColor(Color.RED);
                notice.setText(""+seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                notice.setText("");
                iR = seekBar.getProgress();
                r.setText(""+iR);
                Rendering(iR, iG, iB);
            }
        });

        barG.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                notice.setTextColor(Color.GREEN);
                notice.setText(""+seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                notice.setText("");
                iG = seekBar.getProgress();
                g.setText(""+iG);
                Rendering(iR, iG, iB);
            }
        });

        barB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                notice.setTextColor(Color.BLUE);
                notice.setText(""+seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                notice.setText("");
                iB = seekBar.getProgress();
                b.setText(""+iB);
                Rendering(iR, iG, iB);
            }
        });

        r.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    int input = Integer.parseInt(r.getText().toString());
                    if(input>255){
                        input = 255;
                        r.setText("255");
                    }
                    barR.setProgress(input);
                }
            }
        });

        g.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    int input = Integer.parseInt(g.getText().toString());
                    if(input>255){
                        input = 255;
                        g.setText("255");
                    }
                    barG.setProgress(input);
                }
            }
        });

        b.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    int input = Integer.parseInt(b.getText().toString());
                    if(input>255){
                        input = 255;
                        b.setText("255");
                    }
                    barB.setProgress(input);
                }
            }
        });

        verticalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("tag", "seek bar change...");
                tvVSB.setText(""+seekBar.getProgress());
            }
        });
    }

    private void Rendering(int r, int g, int b){
        GradientDrawable back=(GradientDrawable) view.getBackground();
        back.setColor(Color.rgb(r,g,b));
    }
}
