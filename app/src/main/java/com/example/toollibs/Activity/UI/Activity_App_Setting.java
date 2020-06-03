package com.example.toollibs.Activity.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.example.toollibs.Activity.Config.Constant;
import com.example.toollibs.Activity.Config.SettingData;
import com.example.toollibs.R;

public class Activity_App_Setting extends AppCompatActivity {
    private RadioButton rbAutoStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__app__setting);

        init();
        setData();
        setListener();
    }

    private void init() {
        rbAutoStart = findViewById(R.id.rbAutoStart);
    }

    private void setData() {
        rbAutoStart.setChecked(SettingData.getBoolean(getApplicationContext(), SettingData.AUTO_START_KEY, Constant.AUTO_START_DEFAULT));
    }

    private void setListener() {
        rbAutoStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingData.setBoolean(getApplicationContext(), SettingData.AUTO_START_KEY, isChecked? true:false);
            }
        });
    }
}
