package com.example.toollibs.OverWriteClass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.toollibs.Util.ConvertUtil;

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
    private float textSize = 20.0f;
    private int textColor = Color.YELLOW;

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
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        rect = new Rect();
        //初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(textColor);
        paint.setTextSize(ConvertUtil.dip2px(context, textSize));
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
        parseContent();
        startRoll();
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
            Log.i(TAG, "draw: ");
            canvas.drawText(list.get(i), 0, centerY + divideHeight*i, paint);
        }
    }

    @Override
    public void run() {
        Log.i(TAG, "scrolled Y: " + getScrollY());
        if(list.size()>1){
            if(getScrollY()>viewHeight*list.size()){
                scrollTo(0,0);
            }
            try {
                Thread.sleep(3000);
                scrollBy(0, (int) divideHeight);
                postInvalidate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseContent(){
        for(int i=0; i<rows; i++){
            String temp = contents.substring(numInRow*i, Math.min(numInRow*(i+1), contents.length()));
            list.add(temp);
            Log.i(TAG, "line: " + temp);
        }
    }

    public void setTextSize(float textSize) {
        this.textSize = ConvertUtil.dip2px(context, textSize);
        paint.setTextSize(this.textSize);
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        paint.setColor(textColor);
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
