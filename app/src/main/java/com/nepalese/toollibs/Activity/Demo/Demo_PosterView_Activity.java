package com.nepalese.toollibs.Activity.Demo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nepalese.toollibs.Util.Helper.GlideImageHelper;
import com.nepalese.toollibs.Activity.Component.VirgoImageView;
import com.nepalese.toollibs.R;
import com.nepalese.toollibs.Util.BitmapUtil;
import com.nepalese.toollibs.Util.Helper.VirgoImageHelper;
import com.nepalese.toollibs.Util.SystemUtil;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class Demo_PosterView_Activity extends AppCompatActivity {
    private Banner banner1, banner2;
    private VirgoImageView imageView;

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
        imageView = findViewById(R.id.myImageView);
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

        banner1.setImageLoader(new GlideImageHelper(1));
        banner1.setImages(imageUrl);
        banner1.setDelayTime(5000);//default 5seconds
        banner1.start();

        banner2.setImageLoader(new GlideImageHelper(2));
        banner2.setImages(imageBitmap);
        banner2.setDelayTime(3000);//default 5seconds
        banner2.start();

        VirgoImageHelper.setImgUri(imageView, imageUrl, 0);
        VirgoImageHelper.setAnimation(imageView, VirgoImageHelper.ANIM_TYPE_FADE_IN, VirgoImageHelper.ANIM_TYPE_LEFT_OUT);
    }

    private void setListener() {
        banner1.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                SystemUtil.showToast(getApplicationContext(), "click " + position);
            }
        });

        banner2.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                SystemUtil.showToast(getApplicationContext(), "click " + position);
            }
        });
    }
}