package com.nepalese.toollibs.Activity.Component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import com.nepalese.toollibs.Activity.Events.RefershLrcLine;
import com.nepalese.toollibs.R;
import com.nepalese.toollibs.Util.BitmapUtil;
import com.nepalese.toollibs.Util.ConvertUtil;

import org.greenrobot.eventbus.EventBus;

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

public class VirgoLrcView extends View {
    private final String TAG = "MY_LRC_VIEW";
    private Context context;

    private Paint mainPaint, secPaint;
    private int viewWidth;
    private int viewHeight;
    private float totalHeight;

    //lrc lines
    private List<MyLrcLine> lineList = new LinkedList<>();
    private int curLine = 0;
    private long nextTime = 0;

    //attrs of text px
    private float textSizeMain, textSizeSec;
    private int textColorMain, textColorSec;
    private float dividerHeight;//行间距
    private float padValue;//padding

    private boolean isPlaying = false;
    private boolean isDown = false;
    private Bitmap background;
    private OverScroller scroller;
    private static final String DEFAULT_TEXT = "暂无歌词，快去下载吧！";

    public VirgoLrcView(Context context) {
        this(context, null);
    }

    public VirgoLrcView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VirgoLrcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // 解析自定义属性
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.VirgoLrcView);
        textSizeMain = ta.getDimension(R.styleable.VirgoLrcView_lrcTextSizeMain, 25.0f);
        textSizeSec = ta.getDimension(R.styleable.VirgoLrcView_lrcTextSizeSec, 15.0f);
        textColorMain = ta.getColor(R.styleable.VirgoLrcView_lrcTextColorMain, 0xff66ccff);
        textColorSec = ta.getColor(R.styleable.VirgoLrcView_lrcTextColorSec, 0xff8a8a8a);

        dividerHeight = ta.getDimension(R.styleable.VirgoLrcView_lrcDividerHeight, 15.0f);
        padValue = ta.getDimension(R.styleable.VirgoLrcView_lrcpadValue, 25.0f);
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
        totalHeight = (textSizeSec+dividerHeight)*lineList.size();
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

        //画选择线
        if(isPlaying && isDown){
            float baseLine = centerY+getScrollY();
            canvas.drawLine(padValue, baseLine, viewWidth-padValue, baseLine, mainPaint);
        }

        for(int i=0; i<lineList.size(); i++){
            if(curLine==i){
                canvas.drawText(lineList.get(i).lrc, getStartX(lineList.get(i).lrc, mainPaint), centerY + i * (textSizeSec+dividerHeight), mainPaint);
            }else{
                canvas.drawText(lineList.get(i).lrc, getStartX(lineList.get(i).lrc, secPaint), centerY + i * (textSizeSec+dividerHeight), secPaint);
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

    private float oldY, startY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float y;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isDown = true;
                oldY = event.getY();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "scrollY: " + getScrollY());
                if(getScrollY()>totalHeight || getScrollY()<(-viewHeight/3)){
                    handler.sendEmptyMessage(0);
                    return true;
                }
                y = event.getY();
                if(Math.abs(y-startY) > 5){
                    scroller.startScroll(scroller.getFinalX(), scroller.getFinalY(), 0, (int) (startY-y));
                    invalidate();
                }

                startY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                isDown = false;
                if(isPlaying){
                    y = event.getY();
                    calculateCurLine(oldY-y);
                }
                break;
        }

        return true;
    }

    private void calculateCurLine(float y) {
        float offLine = ((Math.abs(y)-textSizeSec) / (textSizeSec + dividerHeight)) * 1.0f;
        Log.i(TAG, "off line: " + offLine);
        if(y>0){
            curLine = (int) (curLine + offLine + 1);
        }else{
            curLine = (int) (curLine - offLine - 1);
        }

        if(curLine>lineList.size()-1){
            curLine = lineList.size()-1;
        }else if(curLine<0){
            curLine = 0;
        }
        //跳到前面时需要
        if(y<0){
            nextTime = lineList.get(curLine+1).getTime();
        }

        if(curLine<lineList.size()-1){
            long time = lineList.get(curLine).getTime();
            EventBus.getDefault().post(new RefershLrcLine(time));
        }
    }

    private int fourOutFiveIn(float a, boolean convert){
        int temp = (int) (a*10);
        int m = temp % 10;
        int out = temp/10;
        if(convert){
            if(m<5){
                return out+1;
            }else {
                return out;
            }
        }else{
            if(m>5){
                return out+1;
            }else {
                return out;
            }
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0) {
                scrollTo(0, (int) (curLine * (textSizeSec + dividerHeight)));
                setScrollY((int)(curLine*(textSizeSec+dividerHeight)));
                scroller.abortAnimation();
                scroller = null;
                scroller = new OverScroller(context);
                invalidate();
            }
        }
    };

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private float getStartX(String str, Paint paint){
        return (viewWidth - paint.measureText(str)) / 2.0f;
    }

    public void setBackground(Bitmap background) {
        this.background = BitmapUtil.fastBlurBitmap(background, 200);
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

    public void setPlaying(boolean playing) {
        isPlaying = playing;
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
        if(time<lineList.get(curLine).getTime()){//往回跳
            //
        }else if(time<nextTime){
            return;
        }

        for(int i=0; i<lineList.size(); i++){
            if(i<lineList.size()-1){
                if(time>=lineList.get(i).getTime() && time<lineList.get(i+1).getTime()){
                    int temp = i - curLine;
                    curLine = i;
                    nextTime = lineList.get(i+1).getTime();
                    scroller.startScroll(scroller.getFinalX(), scroller.getFinalY(), 0,  (int)(textSizeSec+dividerHeight)*temp);
                    invalidate();
                    break;
                }
            }else{//last line
                int temp = i - curLine;
                curLine = i;
                nextTime = lineList.get(i).getTime() + 60000;
                scroller.startScroll(scroller.getFinalX(), scroller.getFinalY(), 0,  (int)(textSizeSec+dividerHeight)*temp);
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
