package com.example.toollibs.OverWriteClass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MarqueeVertical extends View implements Runnable{
    private final String TAG = "MARQUEE_VERTICAL";
    private Context context;

    private Rect rect;
    private Paint paint;
    private int viewWidth;
    private int viewHeight;

    //字体属性
    private float textSize;
    private int textColor;

    private String contents;
    private List<String> list = new ArrayList<>();

    private int backgroundColor = Color.BLACK;
    private int rows;
    private int numInRow;//每行最多容纳字数
    private float divideHeight;

    private Thread thread;
    
    public MarqueeVertical(Context context) {
        this(context, null);
    }

    public MarqueeVertical(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarqueeVertical(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        startRoll();
    }

    private void init(AttributeSet attrs) {
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
        
        divideHeight = viewHeight - textSize;
        calculateRows();
        super.onLayout(changed, left, top, right, bottom);
    }

    private void calculateRows() {
        numInRow = (int) (viewWidth / textSize);
        rows = contents.length()%numInRow==0 ? contents.length()/numInRow : contents.length()/numInRow+1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(backgroundColor);

        float centerY = (viewHeight - textSize) / 2.0f;
        for(int i=0; i<list.size(); i++){
            canvas.drawText(list.get(i), 0, centerY + divideHeight*i, paint);
        }
    }

    @Override
    public void run() {
        if(list.size()>1){
            if(getScrollY()>viewHeight*list.size()-1){
                scrollTo(0,0);
            }
            try {
                Thread.sleep(3000);
                scrollBy(0, (int) divideHeight);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseContent(){
        for(int i=0; i<rows; i++){
            String temp = contents.substring(numInRow*i, Math.min(numInRow*(i+1), contents.length()));
            list.add(temp);
        }
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

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
