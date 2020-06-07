package com.example.toollibs.OverWriteClass;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class VerticalSeekBar extends android.support.v7.widget.AppCompatSeekBar {
    private OnVerticalSeekBarChangeListener listener;
    private int process;

    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        Log.i("getHeight()",getHeight()+"");
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
                Log.i("Progress", getProgress() + "");
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
        void onProgressChanged(VerticalSeekBar var1, int var2, boolean var3);

        void onStartTrackingTouch(VerticalSeekBar var1);

        void onStopTrackingTouch(VerticalSeekBar var1);
    }
}
