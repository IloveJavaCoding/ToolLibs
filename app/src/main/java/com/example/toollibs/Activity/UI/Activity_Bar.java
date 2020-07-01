package com.example.toollibs.Activity.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.toollibs.R;

public class Activity_Bar extends AppCompatActivity implements View.OnClickListener {
    private Button bSeekBar, bProcessBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__bar);

        init();
        setListener();
    }

    private void init() {
        bSeekBar = findViewById(R.id.bSeekBar);
        bProcessBar = findViewById(R.id.bProcessBar);

    }

    private void setListener() {
        bSeekBar.setOnClickListener(this);
        bProcessBar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.bSeekBar:
                intent = new Intent(this, Activity_Bar_SeekBar.class);
                break;
            case R.id.bProcessBar:
                intent = new Intent(this, Activity_Bar_ProcessBar.class);
                break;
        }
        startActivity(intent);
    }
}
