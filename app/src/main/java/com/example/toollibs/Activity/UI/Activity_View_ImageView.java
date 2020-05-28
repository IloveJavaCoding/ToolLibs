package com.example.toollibs.Activity.UI;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.toollibs.OverWriteClass.EditTextDelIcon;
import com.example.toollibs.R;
import com.example.toollibs.Util.BitmapUtil;
import com.example.toollibs.Util.InternetUtil;
import com.example.toollibs.Util.SystemUtil;

public class Activity_View_ImageView extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView;
    private Button bSearch, bZoomIn, bZoomOut, bRotate, bAlbum, bCamera, bCircle;
    private EditTextDelIcon etInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__view__image_view);

        init();
        setData();
        setListener();
    }

    private void init() {
        imageView = findViewById(R.id.imageView);
        bSearch = findViewById(R.id.bSearch);
        bZoomIn = findViewById(R.id.bZoomIn);
        bZoomOut = findViewById(R.id.bZoomOut);
        bRotate = findViewById(R.id.bRotate);
        bAlbum = findViewById(R.id.bAlbum);
        bCamera = findViewById(R.id.bCamera);
        bCircle = findViewById(R.id.bCircle);

        etInput = findViewById(R.id.delInput);
    }

    private void setData() {
        //set default image
        imageView.setImageBitmap(BitmapUtil.getBitmapFromRes(getApplicationContext(), R.drawable.img_bg));
    }

    private void setListener() {
        imageView.setOnClickListener(this);
        bSearch.setOnClickListener(this);
        bZoomIn.setOnClickListener(this);
        bZoomOut.setOnClickListener(this);
        bRotate.setOnClickListener(this);
        bAlbum.setOnClickListener(this);
        bCamera.setOnClickListener(this);
        bCircle.setOnClickListener(this);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0://no return
                    SystemUtil.ShowToast(getApplicationContext(), "Please check your URL, then try again!");
                    break;
                case 1:
                    imageView.setImageBitmap((Bitmap) msg.obj);
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bSearch:
                getOnlineImage(etInput.getText().toString().trim());
                break;
            case R.id.bZoomIn:

                break;
            case R.id.bZoomOut:

                break;
            case R.id.bRotate:

                break;
            case R.id.bAlbum:

                break;
            case R.id.bCamera:

                break;
            case R.id.bCircle:

                break;
        }
    }

    private void getOnlineImage(final String input){
        new Thread(){
            @Override
            public void run() {
                super.run();

                Bitmap bitmap;
                if(TextUtils.isEmpty(input)){
                    String url = "https://pic.baike.soso.com/ugc/baikepic2/8366/20160729193243-1422261970.jpg/0";
                    bitmap = InternetUtil.GetBitmapFromUrl(url);
                }else{
                    bitmap = InternetUtil.GetBitmapFromUrl(input);
                }

                Message msg = Message.obtain();
                if(bitmap==null){
                    msg.what = 0;
                }else{
                    msg.what = 1;
                }
                msg.obj = bitmap;
                handler.sendMessage(msg);
            }
        }.start();
    }
}
