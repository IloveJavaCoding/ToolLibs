package com.example.toollibs.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.toollibs.OverWriteClass.EditTextDelIcon;
import com.example.toollibs.R;
import com.example.toollibs.Util.BitmapUtil;
import com.example.toollibs.Util.SystemUtil;

public class Dialog_Activity extends AppCompatActivity implements View.OnClickListener{
    private Button bDialog1, bDialog2, bDialog3, bDialog4;
    private Button bNotification;
    private EditTextDelIcon etInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_);

        init();
        setListener();
    }

    private void init() {
        bDialog1 = findViewById(R.id.bDialog1);
        bDialog2 = findViewById(R.id.bDialog2);
        bDialog3 = findViewById(R.id.bDialog3);
        bDialog4 = findViewById(R.id.bDialog4);

        etInput = findViewById(R.id.etDel);
        bNotification = findViewById(R.id.bNotification);
    }

    private void setListener() {
        bDialog1.setOnClickListener(this);
        bDialog2.setOnClickListener(this);
        bDialog3.setOnClickListener(this);
        bDialog4.setOnClickListener(this);
        bNotification.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bDialog1:
                showDefaultDialog();
                break;
            case R.id.bDialog2:
                showTextDialog();
                break;
            case R.id.bDialog3:
                showSelfDefineDialog();
                break;
            case  R.id.bDialog4:
                showSelfDefineDialog2();
                break;
            case R.id.bNotification:
                String msg = etInput.getText().toString();
                callThread(5, msg==null?"msg":msg);
                break;
        }
    }

    private void sendNotification(String channelId, String title, String msg){
        //1. get notification service
        NotificationManager manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //2. create notification  channel version>26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelName = "默认通知";
            manager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
        }

        //3. set intent(active when click the notification)
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        //4. init notification, get instance
        Notification notification = new NotificationCompat.Builder(this,"default")
                .setContentTitle(title) //title
                .setContentText(msg) //message
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)//intent
                .setAutoCancel(true)//cancel the notification when click head
                .setFullScreenIntent(pendingIntent, true)  //hang the notification
                .build();

                //.setCategory(Notification.CATEGORY_MESSAGE) //version>21
                //.setVisibility(Notification.VISIBILITY_PUBLIC)// can be show in the Locker page >21

        /**
         * 1.VISIBILITY_PRIVATE : 显示基本信息，如通知的图标，但隐藏通知的全部内容；
         * 2.VISIBILITY_PUBLIC : 显示通知的全部内容；
         * 3.VISIBILITY_SECRET : 不显示任何内容，包括图标。
         */

        //5. send notification
        manager.notify(1,notification);
    }

    private void callThread(final int seconds, final String msg){
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(seconds*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendNotification("channel1", "Title", msg);
                super.run();
            }
        }.start();
    }


    private void showDefaultDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SystemUtil.ShowToast(getApplicationContext(),"Confirm");
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showTextDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");
        builder.setMessage("Msg");
        builder.setIcon(R.drawable.icon_question);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SystemUtil.ShowToast(getApplicationContext(),"Confirm");
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showSelfDefineDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setting layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view);

        Button bCancel = view.findViewById(R.id.bCancel);
        Button bConfirm = view.findViewById(R.id.bConfirm);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        final EditText etInput = view.findViewById(R.id.etInput);

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        bConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = etInput.getText().toString();
                if(!TextUtils.isEmpty(input)){
                    SystemUtil.ShowToast(getApplicationContext(),"Your name is " + input);
                    dialog.dismiss();
                }else{
                    SystemUtil.ShowToast(getApplicationContext(),"Please input your name!!!");
                }
            }
        });
    }

    private void showSelfDefineDialog2() {
        final Dialog dialog = new Dialog(this, R.style.time_dialog);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog);//layout of the dialog

        //set the width of dialog be same as window and at the bottom
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);//location -- bottom
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = SystemUtil.GetScreenDM(this).widthPixels;
        window.setAttributes(lp);

        //operations
        Button bCancel = dialog.findViewById(R.id.bCancel);
        Button bConfirm = dialog.findViewById(R.id.bConfirm);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        final EditText etInput = dialog.findViewById(R.id.etInput);
        dialog.show();

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        bConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = etInput.getText().toString();
                if(!TextUtils.isEmpty(input)){
                    SystemUtil.ShowToast(getApplicationContext(),"Your name is " + input);
                    dialog.dismiss();
                }else{
                    SystemUtil.ShowToast(getApplicationContext(),"Please input your name!!!");
                }
            }
        });
    }
}
