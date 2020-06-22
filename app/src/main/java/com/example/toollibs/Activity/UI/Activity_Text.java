package com.example.toollibs.Activity.UI;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.toollibs.Activity.Config.Constant;
import com.example.toollibs.OverWriteClass.SelfPasswordTransformationMethod;
import com.example.toollibs.R;
import com.example.toollibs.SelfClass.MarqueeHorizontalText;
import com.example.toollibs.Util.DateUtil;
import com.example.toollibs.Util.SystemUtil;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Activity_Text extends AppCompatActivity {
    private TextView tvLamp, tvTest;
    private EditText etPassword, etEnter, etDel;
    private Button bEnter;
    private ToggleButton tbControl;
    private MarqueeHorizontalText view;
    private Spinner spinner1, spinner2;

    private String data1;
    private int data2;
    private String[] strArray;
    private List<Object> list;

    private SelfPasswordTransformationMethod transformationMethod;
    private static final String DEFAULT_TEXT = "What is faith? If it does't endure when we are tested the most?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__text);

        init();
        setData();
        setListener();
    }

    private void init() {
        tvLamp = findViewById(R.id.tvLamp);
        etPassword = findViewById(R.id.etPassword);
        etEnter = findViewById(R.id.etEnter);
        etDel = findViewById(R.id.etDel);

        bEnter = findViewById(R.id.bEnter);
        tbControl = findViewById(R.id.tbControl);
        view = findViewById(R.id.view);

        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);

        tvTest = findViewById(R.id.tvTest);
    }

    private void setData() {
        transformationMethod = new SelfPasswordTransformationMethod('@');
        etPassword.setTransformationMethod(transformationMethod);

        tvLamp.setText(DEFAULT_TEXT);
        tvLamp.setTextColor(Color.YELLOW);
        //tvLamp.setTextColor(getResources().getColor(R.color.colorYellow));
        tvLamp.setSelected(true);

        strArray = getResources().getStringArray(R.array.Date);
        int[] intArray = getResources().getIntArray(R.array.google_colors);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list = Arrays.stream(intArray).boxed().collect(Collectors.toList());
        }

        //set default value
        data1 = strArray[0];
        data2 = intArray[0];

        //set adapter
        ArrayAdapter<String> strAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, strArray);
        strAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(strAdapter);

        ArrayAdapter<Object> intAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        intAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(intAdapter);

        setMarquee(view);

    }

    private void setMarquee(MarqueeHorizontalText view){
        MarqueeHorizontalText marqueeText = view;
        marqueeText.setTextSize(25);
        marqueeText.setTextColor(Color.YELLOW);
        marqueeText.setBackgroundColor(Color.BLACK);
        marqueeText.setTextSpeed(8);
        marqueeText.setContent(DEFAULT_TEXT);
    }

    private void setListener() {
        bEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = etDel.getText().toString();
                if(temp!=null){
                    tvLamp.setText(temp);
                    view.setContent(temp);
                }
            }
        });

        //ues keyboard to control input
        etEnter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))){
                    //hide keyboard
                    ((InputMethodManager)etEnter.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(etEnter.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    //do something
                    SystemUtil.ShowToast(getApplicationContext(), etEnter.getText().toString());
                    return true;
                }
                return false;
            }
        });

        //hide/show password
        tbControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    etPassword.setTransformationMethod(transformationMethod);//no need .getInstance()
                }
            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //i --> index of array
                data1 = strArray[i];
                SystemUtil.ShowToast(getApplicationContext(),data1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                data2 = (int) list.get(i);
                SystemUtil.ShowToast(getApplicationContext(), data2+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
