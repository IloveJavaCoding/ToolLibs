package com.example.toollibs.Activity.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.toollibs.Activity.Config.Constant;
import com.example.toollibs.Activity.Config.SettingData;
import com.example.toollibs.R;
import com.example.toollibs.Util.BitmapUtil;
import com.example.toollibs.Util.DialogUtil;
import com.example.toollibs.Util.LanguageHelper;
import com.example.toollibs.Util.SystemUtil;

public class Activity_App_Setting extends AppCompatActivity implements View.OnClickListener {
    private CheckBox cbAutoStart;
    private LinearLayout layoutReboot;
    private TextView tvLanguage;
    private TextView tvVersion;
    private boolean isRegister = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__app__setting);

        init();
        setData();
        setListener();
        registerReceiver();
    }

    private void init() {
        cbAutoStart = findViewById(R.id.cbAutoStart);
        layoutReboot = findViewById(R.id.layoutReboot);
        tvLanguage = findViewById(R.id.tvLanguage);
        tvVersion = findViewById(R.id.tvVersionCode);
    }

    private void setData() {
        cbAutoStart.setChecked(SettingData.isAutoStart(this));
        tvLanguage.setText(Constant.LANGUAGE[SettingData.getLanguageIndex(this)]);
        tvVersion.setText(SystemUtil.getVersionName(this));
    }

    private void setListener() {
        cbAutoStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingData.saveAutoStart(getApplicationContext(), isChecked? true:false);
            }
        });

        layoutReboot.setOnClickListener(this);
        tvLanguage.setOnClickListener(this);
    }

    private void registerReceiver(){
        if (!isRegister){
            isRegister = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.ACTION_RESET_SETTING_PAGE);
            registerReceiver(mReceiver,filter);
        }
    }

    private void unRegisterReceiver(){
        if (isRegister){
            isRegister = false;
            unregisterReceiver(mReceiver);
        }
    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(Constant.ACTION_RESET_SETTING_PAGE.equals(action)){
                LanguageHelper.changeLanguage(SettingData.getLanguageIndex(context));
                //重启设置页
                onCreate(null);
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layoutReboot:
                reBootDialog();
                break;
            case R.id.tvLanguage:
                showLanguageDialog();
                break;
        }
    }

    private void reBootDialog() {
        DialogUtil.showMsgDialog(this, "提示", "确认重启么", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialogInterface.dismiss();
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        Log.d("tag", "reboot...");
                        SystemUtil.reBoot(getApplicationContext());
                        break;
                }
            }
        });
    }

    private void showLanguageDialog() {
        DialogUtil.showListDialog(this, Constant.LANGUAGE, getString(R.string.setting_language),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(SettingData.getLanguageIndex(getApplicationContext())!=which){
                            //change language
                            SettingData.saveLanguageIndex(getApplicationContext(), which);
                            sendBroadcast(new Intent(Constant.ACTION_RESET_SETTING_PAGE));
                        }
                        dialog.dismiss();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();
    }
}
