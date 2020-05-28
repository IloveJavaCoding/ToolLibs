package com.example.toollibs.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.toollibs.R;
import com.example.toollibs.Util.SystemUtil;

public class Dialog_Activity extends AppCompatActivity implements View.OnClickListener{
    private Button bDialog1, bDialog2, bDialog3, bDialog4;
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
    }

    private void setListener() {
        bDialog1.setOnClickListener(this);
        bDialog2.setOnClickListener(this);
        bDialog3.setOnClickListener(this);
        bDialog4.setOnClickListener(this);
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
        }
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
