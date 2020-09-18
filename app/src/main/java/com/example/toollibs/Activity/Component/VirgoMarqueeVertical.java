package com.example.toollibs.Activity.Component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.OverScroller;

import java.util.ArrayList;
import java.util.List;

public class VirgoMarqueeVertical extends View implements Runnable{
    private final String TAG = "MARQUEE_VERTICAL";

    private Paint paint;
    private int viewWidth;
    private int viewHeight;

    //字体属性
    private float textSize = 20.0f;
    private int textColor = Color.YELLOW;

    private String contents;
    private List<String> list = new ArrayList<>();

    private int backgroundColor = Color.BLACK;//默认背景色
    private int rows;//文本分割后总行数
    private int numInRow;//每行最多容纳字数
    private int padValue = 15;//左右两边缩进距离 piex
    private int offset=0;//记录画布滚动距离

    private int delay = 3000;//滚动间隔
    private Thread thread;//控制自动滚动
    private OverScroller scroller;//使滚动效果跟平滑
    
    public VirgoMarqueeVertical(Context context) {
        this(context, null);
    }

    public VirgoMarqueeVertical(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VirgoMarqueeVertical(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //解析自定义属性...未加

        //初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(textColor);
        paint.setTextSize(sp2px(textSize));//

        scroller = new OverScroller(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        viewWidth = getWidth();
        viewHeight = getHeight();
        Log.i(TAG, "w&h: " + viewWidth + ", " +viewHeight);

        calculateRows();
        parseContent();
        startRoll();
        super.onLayout(changed, left, top, right, bottom);
    }

    private void calculateRows() {
        numInRow = (int) ((viewWidth-padValue*2) / textSize);
        rows = contents.length()%numInRow==0 ? contents.length()/numInRow : contents.length()/numInRow+1;
    }

    private void parseContent(){
        for(int i=0; i<rows; i++){
            String temp = contents.substring(numInRow*i, Math.min(numInRow*(i+1), contents.length()));
            list.add(temp);
            Log.i(TAG, "line: " + temp);
        }
    }

    private void startRoll() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(backgroundColor);

        //计算绘制文本中心，然后将其移至控件中心
        Paint.FontMetricsInt metricsInt = paint.getFontMetricsInt();
        float centerY = (viewHeight - metricsInt.top - metricsInt.bottom) / 2.0f;

        for(int i=0; i<list.size(); i++){
            canvas.drawText(list.get(i), padValue, centerY + viewHeight*i, paint);//每行相隔距离为一个控件高度
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public void run() {
        if(list.size()>1){
            for(;;){
                Log.i(TAG, "offset: " + offset);
                if(offset>=viewHeight*(list.size()-1)){
                    scrollTo(0,0);
                    offset = -viewHeight;
                }
                try {
                    Thread.sleep(delay);
                    scroller.startScroll(0, offset, 0, viewHeight);
                    offset += viewHeight;
                    postInvalidate();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setTextSize(float textSize) {
        this.textSize = sp2px(textSize);
        paint.setTextSize(sp2px(textSize));
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

    private int sp2px(float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, getResources().getDisplayMetrics());
    }
}
