package com.example.toollibs.OverWriteClass;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.toollibs.Activity.Events.RefershBookTagEvent;
import com.example.toollibs.R;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class BookView extends View {
    private Paint paint;
    private int viewHeight;
    private int viewWidth;

    //attrs of text
    private float textSize;
    private int textColor;
    private float dividerHeight;//行间距
    private float padValue;//padding
    private int bgColor;//背景颜色

    private int rows;
    private int numInRow;
    private int firstIndex;//the first line to draw

    private String filePath;//the path of book
    private List<String> lines;

    public BookView(Context context) {
        this(context, null);
    }

    public BookView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // 解析自定义属性
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.Book);
        textSize = ta.getDimension(R.styleable.Book_textSize, 18.0f);
        textColor = ta.getColor(R.styleable.Book_textColor, 0xff000000);
        bgColor = ta.getColor(R.styleable.Book_bgColor, 0xffe3edcd);
        dividerHeight = ta.getDimension(R.styleable.Book_dividerHeight, 3.0f);
        padValue = ta.getDimension(R.styleable.Book_padValue, 5.0f);
        ta.recycle();
        // </end>

        // 初始化paint
        paint = new Paint();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setAntiAlias(true);

        //获取view的宽高
        viewWidth = getWidth();
        viewHeight = getHeight();

        //计算行数
        rows = (int) ((viewHeight - padValue*2) / (textSize + dividerHeight));
        numInRow = (int) ((viewWidth - padValue*2) / textSize);

        firstIndex = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //背景
        canvas.drawColor(bgColor);

        for(int i=0; i<rows; i++){
            if(i+firstIndex<lines.size()){
                canvas.drawText(lines.get(i+firstIndex), padValue, padValue+i*(textSize+dividerHeight), paint);
            }
        }
    }

    private float oldX, oldY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        //非UI线程用
        //postInvalidate();

        float x,y;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                oldX = event.getX();
                oldY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //=======================
                break;
            case MotionEvent.ACTION_UP:
                x = event.getX();
                y = event.getY();

                if(y-oldY>0){//下滑
                    firstIndex -= 5;
                    if(firstIndex<=0){
                        firstIndex = 0;
                    }
                }else if(y-oldY<0){//上滑
                    if(firstIndex+rows<=lines.size()){
                        firstIndex += 5;
                    }
                }else{
                    //click
                }

                //这个方法里往往需要重绘界面，使用这个方法可以自动调用onDraw（）方法。（主线程）
                invalidate();
                //save tag
                EventBus.getDefault().post(new RefershBookTagEvent(firstIndex));
                break;
        }

        return true;
    }

    public void seekTo(int position){
        if(position>0 && position<lines.size()){
            firstIndex = position;
        }
    }

    public int getFirstIndex(){
        return firstIndex;
    }

    public void setFilePath(String path){
        if (TextUtils.isEmpty(path)) { return;}
        filePath = path;
        lines.clear();
        parseBook(filePath);
    }

    private void parseBook(String path){
        File file = new File(path);
        if(file.exists()){
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            InputStreamReader inputStreamReader = null;
            try {
                inputStreamReader = new InputStreamReader(inputStream, "utf-8");//'utf-8' 'GBK'
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            try {
                while((line=reader.readLine())!=null){
                    lines.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
