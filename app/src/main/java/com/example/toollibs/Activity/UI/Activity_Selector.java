package com.example.toollibs.Activity.UI;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.toollibs.R;
import com.example.toollibs.Util.SystemUtil;

public class Activity_Selector extends AppCompatActivity {
    private DatePicker datePicker;
    private TimePicker timePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__selector);

        init();
        setData();
        steListener();
    }

    private void init() {
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
    }

    private void setData() {
        timePicker.setIs24HourView(true);
    }

    private void steListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    SystemUtil.ShowToast(getApplicationContext(), year + " - " + monthOfYear + " - " + dayOfMonth);
                }
            });
        }

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                SystemUtil.ShowToast(getApplicationContext(), hourOfDay + " : " + minute);
            }
        });
    }

}
