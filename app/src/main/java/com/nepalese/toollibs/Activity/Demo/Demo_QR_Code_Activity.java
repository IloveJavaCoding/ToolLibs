package com.nepalese.toollibs.Activity.Demo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nepalese.toollibs.R;
import com.nepalese.toollibs.Util.BitmapUtil;
import com.nepalese.toollibs.Util.Helper.QRGenerateHelper;

public class Demo_QR_Code_Activity extends AppCompatActivity {
    private ImageView imgQR;
    private EditText input;
    private Button bQR1, bQr2;

    private String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo__q_r__code_);

        init();
        setData();
        setListener();
    }

    private void init() {
        imgQR = findViewById(R.id.imgQR);
        input = findViewById(R.id.etInput);
        bQR1 = findViewById(R.id.bQR1);
        bQr2 = findViewById(R.id.bQR2);
    }

    private void setData() {
        content = "Your are so beautiful!";
    }

    private void setListener() {
        bQR1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = input.getText().toString().trim();
                Bitmap bitmap = QRGenerateHelper.createQRImage(content, 200, 200, null);
                if(bitmap!=null){
                    imgQR.setImageBitmap(bitmap);
                }
            }
        });

        bQr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = input.getText().toString().trim();
                Bitmap bitmap = QRGenerateHelper.createQRImage(content, 200, 200, BitmapUtil.getBitmapFromRes(getApplicationContext(), R.mipmap.ic_launcher));
                if(bitmap!=null){
                    imgQR.setImageBitmap(bitmap);
                }
            }
        });
    }
}