package com.example.toollibs.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.example.toollibs.Activity.UI.Activity_Bar_SeekBar;
import com.example.toollibs.Activity.UI.Activity_Button;
import com.example.toollibs.Activity.UI.Activity_Selector;
import com.example.toollibs.Activity.UI.Activity_Show_Data;
import com.example.toollibs.Activity.UI.Activity_Text;
import com.example.toollibs.Activity.UI.Activity_View;
import com.example.toollibs.R;
import com.example.toollibs.Activity.Interface.NetworkType;
import com.example.toollibs.Util.SystemUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NetStateChangeObserver {
    private Button bPoster, bFragment, bDialog, bTimeSelector, b5168, bTime;

    private Button bText, bButton, bView, bSelector, bShow, bBar, bDemo, bSetting;

    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetStateChangeReceiver.registerReceiver(this);

        init();
        setListener();
    }

    private void init() {
        bText = findViewById(R.id.bText);
        bButton = findViewById(R.id.bButton);
        bView = findViewById(R.id.bView);
        bSelector = findViewById(R.id.bSelector);
        bShow = findViewById(R.id.bShow);
        bBar = findViewById(R.id.bBar);
        bDemo = findViewById(R.id.bDemo);
        bSetting = findViewById(R.id.bSetting);

        bPoster = findViewById(R.id.bPosterView);
        bFragment = findViewById(R.id.bFragment);
        bDialog = findViewById(R.id.bDialog);
        bTimeSelector = findViewById(R.id.bTimeSelector);
        b5168 = findViewById(R.id.b5168);
        bTime = findViewById(R.id.bTime);

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
        bSetting.setOnClickListener(this);

        bPoster.setOnClickListener(this);
        bFragment.setOnClickListener(this);
        bDialog.setOnClickListener(this);
        bTimeSelector.setOnClickListener(this);
        b5168.setOnClickListener(this);
        bTime.setOnClickListener(this);
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
                //intent = new Intent(this, Activity_View.class);
                break;
            case R.id.bSetting:
                intent = new Intent(this, Activity_App_Setting.class);
                break;


            case R.id.bPosterView:
                intent = new Intent(this, PosterView_Activity.class);
                break;
            case R.id.bFragment:
                intent = new Intent(this, Fragment_Activity.class);
                break;
            case R.id.bDialog:
                intent = new Intent(this, Dialog_Activity.class);
                break;
            case R.id.bTimeSelector:
                intent = new Intent(this, TimerSelector_Activity.class);
                break;
            case R.id.b5168:
                intent = new Intent(this, Urat5168_Activity.class);
                break;
            case R.id.bTime:
                intent = new Intent(this, Time_Activity.class);
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
