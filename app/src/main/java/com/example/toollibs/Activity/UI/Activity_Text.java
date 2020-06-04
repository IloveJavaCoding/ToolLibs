package com.example.toollibs.Activity.UI;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.toollibs.OverWriteClass.SelfPasswordTransformationMethod;
import com.example.toollibs.R;
import com.example.toollibs.Util.SystemUtil;

public class Activity_Text extends AppCompatActivity {
    private TextView tvLamp;
    private EditText etPassword, etEnter, etDel;
    private Button bEnter;
    private ToggleButton tbControl;

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

        transformationMethod = new SelfPasswordTransformationMethod('@');
        etPassword.setTransformationMethod(transformationMethod);
    }

    private void setData() {
        tvLamp.setText(DEFAULT_TEXT);
        tvLamp.setTextColor(Color.YELLOW);
        //tvLamp.setTextColor(getResources().getColor(R.color.colorYellow));
        tvLamp.setSelected(true);
    }

    private void setListener() {
        bEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = etDel.getText().toString();
                if(temp!=null){
                    tvLamp.setText(temp);
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
                    etPassword.setTransformationMethod(transformationMethod.getInstance());
                }
            }
        });
    }
}
