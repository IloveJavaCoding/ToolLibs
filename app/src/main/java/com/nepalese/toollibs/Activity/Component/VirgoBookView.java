package com.nepalese.toollibs.Activity.Component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

import com.nepalese.toollibs.Activity.Events.ClickEvent;
import com.nepalese.toollibs.Activity.Events.RefershBookTagEvent;
import com.nepalese.toollibs.Activity.Events.SentTotalLineEvent;
import com.nepalese.toollibs.R;
import com.nepalese.toollibs.Util.ConvertUtil;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VirgoBookView extends View {
    private final String TAG = "BOOK_VIEW";
    private Paint paint;
    private int viewWidth;
    //viewHeight: the height of view displayed; viewHeightAll: the real height of bookView;
    private int viewHeight;
    private float viewHeightAll;
    private int readMode;//1: scroll; 2: page

    //attrs of text px
    private float textSize;
    private int textColor;
    private float dividerHeight;//行间距
    private float padValue;//padding
    private int bgColor;//背景颜色

    private int rows, totalRows;//rows:每页所内显示行数；totalRows:文本总行数
    private int numInRow;//每行最多容纳字数
    private int firstIndex=0;//the first line to draw
    private float curHeight=0;//用于画文字的起始y坐标

    private List<String> lines = new ArrayList<>();
    private String contents;

    private OverScroller scroller;
    private VelocityTracker tracker;
    private int touchSlop, maxVelocity;

    private boolean isShow = true;
    private float offset=0;//滑动距离
    private final int textDivider = 3;//文字间隔
    private final int padTop = 40;//顶部缩进
    public static final int MODE_SLIP = 0x01;//滑动模式； 默认
    public static final int MODE_PAGE = 0x02;//翻页模式

    public VirgoBookView(Context context) {
        this(context, null);
    }

    public VirgoBookView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VirgoBookView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // 解析自定义属性
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.VirgoBookView);
        textSize = ta.getDimension(R.styleable.VirgoBookView_bookTextSize, 18.0f);
        textColor = ta.getColor(R.styleable.VirgoBookView_bookTextColor, 0xff000000);
        bgColor = ta.getColor(R.styleable.VirgoBookView_bgColor, 0xffe3edcd);
        dividerHeight = ta.getDimension(R.styleable.VirgoBookView_bookDividerHeight, 10.0f);
        padValue = ta.getDimension(R.styleable.VirgoBookView_padValue, 15.0f);
        ta.recycle();
        // </end>

        // 初始化paint
        paint = new Paint();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setAntiAlias(true);

        //default setting
        readMode = MODE_SLIP;
        curHeight = padTop + padValue;

        scroller = new OverScroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        touchSlop = configuration.getScaledTouchSlop();
        maxVelocity = configuration.getScaledMinimumFlingVelocity();
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

