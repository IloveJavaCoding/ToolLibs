package com.example.toollibs.Activity.UI;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.example.toollibs.Activity.Config.Constant;
import com.example.toollibs.Activity.Config.SettingData;
import com.example.toollibs.R;
import com.example.toollibs.Util.DialogUtil;
import com.example.toollibs.Util.SystemUtil;

public class Activity_App_Setting extends AppCompatActivity implements View.OnClickListener {
    private RadioButton rbAutoStart;
    private LinearLayout layoutReboot;

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
        layoutReboot = findViewById(R.id.layoutReboot);
    }

    private void setData() {
            rbAutoStart.setChecked(SettingData.getBoolean(getApplicationContext(), Constant.CONFIG_FILE, Constant.AUTO_START_KEY, Constant.AUTO_START_DEFAULT));
    }

    private void setListener() {
        rbAutoStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingData.setBoolean(getApplicationContext(),Constant.CONFIG_FILE, Constant.AUTO_START_KEY, isChecked? true:false);
            }
        });

        layoutReboot.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layoutReboot:
                reBootDialog();
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

//        new AlertDialog.Builder(this)
//                .setTitle("提示")
//                .setMessage("确认重启么？")
//                .setPositiveButton("重启", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.d("tag", "reboot...");
//                        SystemUtil.reBoot(getApplicationContext());
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // 取消当前对话框
//                        dialog.cancel();
//                    }
//                }).show();
    }
}
