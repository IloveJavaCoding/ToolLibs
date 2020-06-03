package com.example.toollibs.Activity.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.example.toollibs.R;

public class Activity_Bar_ProcessBar extends AppCompatActivity {
    private ProgressBar progressBar1, progressBar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__bar__process_bar);

        init();
        setData();
        setListener();
    }

    private void init() {
        progressBar1 = findViewById(R.id.processBar1);
        progressBar2 = findViewById(R.id.processBar2);
    }

    private void setData() {
    }

    private void setListener() {
    }
}
