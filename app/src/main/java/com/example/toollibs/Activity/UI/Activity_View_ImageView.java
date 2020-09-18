package com.example.toollibs.Activity.UI;

import android.Manifest;
import android.animation.ObjectAnimator;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.toollibs.Activity.Component.VirgoDelIconEditText;
import com.example.toollibs.R;
import com.example.toollibs.Util.BitmapUtil;
import com.example.toollibs.Util.FileUtil;
import com.example.toollibs.Util.InternetUtil;
import com.example.toollibs.Util.SystemUtil;

import java.io.File;
import java.io.FileNotFoundException;

public class Activity_View_ImageView extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView;
    private Button bSearch, bZoomIn, bZoomOut, bRotate, bAlbum, bCamera, bCircle, bAnimator;
    private VirgoDelIconEditText etInput;

    private Bitmap curBitmap;
    private Uri imageUris;
    private ObjectAnimator animator;

    private final int DURATION = 5000;
    private static final int GO_ALBUM_CODE = 101;
    private static final int GO_CAMERA_CODE = 102;
    private static final int GO_CROP_CODE = 103;
    private static final int OPEN_URL_FAIL = 0;
    private static final int OPEN_URL_SUCCESS = 1;
    private static final int ACTION_REQUEST_PERMISSIONS = 0x003;

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
        bAnimator = findViewById(R.id.bAnimator);

        etInput = findViewById(R.id.delInput);
    }

    private void setData() {
        //set default image
        //imageView.setImageResource(R.drawable.img_bg);
        //imageView.setImageDrawable(getResources().getDrawable(R.drawable.img_bg));
        curBitmap = BitmapUtil.getBitmapFromRes(getApplicationContext(), R.drawable.img_bg2);
        imageView.setImageBitmap(curBitmap);

        animator = BitmapUtil.rotateIV(imageView, DURATION);
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
        bAnimator.setOnClickListener(this);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case OPEN_URL_FAIL://no return
                    SystemUtil.showToast(getApplicationContext(), "Please check your URL, then try again!");
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
                if(curBitmap.getWidth()<51){
                    SystemUtil.showToast(getApplicationContext(),"Can not be smaller!");
                }else{
                    curBitmap = BitmapUtil.scaleBitmap(curBitmap, 0.75f);
                    imageView.setImageBitmap(curBitmap);
                }
                break;
            case R.id.bZoomOut:
                if(curBitmap.getWidth()>1001){
                    SystemUtil.showToast(getApplicationContext(),"Can not be bigger!");
                }else{
                    curBitmap = BitmapUtil.scaleBitmap(curBitmap, 1.25f);
                    imageView.setImageBitmap(curBitmap);
                }
                break;
            case R.id.bRotate:
                //??? the size will be changed while rotating
                curBitmap = BitmapUtil.rotateBitmap(curBitmap, 60);
                Log.d("Tag", "宽度: ..." + curBitmap.getWidth());
                imageView.setImageBitmap(curBitmap);
                break;
            case R.id.bAlbum:
                openAlbum();
                break;
            case R.id.bCamera:
                if(!SystemUtil.checkPermission(this, NEEDED_PERMISSIONS)){
                    Log.d("Tag", "提示是否要授权");
                    ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
                    return;
                }else{
                    openCamera();
                }
                break;
            case R.id.bCircle:
                curBitmap = BitmapUtil.getCircleBitmap(curBitmap);
                imageView.setImageBitmap(curBitmap);
                break;
            case R.id.bAnimator:
                if(animator.isStarted()){
                    if(animator.isPaused()){
                        animator.resume();
                        Log.d("Tag", "resume");
                    }else{
                        animator.pause();
                        Log.d("Tag", "paused");
                    }
                }else{
                    animator.start();
                    Log.d("Tag", "animator start");
                }
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
                    bitmap = InternetUtil.getBitmapFromUrl(url);
                }else{
                    bitmap = InternetUtil.getBitmapFromUrl(input);
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
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Log.d("Tag", "try to open camera");
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent,GO_CAMERA_CODE);
            Log.d("Tag", "jump to camera");
        } else {
            SystemUtil.showToast(this, "没有SD卡");
        }
    }

    private void imageZoom(Uri uri) {
        imageUris = getTempUri();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 3);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUris);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent,GO_CROP_CODE);
    }

    protected Uri getTempUri() {
        File file = getExternalFilesDir(FileUtil.getAppRootPth(getApplicationContext()));
        File f = new File(file, "temp.jpg");
        return Uri.fromFile(f);
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
            Uri uri = data.getData();
            if (uri != null) {
                imageZoom(uri);
            }else{
                Bundle bundle = data.getExtras();
                curBitmap = (Bitmap)bundle.get("data");
                imageView.setImageBitmap(curBitmap);
            }
        }else if(requestCode==GO_CROP_CODE){
            imageView.setImageURI(imageUris);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isAllGranted = true;
        for (int grantResult : grantResults) {
            isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
        }

        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            if (isAllGranted) {
                //get all requested permissions

            } else {
                SystemUtil.showToast(getApplicationContext(),"Permission denied!");
                finish();
            }
        }
    }
}
