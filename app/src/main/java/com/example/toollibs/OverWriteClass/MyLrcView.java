package com.example.toollibs.OverWriteClass;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import com.example.toollibs.R;
import com.example.toollibs.Util.BitmapUtil;
import com.example.toollibs.Util.ConvertUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyLrcView extends View {
    private final String TAG = "MY_LRC_VIEW";
    private Context context;

    private Paint mainPaint, secPaint;
    private int viewWidth;
    private int viewHeight;

    //lrc lines
    private List<MyLrcLine> lineList = new LinkedList<>();
    private int curLine = 0;
    private long nextTime = 0;

    //attrs of text px
    private float textSizeMain, textSizeSec;
    private int textColorMain, textColorSec;
    private float dividerHeight;//行间距
    private float padValue;//padding

    private int rows;//
    private Bitmap background;
    private OverScroller scroller;
    private static final String DEFAULT_TEXT = "暂无歌词，快去下载吧！";

    public MyLrcView(Context context) {
        this(context, null);
    }

    public MyLrcView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLrcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // 解析自定义属性
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.MyLrcView);
        textSizeMain = ta.getDimension(R.styleable.MyLrcView_lrcTextSizeMain, 25.0f);
        textSizeSec = ta.getDimension(R.styleable.MyLrcView_lrcTextSizeSec, 15.0f);
        textColorMain = ta.getColor(R.styleable.MyLrcView_lrcTextColorMain, 0xff66ccff);
        textColorSec = ta.getColor(R.styleable.MyLrcView_lrcTextColorSec, 0xff8a8a8a);

        dividerHeight = ta.getDimension(R.styleable.MyLrcView_lrcDividerHeight, 15.0f);
        padValue = ta.getDimension(R.styleable.MyLrcView_lrcpadValue, 25.0f);
        ta.recycle();
        // </end>

        // 初始化paint
        mainPaint = new Paint();
        mainPaint.setTextSize(textSizeMain);
        mainPaint.setColor(textColorMain);
        mainPaint.setAntiAlias(true);

        secPaint = new Paint();
        secPaint.setTextSize(textSizeSec);
        secPaint.setColor(textColorSec);
        secPaint.setAntiAlias(true);

        scroller = new OverScroller(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        viewWidth = getWidth();
        viewHeight = getHeight();
        calculateRows();
        scaleBackground();
        super.onLayout(changed, left, top, right, bottom);
    }

    private void calculateRows() {
        rows = (int) ((viewHeight - padValue*2) / (textSizeSec+dividerHeight));
    }

    private void scaleBackground(){
        if (background != null) {
            background = Bitmap.createScaledBitmap(background, viewWidth, viewHeight, true);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw background
        if (background != null) {
            canvas.drawBitmap(background, 0, 0, null);
        }else{
            canvas.drawColor(Color.TRANSPARENT);
        }

        float centerY = (viewHeight - textSizeMain) / 2.0f;
        //draw when have no lrc
        if (lineList.isEmpty()) {
            canvas.drawText(DEFAULT_TEXT, getStartX(DEFAULT_TEXT, mainPaint), centerY, mainPaint);
            return;
        }

//        //draw lrc
//        canvas.drawText(lineList.get(curLine).lrc, getStartX(lineList.get(curLine).lrc, mainPaint), centerY, mainPaint);
//
//        //draw above
//        int aboveRows = (rows -1) / 2;
//        for(int i=1; i<=aboveRows; i++){
//            if(curLine-i<0){
//                break;
//            }
//            canvas.drawText(lineList.get(curLine-i).lrc, getStartX(lineList.get(curLine-i).lrc, secPaint), centerY - i * (textSizeSec+dividerHeight), secPaint);
//        }
//
//        //draw bottom
//        for(int i=1; i<=aboveRows; i++){
//            if(curLine+i>lineList.size()-1){
//                break;
//            }
//            canvas.drawText(lineList.get(curLine+i).lrc, getStartX(lineList.get(curLine+i).lrc, secPaint), centerY + i * (textSizeSec+dividerHeight), secPaint);
//        }

        for(int i=0; i<lineList.size(); i++){
            if(curLine==i){
                canvas.drawText(lineList.get(curLine-i).lrc, getStartX(lineList.get(curLine-i).lrc, mainPaint), centerY + i * (textSizeSec+dividerHeight), mainPaint);
            }else{
                canvas.drawText(lineList.get(curLine-i).lrc, getStartX(lineList.get(curLine-i).lrc, secPaint), centerY + i * (textSizeSec+dividerHeight), secPaint);
            }
        }

        super.onDraw(canvas);
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
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private float getStartX(String str, Paint paint){
        return (viewWidth - paint.measureText(str)) / 2.0f;
    }

    public void setBackground(Bitmap background) {
        this.background = BitmapUtil.fastBlurBitmap(context, background, 200);
    }

    private void reset(){
        lineList.clear();
        curLine = 0;
        nextTime = 0;
        if(scroller.isFinished()){
            scroller.abortAnimation();
        }
    }

    public void loopMode(){
        curLine = 0;
        nextTime = 0;
        scrollTo(0,0);
        scroller.abortAnimation();
        scroller = null;
        scroller = new OverScroller(context);
    }

    public void setLrc(String lrc) {
        reset();
        if (TextUtils.isEmpty(lrc)) { return;}
        parseLrc(new InputStreamReader(new ByteArrayInputStream(lrc.getBytes())));
    }

    public void setLrcFile(String path){
        File file = new File(path);
        if(file.exists()){
            reset();
            String format;
            if(ConvertUtil.isUtf8(file)){
                format = "UTF-8";
            }else{//utf_8
                format = "GBK";
            }

            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            InputStreamReader inputStreamReader = null;//'utf-8' 'GBK'
            try {
                inputStreamReader = new InputStreamReader(inputStream, format);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            parseLrc(inputStreamReader);
        }
    }

    private void parseLrc(InputStreamReader inputStreamReader){
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line;
        try {
            while((line=reader.readLine())!=null){
                //deal with line
                parseLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void seekTo(long time){
        if(time<nextTime){
            return;
        }
        for(int i=0; i<lineList.size(); i++){
            if(i<lineList.size()-1){
                if(time>=lineList.get(i).getTime() && time<lineList.get(i+1).getTime()){
                    curLine = i;
                    nextTime = lineList.get(i+1).getTime();
//                    scrollBy(0, (int)(textSizeSec+dividerHeight));
                    scroller.startScroll(scroller.getFinalX(), scroller.getFinalY(), 0,  (int)(textSizeSec+dividerHeight));
                    invalidate();
                    break;
                }
            }else{
                curLine = i;
                nextTime = lineList.get(i).getTime() + 60000;
//                scrollBy(0, (int)(textSizeSec+dividerHeight));
                scroller.startScroll(scroller.getFinalX(), scroller.getFinalY(), 0,  (int)(textSizeSec+dividerHeight));
                invalidate();
            }
        }
    }

    private long parseTime(String time) {
        // 00:01.10
        String[] min = time.split(":");
        String[] sec = min[1].split("\\.");

        long minInt = Long.parseLong(min[0].replaceAll("\\D+", "")
                .replaceAll("\r", "").replaceAll("\n", "").trim());
        long secInt = Long.parseLong(sec[0].replaceAll("\\D+", "")
                .replaceAll("\r", "").replaceAll("\n", "").trim());
        long milInt = Long.parseLong(sec[1].replaceAll("\\D+", "")
                .replaceAll("\r", "").replaceAll("\n", "").trim());

        return minInt * 60 * 1000 + secInt * 1000 + milInt;// * 10;
    }

    private void parseLine(String line) {
        Matcher matcher = Pattern.compile("\\[\\d.+\\].+").matcher(line);
        // 如果形如：[xxx]后面啥也没有的，则return空
        if (!matcher.matches()) {
            Long time;
            String str;
            String con = line.replace("\\[", "").replace("\\]", "");
            Log.d(TAG, con);
            if(con.matches("^\\d.+")){//time
                time = parseTime(con);
                str = " ";
            }else{
                return;
            }
            lineList.add(new MyLrcLine(time, str));
            return;
        }

        //[00:23.24]让自己变得快乐
        line = line.replaceAll("\\[", "");
        String[] result = line.split("\\]");
        lineList.add(new MyLrcLine(parseTime(result[0]), result[1]));
    }
}

class MyLrcLine {
    long time;
    String lrc;

    public MyLrcLine(long time, String lrc) {
        this.time = time;
        this.lrc = lrc;
    }

    public MyLrcLine() {
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }
}
