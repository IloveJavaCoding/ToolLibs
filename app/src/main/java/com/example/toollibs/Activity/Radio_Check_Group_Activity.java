package com.example.toollibs.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.toollibs.R;
import com.example.toollibs.Util.SystemUtil;

public class Radio_Check_Group_Activity extends AppCompatActivity {
    private RadioGroup radioGroup1, radioGroup2, radioGroup3;
    private RadioButton rButton11, rButton12, rButton21, rButton22, rButton31, rButton32;

    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_check_group);

        init();
        setListener();
    }

    private void init() {
        radioGroup1 = findViewById(R.id.rGroup1);
        radioGroup2 = findViewById(R.id.rGroup2);
        radioGroup3 = findViewById(R.id.rGroup3);

        rButton11 = findViewById(R.id.rButton1);
        rButton12 = findViewById(R.id.rButton2);
        rButton21 = findViewById(R.id.rButton3);
        rButton22 = findViewById(R.id.rButton4);
        rButton31 = findViewById(R.id.rButton5);
        rButton32 = findViewById(R.id.rButton6);

        checkBox1 = findViewById(R.id.checkbox1);
        checkBox2 = findViewById(R.id.checkbox2);
        checkBox3 = findViewById(R.id.checkbox3);
        checkBox4 = findViewById(R.id.checkbox4);
        checkBox5 = findViewById(R.id.checkbox5);
    }

    private void setListener() {
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rButton1:

                        break;
                    case R.id.rButton2:

                        break;
                }
            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rButton3:
                        rButton21.setTextColor(getResources().getColor(R.color.colorWhite));
                        rButton22.setTextColor(getResources().getColor(R.color.colorBlack));
                        break;
                    case R.id.rButton4:
                        rButton22.setTextColor(getResources().getColor(R.color.colorWhite));
                        rButton21.setTextColor(getResources().getColor(R.color.colorBlack));
                        break;
                }
            }
        });

        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rButton5:

                        break;
                    case R.id.rButton6:

                        break;
                }
            }
        });

        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    SystemUtil.ShowToast(getApplicationContext(), checkBox1.getText().toString());
                }
            }
        });

        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    SystemUtil.ShowToast(getApplicationContext(), checkBox2.getText().toString());
                }
            }
        });

        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    SystemUtil.ShowToast(getApplicationContext(), checkBox3.getText().toString());
                }
            }
        });

        checkBox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    SystemUtil.ShowToast(getApplicationContext(), checkBox4.getText().toString());
                }
            }
        });

        checkBox5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    SystemUtil.ShowToast(getApplicationContext(), checkBox5.getText().toString());
                }
            }
        });
    }
}
