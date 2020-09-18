package com.example.toollibs.Util.Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

public class GlideImageHelper extends ImageLoader {
    private int type;

    public GlideImageHelper(int type){
        this.type = type;
    }
    @Override
    public void displayImage(Context context, Object obj, ImageView imageView) {
        switch (type){
            case 1:
                //1. load web image:url
                Glide.with(context).load(obj).into(imageView);
                break;
            case 2:
                //2. load local image:bitmap
                imageView.setImageBitmap((Bitmap) obj);
                break;
            default:
                //
                break;
        }
    }
}
