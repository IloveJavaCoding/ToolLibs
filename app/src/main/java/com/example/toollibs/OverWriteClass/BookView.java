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
import com.example.toollibs.Util.FileUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookView extends View {
    private final String TAG = "BOOK_VIEW";
    private Paint paint;
    private int viewWidth;
    //viewHeight: the height of view displayed; viewHeightAll: the real height of bookView;
    private int viewHeight, viewHeightAll;
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
    private float curHeight=0;//画布顶端y轴坐标

    private List<String> lines = new ArrayList<>();
    private String contents;

    private boolean isDraw = false;//监听是否已画完；
    private boolean isSlip = false;//监听是否滑动
    private float offset=0;//滑动距离

    private final int textDivider = 3;//文字间隔
    private final int padTop = 15;//顶部缩进
    public static final int MODE_SLIP = 0x01;//滑动模式； 默认
    public static final int MODE_PAGE = 0x02;//翻页模式

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
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.BookView);
        textSize = ta.getDimension(R.styleable.BookView_bookTextSize, 18.0f);
        textColor = ta.getColor(R.styleable.BookView_bookTextColor, 0xff000000);
        bgColor = ta.getColor(R.styleable.BookView_bgColor, 0xffe3edcd);
        dividerHeight = ta.getDimension(R.styleable.BookView_bookDividerHeight, 10.0f);
        padValue = ta.getDimension(R.styleable.BookView_padValue, 15.0f);
        ta.recycle();
        // </end>

        // 初始化paint
        paint = new Paint();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setAntiAlias(true);

        //default setting
        readMode = MODE_SLIP;
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
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.UNSPECIFIED);
        setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, 0),
                resolveSizeAndState(height, heightMeasureSpec, 0));

//        viewWidth = getMeasuredWidth();
        viewHeightAll = getMeasuredHeight();
        Log.d(TAG, "viewHeightAll: " +  viewHeightAll);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        viewWidth = getWidth();
        viewHeight = getHeight();
        Log.d(TAG, "w*h: "+ viewWidth + " * " + viewHeight);

        calculateRows();
        parseLines(contents);
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
                if(!isDraw){
                    isDraw = true;
                    drawMode1(canvas);//only draw once
                }
                translateCanvas(canvas);
                break;
            case MODE_PAGE://page
                drawMode2(canvas);
                break;
            default:
                //
                break;
        }
    }

    private void translateCanvas(Canvas canvas) {
        if(isSlip){
            isSlip = false;
            canvas.translate(0, offset);
        }
    }

    //draw all lines
    private void drawMode1(Canvas canvas) {
        for(int i=0; i<totalRows; i++){
            canvas.drawText(lines.get(i), padValue, padTop + padValue+i*(textSize+dividerHeight), paint);
        }
    }

    //only draw a page
    private void drawMode2(Canvas canvas){
        Log.d(TAG, "first line: " + firstIndex);
        for(int i=0; i<rows; i++){
            if(i+firstIndex<lines.size()){
                canvas.drawText(lines.get(i+firstIndex), padValue, padTop + padValue+i*(textSize+dividerHeight), paint);
                //一个字一个字画
//                String line = lines.get(i+firstIndex);
//                for(int j=0; j<line.length(); j++){
//                    canvas.drawText(line.substring(j, j+1), padValue + j*(textSize+textDivider), 35 + padValue+i*(textSize+dividerHeight), paint);
//                }
            }
        }
    }

    private float oldX, oldY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float x,y;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                oldX = event.getX();
                oldY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(readMode==MODE_SLIP){
                    y = event.getY();
                    flashSlip(y);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(readMode==MODE_PAGE){
                    x = event.getX();
                    flashPage(x);
                }
                break;
        }

        return true;
    }

    private void flashSlip(float y){
        offset = oldY-y;
        curHeight += offset;
        if(curHeight<=0){
            offset = offset-curHeight;
            curHeight = 0;
        }else if(curHeight+viewHeight>=viewHeightAll){
            offset = curHeight+viewHeight-viewHeightAll;
            curHeight = viewHeightAll - viewHeight;
        }
        invalidate();
        //save tag
        EventBus.getDefault().post(new RefershBookTagEvent(getCurHeight()));
    }

    private void flashPage(float x){
        if(x-oldX>0){//右滑： 上一页
            firstIndex -= (rows+3);
            if(firstIndex<=0){
                firstIndex = 0;
            }
        }else if(x-oldX<0){//左滑：下一页
            if(firstIndex+rows<=lines.size()){
                firstIndex += (rows-3);//repeat 3 lines
            }
        }
        invalidate();
        //save tag
        EventBus.getDefault().post(new RefershBookTagEvent(firstIndex));
    }

    private void move(float x, float y){
        if(y-oldY>0){//下滑
            firstIndex -= 3;
            if(firstIndex<=0){
                firstIndex = 0;
            }
        }else if(y-oldY<0){//上滑
            if(firstIndex+rows<=lines.size()){
                firstIndex += 3;
            }
        }
        //这个方法里往往需要重绘界面，使用这个方法可以自动调用onDraw（）方法。（主线程）
        invalidate();
        //save tag
        EventBus.getDefault().post(new RefershBookTagEvent(firstIndex));
    }

    public void setTag(int tag){
        if(tag>0 && tag<lines.size()){
            if(readMode==MODE_PAGE){
                firstIndex = tag;
            }else{
                curHeight = tag*(textSize + dividerHeight) + padTop + padValue;
            }
        }
    }

    public void seekTo(int position){
        setTag(position);
        invalidate();
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getCurHeight() {
        return (int) ((curHeight-padValue-padTop) / (textSize + dividerHeight));
    }

    public int getFirstIndex(){
        return firstIndex;
    }

    public void setFilePath(String path){
        if (TextUtils.isEmpty(path)) { return;}
        lines.clear();
        parseBook(path);
        //contents = FileUtil.readContents(path, "utf-8");
    }

    public int getReadMode() {
        return readMode;
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
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);//'utf-8' 'GBK'
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
            Pattern p = Pattern.compile("\\s*|\t|\r");
            Matcher m = p.matcher(str);
            temp = m.replaceAll("");
        }
        return temp;
    }

    private void parseLines(String contents) {
        totalRows = contents.length()%numInRow==0? contents.length() / numInRow: contents.length() / numInRow +1;
        for(int i=0; i<totalRows; i++){
            String temp = contents.substring(numInRow*i, Math.min(numInRow*(i+1), contents.length()));
            if(temp.contains("\n")){
                Log.d("TAG", "find: "+ i);
                lines.add(temp.substring(0, temp.lastIndexOf("\n")));
                lines.add(temp.substring(temp.lastIndexOf("\n")+1));
                totalRows++;
            }
        }

        Log.d(TAG, "lines size: " + lines.size());
    }
}