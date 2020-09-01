package com.example.toollibs.Activity.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.example.toollibs.OverWriteClass.SlideToggleButton;
import com.example.toollibs.R;
import com.example.toollibs.Util.BitmapUtil;
import com.example.toollibs.Util.SystemUtil;

public class Activity_Button extends AppCompatActivity {
    private Button button;
    private ImageButton imageButton;

    private RadioGroup radioGroup1, radioGroup2, radioGroup3;
    private RadioButton rButton11, rButton12, rButton21, rButton22, rButton31, rButton32;
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5;

    private ToggleButton tbDefault, tbSelector;
    private SlideToggleButton slideToggleButton;

    private Switch switchDefault;

    private boolean isGreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__button);

        init();
        setData();
        setListener();
    }

    private void init() {
        button = findViewById(R.id.button);
        imageButton = findViewById(R.id.imageButton);
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

        tbDefault = findViewById(R.id.tbDefault);
        tbSelector = findViewById(R.id.tbSelector);
        slideToggleButton = findViewById(R.id.stbSlide);

        switchDefault = findViewById(R.id.switchDefault);
    }

    private void setData() {
        rButton21.setChecked(true);
        isGreen = true;

        slideToggleButton.setToggle_bkg_on(BitmapUtil.getBitmapFromRes(getApplicationContext(), R.drawable.icon_toggle_on));
        slideToggleButton.setToggle_bkg_off(BitmapUtil.getBitmapFromRes(getApplicationContext(), R.drawable.icon_toggle_off));
        slideToggleButton.setToggle_slip(BitmapUtil.getBitmapFromRes(getApplicationContext(), R.drawable.icon_toggle_slip));
        slideToggleButton.setIsChecked(false);
    }

    private void setListener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemUtil.showToast(getApplicationContext(), "Click");
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isGreen){
                    imageButton.setImageDrawable(getResources().getDrawable(R.drawable.icon_image_button_red));
                    isGreen = false;
                }else{
                    imageButton.setImageDrawable(getResources().getDrawable(R.drawable.icon_image_button));
                    //imageButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_image_button));
                }
            }
        });

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
                    SystemUtil.showToast(getApplicationContext(), checkBox1.getText().toString());
                }
            }
        });

        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    SystemUtil.showToast(getApplicationContext(), checkBox2.getText().toString());
                }
            }
        });

        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    SystemUtil.showToast(getApplicationContext(), checkBox3.getText().toString());
                }
            }
        });

        checkBox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    SystemUtil.showToast(getApplicationContext(), checkBox4.getText().toString());
                }
            }
        });

        checkBox5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    SystemUtil.showToast(getApplicationContext(), checkBox5.getText().toString());
                }
            }
        });

        tbDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //checked
                    SystemUtil.showToast(getApplicationContext(), "Open");
                }else{
                    SystemUtil.showToast(getApplicationContext(), "Off");
                }
            }
        });

        tbSelector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //checked
                    SystemUtil.showToast(getApplicationContext(), "Open");
                }else{
                    SystemUtil.showToast(getApplicationContext(), "Off");
                }
            }
        });

        slideToggleButton.setOnisCheckedChangeListener(new SlideToggleButton.OnisCheckedChangeListener() {
            @Override
            public void onisCheckedChange(Boolean state) {
                if(state){
                    SystemUtil.showToast(getApplicationContext(), "Open");
                }else{
                    SystemUtil.showToast(getApplicationContext(), "Off");
                }
            }
        });

        switchDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //checked
                    SystemUtil.showToast(getApplicationContext(), "Open");
                }else{
                    SystemUtil.showToast(getApplicationContext(), "Off");
                }
            }
        });
    }
}
