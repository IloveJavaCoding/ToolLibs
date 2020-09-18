package com.example.toollibs.Activity.Component;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class VirgoVerticalSeekBar extends android.support.v7.widget.AppCompatSeekBar {
    private static final String TAG = "VirgoVerticalSeekBar";

    private OnVerticalSeekBarChangeListener listener;
    private int process;

    public VirgoVerticalSeekBar(Context context) {
        this(context, null);
    }

    public VirgoVerticalSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VirgoVerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnVerticalSeekBarChangeListener(OnVerticalSeekBarChangeListener l){
        listener = l;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onDraw(Canvas c) {
        //将SeekBar转转90度
        c.rotate(-90);
        //将旋转后的视图移动回来
        c.translate(-getHeight(),0);
        super.onDraw(c);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onStartTrackingTouch();
            case MotionEvent.ACTION_MOVE:
                //获取滑动的距离
                process = getMax() - (int) (getMax() * event.getY() / getHeight());
                //设置进度
                setProgress(process);
                Log.d(TAG , "Process: " + getProgress());
                //每次拖动SeekBar都会调用
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                onProgressChanged();
            case MotionEvent.ACTION_UP:
                onStopTrackingTouch();
                break;

            case MotionEvent.ACTION_CANCEL:
                onStopTrackingTouch();
                break;
        }
        return true;
    }

    void onProgressChanged() {
        if(listener!=null){
            listener.onProgressChanged(this,process,true);
        }
    }

    void onStartTrackingTouch() {
        if(listener!=null){
            listener.onStartTrackingTouch(this);
        }
    }

    void onStopTrackingTouch() {
        if(listener!=null){
            listener.onStopTrackingTouch(this);
        }
    }

    public interface OnVerticalSeekBarChangeListener {
        void onProgressChanged(VirgoVerticalSeekBar var1, int var2, boolean var3);

        void onStartTrackingTouch(VirgoVerticalSeekBar var1);

        void onStopTrackingTouch(VirgoVerticalSeekBar var1);
    }
}
