package com.example.toollibs.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.example.toollibs.Activity.Interface.NetStateChangeObserver;
import com.example.toollibs.Activity.Receiver.NetStateChangeReceiver;
import com.example.toollibs.Activity.UI.Activity_App_Setting;
import com.example.toollibs.Activity.UI.Activity_Bar;
import com.example.toollibs.Activity.UI.Activity_Button;
import com.example.toollibs.Activity.UI.Activity_DataBase;
import com.example.toollibs.Activity.UI.Activity_Demos;
import com.example.toollibs.Activity.UI.Activity_Dialog_Notification;
import com.example.toollibs.Activity.UI.Activity_Intent;
import com.example.toollibs.Activity.UI.Activity_Selector;
import com.example.toollibs.Activity.UI.Activity_Service;
import com.example.toollibs.Activity.UI.Activity_Show_Data;
import com.example.toollibs.Activity.UI.Activity_Text;
import com.example.toollibs.Activity.UI.Activity_View;
import com.example.toollibs.R;
import com.example.toollibs.Activity.Interface.NetworkType;
import com.example.toollibs.Util.SystemUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NetStateChangeObserver {
    private Button bDialog;

    private Button bText, bButton, bView, bSelector, bShow, bBar, bDemo, bIntent, bService, bDatabase, bSetting;

    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CALL_PHONE

    };
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLayout();
        NetStateChangeReceiver.registerReceiver(this);

        init();
        setListener();
    }

    private void setLayout() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void init() {
        bText = findViewById(R.id.bText);
        bButton = findViewById(R.id.bButton);
        bView = findViewById(R.id.bView);
        bSelector = findViewById(R.id.bSelector);
        bShow = findViewById(R.id.bShow);
        bBar = findViewById(R.id.bBar);
        bDemo = findViewById(R.id.bDemo);
        bIntent = findViewById(R.id.bIntent);
        bService = findViewById(R.id.bService);
        bDatabase = findViewById(R.id.bDatabase);
        bSetting = findViewById(R.id.bSetting);

        bDialog = findViewById(R.id.bDialog);

        if(!SystemUtil.CheckPermission(this, NEEDED_PERMISSIONS)){
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
            return;
        }
    }

    private void setListener() {
        bText.setOnClickListener(this);
        bButton.setOnClickListener(this);
        bView.setOnClickListener(this);
        bSelector.setOnClickListener(this);
        bShow.setOnClickListener(this);
        bBar.setOnClickListener(this);
        bDemo.setOnClickListener(this);
        bIntent.setOnClickListener(this);
        bService.setOnClickListener(this);
        bDatabase.setOnClickListener(this);
        bSetting.setOnClickListener(this);

        bDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()){
            case R.id.bText:
                intent = new Intent(this, Activity_Text.class);
                break;
            case R.id.bButton:
                intent = new Intent(this, Activity_Button.class);
                break;
            case R.id.bView:
                intent = new Intent(this, Activity_View.class);
                break;
            case R.id.bSelector:
                intent = new Intent(this, Activity_Selector.class);
                break;
            case R.id.bShow:
                intent = new Intent(this, Activity_Show_Data.class);
                break;
            case R.id.bBar:
                intent = new Intent(this, Activity_Bar.class);
                break;
            case R.id.bDemo:
                intent = new Intent(this, Activity_Demos.class);
                break;
            case R.id.bIntent:
                intent = new Intent(this, Activity_Intent.class);
                break;
            case R.id.bService:
                intent = new Intent(this, Activity_Service.class);
                break;
            case R.id.bDatabase:
                intent = new Intent(this, Activity_DataBase.class);
                break;
            case R.id.bSetting:
                intent = new Intent(this, Activity_App_Setting.class);
                break;

            case R.id.bDialog:
                intent = new Intent(this, Activity_Dialog_Notification.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isAllGranted = true;
        for (int grantResult : grantResults) {
            isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
        }

        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            if (isAllGranted) {
                //get all requested permissions

            } else {
                SystemUtil.ShowToast(getApplicationContext(),"Permission denied!");
                finish();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onNetDisconnected() {
        SystemUtil.ShowToast(getApplicationContext(),"Connected");
    }

    @Override
    public void onNetConnected(NetworkType networkType) {
        SystemUtil.ShowToast(getApplicationContext(),"DisConnected");
    }

    @Override
    protected void onDestroy() {
        NetStateChangeReceiver.unRegisterReceiver(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume( );
        NetStateChangeReceiver.registerObserver(this);
    }

    @Override
    protected void onPause() {
        super.onPause( );
        NetStateChangeReceiver.unRegisterObserver(this);
    }
}
