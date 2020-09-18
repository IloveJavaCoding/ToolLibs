package com.example.toollibs.Activity.SelfClass;

import android.view.MotionEvent;
import android.view.View;

public class VirgoOnDoubleClickListener implements View.OnTouchListener{
    private int count = 0;//点击次数
    private long firstClick = 0;//第一次点击时间
    private long secondClick = 0;//第二次点击时间
    private final int duration = 500;//预设两次点击最多间隔时间

    private Callback callback;

    public interface Callback{
        void onDoubleClick();
    }

    public VirgoOnDoubleClickListener(Callback callback) {
        this.callback = callback;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            count ++;
            if (count == 1){
                firstClick = System.currentTimeMillis();
            }else if (count == 2){
                secondClick = System.currentTimeMillis();
                if (secondClick - firstClick<=duration){
                    if (callback!=null){
                        callback.onDoubleClick();//调用重写方法
                    }
                    count = 0;
                    firstClick = 0;
                    return true;
                }else {
                    //间隔时间超过设定值，重新计算
                    firstClick = secondClick;
                    count = 1;
                }
                secondClick = 0;
            }
        }
        //最后要返回false 否则单击事件会被屏蔽掉
        return false;
    }
}
