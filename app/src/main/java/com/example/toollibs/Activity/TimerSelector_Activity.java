package com.example.toollibs.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.toollibs.R;
import com.example.toollibs.SelfClass.TimeSelector;
import com.example.toollibs.Util.DateUtil;

import java.util.Date;

public class TimerSelector_Activity extends AppCompatActivity {
    private TextView tvBirthday;
    private Button bChoose;
    private RadioGroup radioGroup;

    private int mode = 0;
    private final String minTime = "1900-01-01 01:01";
    private final String maxTime = "2050-12-30 23:59";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_selector_);

        init();
    }

    private void init() {
        tvBirthday = findViewById(R.id.tvBirth);
        bChoose = findViewById(R.id.bChoose);
        radioGroup = findViewById(R.id.radioG);

        bChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeSelector();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {//i = id
                switch (i){
                    case R.id.rbLeft:
                        mode = 0;
                        break;
                    case R.id.rbRight:
                        mode = 1;
                        break;
                }
            }
        });
    }

    private void timeSelector(){
        TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(Date time) {
                if(mode==1){
                    tvBirthday.setText(DateUtil.Date2String(time, "yyyy-MM-dd HH:mm"));
                }else{
                    tvBirthday.setText(DateUtil.Date2String(time, "yyyy-MM-dd"));
                }
            }
        }, minTime, maxTime);

        switch(mode){
            case 0:
                timeSelector.setMode(TimeSelector.MODE.YMD);
                break;
            case 1:
                timeSelector.setMode(TimeSelector.MODE.YMDHM);
                break;
        }
        timeSelector.show();
        timeSelector.setIsLoop(false);
    }
}
