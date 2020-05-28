package com.example.toollibs.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.toollibs.R;
import com.example.toollibs.Util.BitmapUtil;
import com.example.toollibs.Util.FileUtil;
import com.example.toollibs.Util.SystemUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Urat5168_Activity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvShow, tvTime;
    private Button bPath, bScreenCap, bReBoot, bScreenRotate, bImage;
    private ImageView imageView;
    private Thread thread;

    private String rootPath;
    private boolean isLocked = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urat5168__avtivity);

        init();
        setData();
        setListener();
    }

    private void init() {
        tvShow = findViewById(R.id.tvShow);
        tvTime = findViewById(R.id.tvTime);

        bPath = findViewById(R.id.bPath);
        bScreenCap = findViewById(R.id.bScreenCap);
        bReBoot = findViewById(R.id.bReBoot);
        bScreenRotate = findViewById(R.id.bRotate);
        bImage = findViewById(R.id.bImage);

        imageView = findViewById(R.id.image);
        rootPath = FileUtil.GetAppRootPth(this);
    }

    private void setData() {
        thread = new Thread(new TimeThread());
        thread.start();
    }

    private void setListener() {
        bPath.setOnClickListener(this);
        bScreenCap.setOnClickListener(this);
        bReBoot.setOnClickListener(this);
        bScreenRotate.setOnClickListener(this);
        bImage.setOnClickListener(this);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                Bundle bundle = msg.getData();
                tvTime.setText(bundle.getString("time"));
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bPath:
                tvShow.setText(rootPath);
                break;
            case R.id.bScreenCap:
                /*
                String rebootCmd = "screencap /" + rootPath+"/screenshot1.png";
                Intent intent = new Intent();
                intent.setAction("com.example.toollibs.execshellcommand");
                intent.putExtra("shell_command", rebootCmd);
                sendBroadcast(intent);
                 */

                SystemUtil.screenCap(this);
                break;
            case R.id.bReBoot:
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("确认重启么？")
                        .setPositiveButton("重启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SystemUtil.reBoot(getApplicationContext());
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 取消当前对话框
                                dialog.cancel();
                            }
                        }).show();
                break;
            case R.id.bImage:
                imageView.setImageBitmap(BitmapUtil.GetBitmapFromFile(rootPath+"/screenshot.png"));
                break;
        }
    }

    private String getCurTime(){
        Date date = Calendar.getInstance().getTime();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public class TimeThread implements Runnable{
        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = Message.obtain();
                message.what = 0;
                Bundle bundle = new Bundle();
                bundle.putString("time", getCurTime());
                message.setData(bundle);

                handler.sendMessage(message);
            }
        }
    }
}
