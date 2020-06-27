package com.example.toollibs.Activity.Demo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.toollibs.OverWriteClass.GlideImageLoader;
import com.example.toollibs.R;
import com.example.toollibs.Util.BitmapUtil;
import com.example.toollibs.Util.SystemUtil;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class Demo_PosterView_Activity extends AppCompatActivity {
    private Banner banner1, banner2;

    private List<String> imageUrl;
    private List<Bitmap> imageBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_view_);

        init();
        setDate();
        setListener();
    }

    private void init() {
        banner1 = findViewById(R.id.bannerWeb);
        banner2 = findViewById(R.id.bannerLocal);
    }

    private void setDate() {
        imageUrl = new ArrayList<>();
        imageUrl.add("https://images4.alphacoders.com/651/651952.jpg");
        imageUrl.add("https://images7.alphacoders.com/973/973119.jpg");
        imageUrl.add("https://images6.alphacoders.com/947/947849.jpg");

        imageBitmap = new ArrayList<>();
        imageBitmap.add(BitmapUtil.getBitmapFromRes(this, R.drawable.img_bg));
        imageBitmap.add(BitmapUtil.getBitmapFromRes(this, R.drawable.img_bg2));
        imageBitmap.add(BitmapUtil.getBitmapFromRes(this, R.drawable.img_bg3));

        banner1.setImageLoader(new GlideImageLoader(1));
        banner1.setImages(imageUrl);
        banner1.setDelayTime(5000);//default 5seconds
        banner1.start();

        banner2.setImageLoader(new GlideImageLoader(2));
        banner2.setImages(imageBitmap);
        banner2.setDelayTime(3000);//default 5seconds
        banner2.start();
    }

    private void setListener() {
        banner1.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                SystemUtil.ShowToast(getApplicationContext(), "click " + position);
            }
        });

        banner2.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                SystemUtil.ShowToast(getApplicationContext(), "click " + position);
            }
        });
    }
}