//        widthMeasureSpec = MeasureSpec.makeMeasureSpec(1080, MeasureSpec.AT_MOST);
        //heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, 0),
                resolveSizeAndState(height, heightMeasureSpec, 0));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        viewWidth = getWidth();
        viewHeight = getHeight();
        Log.d(TAG, "w*h: "+ viewWidth + " * " + viewHeight);

        calculateRows();
        parseLines(contents);

        viewHeightAll = padValue*2 + (textSize + dividerHeight)*totalRows;
        super.onLayout(changed, left, top, right, bottom);
    }

    private void calculateRows(){
        //计算行数
        rows = (int) ((viewHeight - padValue*2) / (textSize + dividerHeight));
        numInRow = (int) ((viewWidth - padValue*2) / (textSize+textDivider));

        Log.d(TAG, "padValue :" + padValue);
        Log.d(TAG, "textSize :" + textSize);
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
            canvas.drawText("No contents!", 0, viewHeight/2, paint);
            return;
        }

        switch (readMode){
            case MODE_SLIP://slip
                drawMode1(canvas);
                break;
            case MODE_PAGE://page
                drawMode2(canvas);
                break;
            default:
                //
                break;
        }
    }

    private void drawMode1(Canvas canvas) {
        firstIndex = (int) ((curHeight- padTop - padValue)/(textSize+dividerHeight));
        Log.i(TAG, "SLIP: drawing.....curHeight: " + curHeight + " offset: " + offset);
        for(int i=0; i<rows+3; i++){
            canvas.drawText(lines.get(i+firstIndex), padValue,  curHeight + padTop + padValue + (i)*(textSize+dividerHeight), paint);
        }
    }

    //only draw a page
    private void drawMode2(Canvas canvas){
        Log.i(TAG, "PAGE: drawing.....first line: " + firstIndex);
        for(int i=0; i<rows; i++){
            if(i+firstIndex<lines.size()){
                canvas.drawText(lines.get(i+firstIndex), padValue, padTop + padValue  + i*(textSize+dividerHeight), paint);
            }
        }
    }

    public void toSlipMode() {
        readMode = MODE_SLIP;
        if(firstIndex>1){
            curHeight = (firstIndex-1)*(textSize + dividerHeight) + padTop + padValue;
        }else{
            curHeight = 0;
        }

    }

    public void toPageMode() {
        readMode = MODE_PAGE;
        firstIndex = (int) ((curHeight - padTop - padValue) / (textSize + dividerHeight)) + 1;
        scrollTo(0, (int) (padTop + padValue));
        Log.i(TAG, "change to page mode: firstIndex" + firstIndex);
    }

    private float startX, startY, oldX, oldY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if(readMode==MODE_SLIP){
            initVelocityTracker();
            tracker.addMovement(event);
        }

        float x,y;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                oldX = event.getX();
                oldY = event.getY();

                if(!scroller.isFinished()){
                    scroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(readMode==MODE_SLIP){
                    y = event.getY();
                    flashSlip(y);
                    oldX = event.getX();
                    oldY = event.getY();
                }

                break;
            case MotionEvent.ACTION_UP:
                x = event.getX();
                y = event.getY();
                if(Math.abs(x-startX)<10 && Math.abs(y-startY)<10){
                    //click
                    Log.d(TAG, "click......" + isShow);
                    EventBus.getDefault().post(new ClickEvent(isShow));
                    if(isShow){
                        isShow = false;
                    }else{
                        isShow = true;
                    }
                }
                else{
                    if(readMode==MODE_PAGE){
                        flashPage(x);
                        //save tag
                        EventBus.getDefault().post(new RefershBookTagEvent(firstIndex));
                    }
                    else{
                        //slip mode
                        final VelocityTracker tempTracker = tracker;
                        tempTracker.computeCurrentVelocity(1000);
                        int velocityY = (int) tempTracker.getYVelocity();

                        if(velocityY<-maxVelocity){//向上快速滑动
                            Log.i(TAG, "向上快速滑动 velocity: " + velocityY);
                        }else if(velocityY>maxVelocity){//向下快速滑动
                            Log.i(TAG, "向下快速滑动 velocity: " + velocityY);
                        }else{//slow slip
                            Log.i(TAG, "slow slip velocity: " + velocityY);
                        }
                        recycleVelocityTracker();
                        EventBus.getDefault().post(new RefershBookTagEvent(getCurLines()));
                    }
                }
                break;
        }
        return true;
    }

    private void initVelocityTracker() {
        if(tracker==null){
            tracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker(){
        if(tracker!=null){
            tracker.recycle();
            tracker = null;
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(readMode==MODE_SLIP  && scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    private void flashSlip(float y){
        offset = oldY-y;
        if(Math.abs(offset)>10){
            float oldScrollY = getScrollY();
            curHeight = oldScrollY + offset;
            if((curHeight-padTop)>(viewHeightAll-viewHeight)){
                curHeight = viewHeightAll-viewHeight;
                offset = curHeight - oldScrollY;
            }else if(curHeight < (padTop+padValue)){
                curHeight = padTop + padValue;
                offset = curHeight - oldScrollY;
            }

            Log.i(TAG, "offset: " + offset + " curHeight: " + curHeight + " scrollY: " + oldScrollY);
            //start animation
            scroller.startScroll(scroller.getFinalX(), scroller.getFinalY(), 0,(int)offset);
            //scrollBy(0, (int) offset);
            invalidate();
        }
    }

    private void flashPage(float x){
        Log.d(TAG, "offset: " + (x-startX));
        if(Math.abs(x-startX)<10){
            return;
        }
        if(x-startX>0){//右滑： 上一页
            firstIndex -= (rows+3);
            if(firstIndex<0){
                firstIndex = 0;
            }
        }else if(x-startX<0){//左滑：下一页
            if(firstIndex+rows<=lines.size()){
                firstIndex += (rows-3);//repeat 3 lines
            }
        }
        invalidate();
    }

    public void setTag(int tag){
        Log.i(TAG, "tag: " + tag);
        if(tag>0){
            if(readMode==MODE_PAGE){
                firstIndex = tag;
            }else{
                curHeight = (tag-1)*(textSize + dividerHeight) + padTop + padValue;
                scrollTo(0, (int)curHeight);
            }
        }else{
            if(readMode==MODE_PAGE){
                firstIndex = 0;
            }else{
                curHeight = 0;
            }
        }
    }

    public void seekTo(int position){
        Log.d(TAG, "seek to: "+ position);
        setTag(position);
        invalidate();
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getCurLines() {
        return (int) ((curHeight-padValue-padTop) / (textSize + dividerHeight)) + 1;
    }

    public void setFilePath(String path){
        if (TextUtils.isEmpty(path)) { return;}
        lines.clear();
        parseBook(path);
        //contents = FileUtil.readContents(path, "utf-8");
    }

    public void setReadMode(int readMode) {
        this.readMode = readMode;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    private void parseBook(String path){
        File file = new File(path);
        Log.d(TAG, "parsePath "+ path);
        if(file.exists()){
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
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line;
            StringBuilder builder = new StringBuilder();
            try {
                while((line=reader.readLine())!=null){
                    //deal with line
                    line = replaceSpecialStr(line);
                    builder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            contents = builder.toString();
        }
    }

    /**
     * 去除字符串中的空格、换行符、制表符等
     */
    public static String replaceSpecialStr(String str) {
        String temp = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            temp = m.replaceAll("");
        }
        return temp;
    }

    private void parseLines(String contents) {
        //totalRows = contents.length()%numInRow==0? contents.length() / numInRow: contents.length() / numInRow +1;
        String[] sections = contents.split("。");

        //"\\u300" 中文缩进一个字；
        for(int i=0; i<sections.length; i++){
            String section = "\u3000\u3000" + sections[i] + "。";
            int tempRows = section.length() % numInRow==0? section.length() / numInRow: section.length() / numInRow +1;

            for(int j=0; j<tempRows; j++){
                String temp = section.substring(numInRow*j, Math.min(numInRow*(j+1), section.length()));
                lines.add(temp);
            }
        }

        Log.d(TAG, "lines size: " + lines.size());
        totalRows = lines.size();
        EventBus.getDefault().post(new SentTotalLineEvent(totalRows));
    }
}
