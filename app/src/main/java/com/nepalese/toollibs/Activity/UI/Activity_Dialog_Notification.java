package com.nepalese.toollibs.Activity.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TimePicker;

import com.nepalese.toollibs.Activity.Component.VirgoDelIconEditText;
import com.nepalese.toollibs.R;
import com.nepalese.toollibs.Util.DialogUtil;
import com.nepalese.toollibs.Util.NotificationUtil;
import com.nepalese.toollibs.Util.ScreenUtil;
import com.nepalese.toollibs.Util.SystemUtil;

public class Activity_Dialog_Notification extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "Activity_Dialog_Notific";
    private Button bDialog1, bDialog2, bDialog3, bDialog4, bDialog5;
    private Button bNotification;
    private VirgoDelIconEditText etInput;

    private int hour,min;
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
        bDialog5 = findViewById(R.id.bDialog5);

        etInput = findViewById(R.id.etDel);
        bNotification = findViewById(R.id.bNotification);
    }

    private void setListener() {
        bDialog1.setOnClickListener(this);
        bDialog2.setOnClickListener(this);
        bDialog3.setOnClickListener(this);
        bDialog4.setOnClickListener(this);
        bDialog5.setOnClickListener(this);
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
            case R.id.bDialog5:
                pickTime();
                break;
            case R.id.bNotification:
                String msg = etInput.getText().toString();
                callThread(5, msg==null?"msg":msg);
                break;
        }
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
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.baidu.com"));
                NotificationUtil.sendNotification(getApplicationContext(), "channel0", intent, "Title", msg, 0);
                super.run();
            }
        }.start();
    }

    private void showDefaultDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SystemUtil.showToast(getApplicationContext(),"Confirm");
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
                SystemUtil.showToast(getApplicationContext(),"Confirm");
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
                    SystemUtil.showToast(getApplicationContext(),"Your name is " + input);
                    dialog.dismiss();
                }else{
                    SystemUtil.showToast(getApplicationContext(),"Please input your name!!!");
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
        lp.width = ScreenUtil.getScreenDM(this).widthPixels;
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
                    SystemUtil.showToast(getApplicationContext(),"Your name is " + input);
                    dialog.dismiss();
                }else{
                    SystemUtil.showToast(getApplicationContext(),"Please input your name!!!");
                }
            }
        });
    }

    private void pickTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                hour = i;
                min = i1;
                SystemUtil.showToast(getApplicationContext(), "hour: " + hour + "\tmin: " + min);
            }
        },0,0,true);
        timePickerDialog.show();
    }
}