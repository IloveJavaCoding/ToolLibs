package com.example.toollibs.Activity.ComponentThird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SlideToggleButton extends View {
    private Bitmap toggle_bkg_on;
    private Bitmap toggle_bkg_off;
    private Bitmap toggle_slip;

    private Boolean isChecked = true; //check 的状态
    private Boolean isSliding = false; //滑动状态，false为未滑动状态

    private float currentX; //手指滑动的距离
    private OnisCheckedChangeListener listener;

    public SlideToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    public SlideToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public SlideToggleButton(Context context) {
        super(context);
    }

    /*
     * 繼承View的方法，測量髖高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }

    /*
     * 根据拖动位置绘制图像
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //如果移动距离小于背景一般，绘制关闭按钮背景，否则绘制打开按钮背景
        if (currentX < toggle_bkg_off.getWidth() / 2) {
            canvas.drawBitmap(toggle_bkg_off, 0, 0, null);
        } else {
            canvas.drawBitmap(toggle_bkg_on, 0, 0, null);
        }
        float slip_left;
        if (isSliding) {
            if (currentX > toggle_bkg_off.getWidth() - toggle_slip.getWidth()) {
                slip_left = toggle_bkg_off.getWidth() - toggle_slip.getWidth();
                canvas.drawBitmap(toggle_slip, slip_left, 0, null);
            } else {
                slip_left = currentX - toggle_slip.getWidth() / 2;
                canvas.drawBitmap(toggle_slip, slip_left, 0, null);
            }
        } else {
            if (isChecked) {
                slip_left = toggle_bkg_off.getWidth() - toggle_slip.getWidth();
                canvas.drawBitmap(toggle_slip, slip_left, 0, null);
            } else {
                canvas.drawBitmap(toggle_slip, 0, 0, null);
            }
        }
        //滑出左边界
        if (currentX < 0) {
            canvas.drawBitmap(toggle_slip, 0, 0, null);
        }
        //滑出右边界
        if (currentX > toggle_bkg_off.getWidth() - toggle_slip.getWidth()) {
            slip_left = toggle_bkg_off.getWidth() - toggle_slip.getWidth();
            canvas.drawBitmap(toggle_slip, slip_left, 0, null);
        }
    }

    /*
     * 继承view触摸事件方法并实现逻辑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取滑动距离
        currentX = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //滑动状态改成true
                currentX = event.getX();
                isSliding = true;
                break;
            case MotionEvent.ACTION_MOVE://移动
                currentX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                //滑动状态改成false
                isSliding = false;
                //如果滑动距离大于背景的1/2将开关打开，小于1/2将开关关闭
                if (currentX < toggle_bkg_off.getWidth() / 2) {
                    //检测原来的开关状态是否为开，如果是开则关闭它
                    if (isChecked) {
                        //关闭ToggleButton
                        isChecked = false;
                        //检测是否注册监听器，如果没有则注册
                        if (listener != null) {
                            listener.onisCheckedChange(isChecked);
                        }
                    }
                } else {
                    if (!isChecked) {
                        isChecked = true;
                        //检测是否注册监听器，如果没有则注册
                        if (listener != null) {
                            listener.onisCheckedChange(isChecked);
                        }
                    }
                }
                break;
        }
        //在onTouchEvent事件完成后重新调用onDraw绘制图形
        invalidate();
        //返回true表示操作完毕
        return true;
    }

    /**
     * 定义一个ToggleButton状态改变监听器接口
     */
    public interface OnisCheckedChangeListener {
        void onisCheckedChange(Boolean state);
    }

    public void setOnisCheckedChangeListener(OnisCheckedChangeListener listener) {
        this.listener = listener;
    }

    public void setToggle_bkg_on(Bitmap toggle_bkg_on) {
        this.toggle_bkg_on = toggle_bkg_on;
    }

    public void setToggle_bkg_off(Bitmap toggle_bkg_off) {
        this.toggle_bkg_off = toggle_bkg_off;
    }

    public void setToggle_slip(Bitmap toggle_slip) {
        this.toggle_slip = toggle_slip;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }
}