package com.nepalese.toollibs.Activity.Demo;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nepalese.toollibs.R;

import java.io.FileNotFoundException;

public class Demo_Marge2Bitmap_Activity extends AppCompatActivity {
    private static final String TAG = "MargeTwoBitmapActivity";

    private static final int REQUEST_CODE_1 = 1;
    private static final int REQUEST_CODE_2 = 2;

    private ImageView imgSec1, imgSrc2, imgMerge;
    private TextView tvSize1, tvSize2;;
    private Button bMarge, bC1, bC2;

    private Bitmap bitmap1, bitmap2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merg_two_bitmap);

        init();
        setListener();
    }

    private void init() {
        imgSec1 = findViewById(R.id.imgSrc1);
        imgSrc2 = findViewById(R.id.imgSrc2);
        imgMerge = findViewById(R.id.imgMarge);

        tvSize1 = findViewById(R.id.tvSizeSec1);
        tvSize2 = findViewById(R.id.tvSizeSec2);

        bMarge = findViewById(R.id.bMarge);
        bC1 = findViewById(R.id.bChoose1);
        bC2 = findViewById(R.id.bChoose2);
    }

    private void setListener() {
        bC1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               chooseImage(REQUEST_CODE_1);
            }
        });

        bC2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(REQUEST_CODE_2);
            }
        });

        bMarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap1!=null && bitmap2!=null){
                    imgMerge.setImageBitmap(marge(bitmap1, bitmap2));
                }
            }
        });
    }

    private void chooseImage(int requestCode){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, requestCode);
    }

    private Bitmap marge(Bitmap src1, Bitmap src2) {
        //获取图片的宽高
        int src1Width = src1.getWidth();
        int src1Height = src1.getHeight();
        int src2Width = src2.getWidth();
        int src2Height = src2.getHeight();

        int aimWidth = Math.max(src1Width, src2Width);
        int aimHeight = src1Height + src2Height;

//        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;

        Bitmap bitmap = Bitmap.createBitmap(aimWidth, aimHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src1, 0, 0, null);
//            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2f, srcHeight / 2f);
            canvas.drawBitmap(src2, 0, src1Height, null);

            canvas.save();
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //album
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            ContentResolver contentResolver = this.getContentResolver();
            if (requestCode == REQUEST_CODE_1) {
                try {
                    bitmap1 = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                    imgSec1.setImageBitmap(bitmap1);
                    tvSize1.setText(bitmap1.getWidth() + " * " + bitmap1.getHeight());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CODE_2) {
                try {
                    bitmap2 = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                    imgSrc2.setImageBitmap(bitmap2);
                    tvSize2.setText(bitmap2.getWidth() + " * " + bitmap2.getHeight());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bitmap1.recycle();
        bitmap2.recycle();
    }
}