package com.example.toollibs.OverWriteClass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class MarqueeHorizontal extends View implements Runnable {
    private final String TAG = "MARQUEE_HORIZONTAL";
    private Context context;

    private Paint paint;
    private int viewWidth;
    private int viewHeight;

    private Rect rect;//用于获取文字宽度
    //字体属性
    private float textSize;
    private int textColor;
    private int speed;
    private float textWidth;

    private String contents;
    private int backgroundColor = Color.BLACK;

    private Thread thread;

    public MarqueeHorizontal(Context context) {
        this(context, null);
    }

    public MarqueeHorizontal(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarqueeHorizontal(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        startRoll();
    }

    private void init(AttributeSet attrs) {
        //解析自定义属性

        rect = new Rect();
        //初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
    }

    private void startRoll() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        viewWidth = getWidth();
        viewHeight = getHeight();

        adjustText();
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(backgroundColor);

        float centerY = (viewHeight - textSize) / 2.0f;
        canvas.drawText(contents, viewWidth, centerY, paint);
    }

    private void adjustText() {
        textWidth = getTextWidth(contents);
        if(textWidth < viewWidth){
            String blank = getBlanks(contents.length());
            int times = (int) (viewWidth / textWidth) + 1;
            String newCont = "";
            if(times%2==0){
                for(int i=0; i<times/2; i++){
                    newCont += (contents + blank);
                }
            }else{
                for(int i=0; i<times/2; i++){
                    newCont += (contents + blank);
                }
                newCont += contents;
            }
            contents = newCont;
        }
    }

    private String getBlanks(int length) {
        String temp = "\u3000";
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<length; i++){
            builder.append(temp);
        }

        return builder.toString();
    }

    private float getTextWidth(String str){
        if (str == null || str == "") {
            return 0;
        }
        if (rect == null) {
            rect = new Rect();
        }
        paint.getTextBounds(str, 0, str.length(), rect);

        return rect.width();
    }

    @Override
    public void run() {
        while (!TextUtils.isEmpty(contents)) {
            if(getScrollX()>viewWidth+getTextWidth(contents)){
                scrollTo(0,0);
            }
            try {
                Thread.sleep(20);
                scrollBy(speed, 0);
//                postInvalidate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
