package com.example.toollibs.Activity.UI;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.solver.GoalRow;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.toollibs.OverWriteClass.EditTextDelIcon;
import com.example.toollibs.R;
import com.example.toollibs.Util.BitmapUtil;
import com.example.toollibs.Util.InternetUtil;
import com.example.toollibs.Util.SystemUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Activity_View_ImageView extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView;
    private Button bSearch, bZoomIn, bZoomOut, bRotate, bAlbum, bCamera, bCircle;
    private EditTextDelIcon etInput;
    private Bitmap curBitmap;

    private static final int GO_ALBUM_CODE = 101;
    private static final int GO_CAMERA_CODE = 102;
    private static final int OPEN_URL_FAIL = 0;
    private static final int OPEN_URL_SUCCESS = 1;

    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
    };

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
        //imageView.setImageResource(R.drawable.img_bg);
        //imageView.setImageDrawable(getResources().getDrawable(R.drawable.img_bg));
        curBitmap = BitmapUtil.getBitmapFromRes(getApplicationContext(), R.drawable.img_bg);
        imageView.setImageBitmap(curBitmap);
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
                case OPEN_URL_FAIL://no return
                    SystemUtil.ShowToast(getApplicationContext(), "Please check your URL, then try again!");
                    break;
                case OPEN_URL_SUCCESS:
                    curBitmap = (Bitmap) msg.obj;
                    imageView.setImageBitmap(curBitmap);
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
                curBitmap = BitmapUtil.RotateBitmap(curBitmap, 60, false);
                imageView.setImageBitmap(curBitmap);
                break;
            case R.id.bAlbum:
                openAlbum();
                break;
            case R.id.bCamera:
                if(verifyPermissions(this,NEEDED_PERMISSIONS[2])){
                    Log.d("Tag", "提示是否要授权");
                    ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, 3);
                }else{
                   openCamera();
                }
                break;
            case R.id.bCircle:
                curBitmap = BitmapUtil.GetCircleBitmap(curBitmap);
                imageView.setImageBitmap(curBitmap);
                break;
        }
    }

    private void getOnlineImage(final String input){
        //must be in a sub thread to open a url
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
                Log.d("Tag", "open url: "+ input);
                Message msg = Message.obtain();
                if(bitmap==null){
                    msg.what = OPEN_URL_FAIL;
                }else{
                    msg.what = OPEN_URL_SUCCESS;
                }
                msg.obj = bitmap;
                handler.sendMessage(msg);
            }
        }.start();
    }

    private void openAlbum(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GO_ALBUM_CODE);
        Log.d("Tag", "jump to album");
    }

    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  //跳转到 ACTION_IMAGE_CAPTURE
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"fileImg.jpg")));
        startActivityForResult(intent,GO_CAMERA_CODE);
        Log.d("Tag", "jump to camera");
    }

    public boolean verifyPermissions(Activity activity, java.lang.String permission) {
        int Permission = ActivityCompat.checkSelfPermission(activity,permission);
        if (Permission == PackageManager.PERMISSION_GRANTED) {
            Log.d("Tag","已经同意权限");
            return true;
        }else{
            Log.d("Tag","没有同意权限");
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //album
        if(requestCode==GO_ALBUM_CODE && resultCode==RESULT_OK){
            Uri uri = data.getData();
            ContentResolver contentResolver = this.getContentResolver();
            try {
                curBitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                imageView.setImageBitmap(curBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else if (requestCode==GO_CAMERA_CODE && resultCode==RESULT_OK){
            try {
                Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"fileImg.jpg"));
                curBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                imageView.setImageBitmap(curBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
