package com.example.toollibs.OverWriteClass;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.LongDef;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.List;

public class BookView extends View {
    private final String TAG = "BOOK_VIEW";
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

    private List<String> lines = new ArrayList<>();
    private String contents;

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
        textSize = ta.getDimension(R.styleable.Book_bookTextSize, 18.0f);
        textColor = ta.getColor(R.styleable.Book_bookTextColor, 0xff000000);
        bgColor = ta.getColor(R.styleable.Book_bgColor, 0xffe3edcd);
        dividerHeight = ta.getDimension(R.styleable.Book_bookDividerHeight, 5.0f);
        padValue = ta.getDimension(R.styleable.Book_padValue, 10.0f);
        ta.recycle();
        // </end>

        // 初始化paint
        paint = new Paint();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setAntiAlias(true);

        firstIndex = 0;
    }

    public void getWindowWH(int w, int h){
        viewHeight = h;
        viewWidth = w;;
    }

    /**
     * case MeasureSpec.AT_MOST:  // 子容器可以是声明大小内的任意大小
     * 			Log.e(Tag, "子容器可以是声明大小内的任意大小");
     * 			Log.e(Tag, "大小为:"+specSize);
     * 			result=specSize;
     * 			break;
     * 		case MeasureSpec.EXACTLY: //父容器已经为子容器设置了尺寸,子容器应当服从这些边界,不论子容器想要多大的空间.  比如EditTextView中的DrawLeft
     * 			Log.e(Tag, "父容器已经为子容器设置了尺寸,子容器应当服从这些边界,不论子容器想要多大的空间");
     * 			Log.e(Tag, "大小为:"+specSize);
     * 			result=specSize;
     * 			break;
     * 		case MeasureSpec.UNSPECIFIED:  //父容器对于子容器没有任何限制,子容器想要多大就多大. 所以完全取决于子view的大小
     * 			Log.e(Tag, "父容器对于子容器没有任何限制,子容器想要多大就多大");
     * 			Log.e(Tag, "大小为:"+specSize);
     * 			result=1500;
     * 			break;
     * 		default:
     * 			break;
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getPaddingLeft() + getPaddingRight();
        int height = getPaddingTop() + getPaddingBottom();
        width = Math.max(width, getSuggestedMinimumWidth());
        height = Math.max(height, getSuggestedMinimumHeight());

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(1080, MeasureSpec.AT_MOST);
        setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, 0),
                resolveSizeAndState(height, heightMeasureSpec, 0));


//        viewHeight = getMeasuredHeight();//992
//        viewWidth =(int) ((1080f/1920f)*viewHeight);
        Log.d(TAG, viewWidth + " " + viewHeight);

        calculateRows();
        parseLines(contents);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void calculateRows(){
        //计算行数
        rows = (int) ((viewHeight - padValue*2) / (textSize + dividerHeight));
        numInRow = (int) ((viewWidth - padValue*2) / textSize);

        Log.d(TAG, "rows :" + rows);
        Log.d(TAG, "numInRows :" + numInRow);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        Log.d(TAG, "draw...");
        //背景
        canvas.drawColor(bgColor);


        if(lines==null){
            canvas.drawText("No contents!", 0,viewHeight/2, paint);
            return;
        }

        Log.d(TAG, "first line: " + firstIndex);
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
        lines.clear();
        parseBook(path);
    }

    private void parseBook(String path){
        File file = new File(path);
        Log.d(TAG, "parsePath "+ path);
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
            StringBuilder builder = new StringBuilder();
            try {
                while((line=reader.readLine())!=null){
                    builder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            contents = builder.toString();
        }
    }

    private void parseLines(String contents) {
        int totalRows = contents.length()%numInRow==0? contents.length() / numInRow: contents.length() / numInRow +1;
        for(int i=0; i<totalRows; i++){
            lines.add(contents.substring(numInRow*i, numInRow*(i+1)>contents.length()? contents.length(): numInRow*(i+1)));
            Log.d("TAG", "lines: " + i + " " +lines.get(i));
        }

        Log.d(TAG, "lines size: " + lines.size());
    }
}
