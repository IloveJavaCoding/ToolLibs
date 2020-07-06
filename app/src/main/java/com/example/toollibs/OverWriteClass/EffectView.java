package com.example.toollibs.OverWriteClass;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.example.toollibs.OverWriteClass.Drawable.AncientEffectDrawable;
import com.example.toollibs.OverWriteClass.Drawable.BaseEffectDrawable;
import com.example.toollibs.OverWriteClass.Drawable.ElectronicEffectDrawable;
import com.example.toollibs.OverWriteClass.Drawable.LonelyEffectDrawable;
import com.example.toollibs.OverWriteClass.Drawable.SurroundEffectDrawable;

public class EffectView extends android.support.v7.widget.AppCompatImageView {
    private int mPaintColor = Color.parseColor("#CABFA3");
    private int[] mPaintColors;

    public EffectView(Context context) {
        super(context);
        init();
    }

    public EffectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EffectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }

    private BaseEffectDrawable mDrawable;

    private void initDrawable(BaseEffectDrawable drawable) {
        mDrawable = drawable;
        setImageDrawable(mDrawable);
        setColor();
    }

    //古风
    public void setAncientEffectDrawable() {
        initDrawable(new AncientEffectDrawable(getContext()));
    }

    //电音
    public void setElectronicEffectDrawable() {
        initDrawable(new ElectronicEffectDrawable(getContext()));
    }

    //环绕
    public void setSurroundEffectDrawable() {
        initDrawable(new SurroundEffectDrawable(getContext()));
    }

    //孤独
    public void setLonelyEffectDrawable() {
        initDrawable(new LonelyEffectDrawable(getContext()));
    }

    public void setColor() {
        if (mDrawable != null) {
            mDrawable.setColor(mPaintColor);
        }
    }

    public void setColor(int color) {
        mPaintColor = color;
        if (mDrawable != null) {
            mDrawable.setColor(color);
        }
    }
    public void setColors(int[] colors) {
        mPaintColors = colors;
        if (mDrawable != null) {
            mDrawable.setColors(colors);
        }
    }

    public void onCall(final byte[] data) {
        if (mDrawable != null) {
            mDrawable.onCall(data);
        }
    }

    public void onWaveCall(byte[] data) {
        if (mDrawable != null) {
            mDrawable.onWaveCall(data);
        }
    }
}
