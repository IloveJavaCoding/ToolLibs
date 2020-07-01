package com.example.toollibs.Activity.UI;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.toollibs.R;

import java.util.ArrayList;
import java.util.List;

public class Activity_Animator extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgArrow;
    private Button bStop;

    private List<Drawable> drawables;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__animator);

        init();
        setData();
        setListener();
    }

    private void init() {
        imgArrow = findViewById(R.id.img_arrow);
        
        bStop = findViewById(R.id.bStopAll);
    }

    private void setData() {
        drawables = new ArrayList<>();
    }

    private void setListener() {
        imgArrow.setOnClickListener(this);
        
        bStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_arrow:
                playGroupAnim(imgArrow);
                break;
                
            case R.id.bStopAll:
                stopAll();
                break;
        }
    }

    private void stopAll() {
        for(Drawable drawable: drawables){
            ((Animatable) drawable).stop();
        }
        drawables.clear();
    }

    private void playGroupAnim(ImageView imageView){
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof Animatable) {
            if(!drawables.contains((drawable))){
                drawables.add(drawable);
            }
            ((Animatable) drawable).start();
        }
    }
}