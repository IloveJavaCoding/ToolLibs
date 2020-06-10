package com.example.toollibs.Activity.Demo;

import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.toollibs.OverWriteClass.PosterView;
import com.example.toollibs.R;
import com.example.toollibs.Util.BitmapUtil;
import com.example.toollibs.Util.SystemUtil;

public class Demo_PosterView_Activity extends AppCompatActivity {
    private Button bChange, bDialog;

    private boolean isChanged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_view_);

        init();
        setListener();
    }

    private void init() {
        bChange = findViewById(R.id.bChange);
        bDialog = findViewById(R.id.bDialog);
    }

    private void setListener() {
        bChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isChanged){
                    isChanged = true;
                    //posterView.setIMAGES(BitmapUtil.getBitmapsFromRes(getApplicationContext(), new int[]{R.drawable.img_bg, R.drawable.img_bg2}));
                }
            }
        });

        bDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this, R.style.time_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.poster_view);//layout of the dialog

        //set the width of dialog be same as window and at the bottom
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);//location -- bottom
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = SystemUtil.GetScreenDM(this).widthPixels;
        window.setAttributes(lp);

        //operations
        Button bCancel = dialog.findViewById(R.id.bCancel);
        Button bConfirm = dialog.findViewById(R.id.bConfirm);
        PosterView posterView = dialog.findViewById(R.id.posterView);
        posterView.setIMAGES(BitmapUtil.getBitmapsFromRes(getApplicationContext(), new int[]{R.drawable.img_bg, R.drawable.img_bg2}));

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
                dialog.dismiss();
            }
        });
    }
}
