package com.nepalese.toollibs.Activity.Component;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @author nepalese on 2020/12/3 11:03
 * @usage
 */
public class VerticalSeekBar extends android.support.v7.widget.AppCompatSeekBar {
    private static final String TAG = "VerticalSeekBar";

    public VerticalSeekBar(Context context) {
        super(context);
    }

    private int process;//进度
    private OnVerticalSeekBarChangeListener listener;//回调监听

    public void setOnVerticalSeekBarChangeListener(OnVerticalSeekBarChangeListener verticalSeekBarChangeListener){
        listener = verticalSeekBarChangeListener;
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onDraw(Canvas canvas) {
        //将SeekBar转转90度
        canvas.rotate(-90);
        //将旋转后的视图移动回来
        canvas.translate(-getHeight(),0);
        super.onDraw(canvas);
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
        void onProgressChanged(VerticalSeekBar verticalSeekBar, int process, boolean fromUser);

        void onStartTrackingTouch(VerticalSeekBar verticalSeekBar);

        void onStopTrackingTouch(VerticalSeekBar verticalSeekBar);
    }
}
