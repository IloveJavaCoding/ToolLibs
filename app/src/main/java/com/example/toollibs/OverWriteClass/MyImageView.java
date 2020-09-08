package com.example.toollibs.OverWriteClass;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.toollibs.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nepalese on 2020/9/8 10:25
 * @usage 一个可自动播放图片的自定义控件
 * 1. 进出动画可选
 * 2. 每张图片显示时间可控（分别控制）
 * 3. 可加载网络图片
 */
public class MyImageView extends FrameLayout {
    private static final String TAG = "TransformImageView";

    private final int MSG_CODE_PLAY = 0;//执行播放
    private final int MSG_CODE_PLAY_UP = 1;//执行上层播放
    private final int MSG_CODE_PLAY_BG = 2;//执行底层播放
    private final int MSG_CODE_PLAY_DELAY = 3;//延时执行播放
    private final int MSG_CODE_OVER = 4;//执行结束
    private final int MSG_CODE_TIMING = 5;//定时执行

    private Context context;

    //两个imageView, 一个上层，一个下层；
    private ImageView ivUP;
    private ImageView ivBG;

    //进入，出去动画
    private Animation animationIN;
    private Animation animationOUT;

    //用于存储图片的uri(基于Glide显示图片)
    private List<String> imgUri = new ArrayList<>();

    private int nowIndex = -1;//当前图片索引
    private int nextIndex = 0;//下一个图片索引

    private long duration = 0;//单个图片播放时间(当前图片）
    private long maxAnimTime;//进入和出去动画最大使用时间

    public MyImageView(@NonNull Context context) {
        this(context, null);
    }

    public MyImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    //当控件可见性发生变化时调用
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(visibility!=VISIBLE){//由可见变不可见
            Log.d(TAG,  "停止定时播放!");
            handler.removeMessages(MSG_CODE_TIMING);
        }else {
            Log.d(TAG,  "开启定时播放!");
            showNextDelay(duration);//duration 后播放下一张图片
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        recycle();
    }

    //=======================================set methods===========================================
    public void setAnimationIN(Animation animationIN) {
        this.animationIN = animationIN;
    }

    public void setAnimationOUT(Animation animationOUT) {
        this.animationOUT = animationOUT;
    }

    public void setImgUri(List<String> imgUri) {
        this.imgUri = imgUri;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    //==============================================================================================
    //初始化
    private void init() {
        //创建两个ImageView， 先隐藏
        ivUP = createView();
        ivBG = createView();
        setViewState(ivUP, INVISIBLE);
        setViewState(ivBG, INVISIBLE);

        //将创建的imageView添加到自定义控件
        //默认position为-1， 先添加的在下面
        addView(ivBG);
        addView(ivUP);

        //载人进出场动画
        animationIN = AnimationUtils.loadAnimation(context, R.anim.miv_anim_fade_in);
        animationOUT = AnimationUtils.loadAnimation(context, R.anim.miv_anim_fade_out);
    }

    //创建imageView， 并设置一些基本参数
    private ImageView createView() {
        ImageView imageView = new ImageView(context);
        imageView.setBackgroundColor(Color.TRANSPARENT);//背景透明
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);//图片扩展方式
        //设置布局， 宽高继承父控件(MyImageView)
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);

        return imageView;
    }

    //控制View的显隐状态
    private void setViewState(View view, int state) {
        if (view != null) {
            if (view.getVisibility() != state) {
                view.setVisibility(state);
            }
        }
    }

    //定时播放下一个图片
    public void showNextDelay(long duration) {
        if(this.duration!=duration){
            this.duration = duration;
        }

        if(duration>0){
            showNextImg(nowIndex+1);
            if(imgUri.size()>1){
                handler.sendEmptyMessageDelayed(MSG_CODE_PLAY_DELAY, duration);
            }
        }else{
            handler.removeMessages(MSG_CODE_PLAY_DELAY);
        }
    }

    //播放下一张图片
    private void showNextImg(int position) {
        if(ivBG.getVisibility()==VISIBLE) {
            resetView();
        }

        //更新下一张图的索引
        if(imgUri.size()>position){
            nextIndex = position;
        }else if(imgUri.size()>0){//position溢出
            nextIndex = 0;
        }else{
            return;
        }

        handler.sendEmptyMessage(MSG_CODE_PLAY);
    }

    //重置控件
    private void resetView() {
        Log.d(TAG, "reset view");
        handler.removeMessages(MSG_CODE_TIMING);
        handler.removeMessages(MSG_CODE_PLAY);
        handler.removeMessages(MSG_CODE_PLAY_BG);
        handler.removeMessages(MSG_CODE_PLAY_UP);
        handler.removeMessages(MSG_CODE_PLAY_DELAY);
        handler.removeMessages(MSG_CODE_OVER);

        ivUP.clearAnimation();
        ivBG.clearAnimation();
        setViewState(ivUP, INVISIBLE);
        setViewState(ivBG, INVISIBLE);

        if(imgUri.size()>0){
            imgUri.clear();
        }
    }

    private void playUPView() {
        setViewState(ivUP, VISIBLE);
        ivUP.startAnimation(animationOUT);
        ivUP.setAlpha(1f);

        long in = animationIN.getDuration();
        long out = animationOUT.getDuration();

        maxAnimTime = Math.max(in, out+duration);
        handler.sendEmptyMessageDelayed(MSG_CODE_PLAY_UP, out);
    }

    private void playBGView() {
        setViewState(ivBG, VISIBLE);
        ivBG.setAlpha(1f);
        ivBG.startAnimation(animationIN);

        long in = animationIN.getDuration();
        if(nowIndex!=-1){
            long out = animationOUT.getDuration();
            maxAnimTime = Math.max(in, out);
        }else{
            maxAnimTime = in;
        }

        handler.sendEmptyMessageDelayed(MSG_CODE_PLAY_BG, in);
    }

    //回收资源
    private void recycle() {
        if(imgUri.size()>0){
            resetView();
            imgUri.clear();
        }
    }

    //======================================handler=================================================
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_CODE_PLAY:
                    if(nowIndex==-1){//首次播放
                        // 默认上布局是隐藏的，然后加载到下布局里面
                        GlideHelper.displayImg(imgUri.get(nextIndex), ivBG);
                        playBGView();
                    }else{
                        playUPView();
                        handler.sendEmptyMessage(MSG_CODE_PLAY_DELAY);
                    }

                    handler.sendEmptyMessageDelayed(MSG_CODE_OVER, maxAnimTime);
                    break;
                case MSG_CODE_PLAY_DELAY:
                    GlideHelper.displayImg(imgUri.get(nextIndex), ivBG);
                    playBGView();
                    break;
                case MSG_CODE_PLAY_UP:
                    setViewState(ivUP, INVISIBLE);
                    break;
                case MSG_CODE_TIMING:
                    showNextDelay(duration);
                    break;
                case MSG_CODE_OVER:
                    GlideHelper.displayImg(imgUri.get(nextIndex), ivUP);
                    ivUP.clearAnimation();
                    ivBG.clearAnimation();
                    setViewState(ivUP, VISIBLE);
                    setViewState(ivBG, INVISIBLE);

                    nowIndex = nextIndex;
                    break;
            }
        }
    };

    public static class GlideHelper {
        //uri: "file://" + file.getPath();
        public static void displayImg(String uri, ImageView iv) {
            if (iv == null || uri == null) return;
            Log.i(TAG, "play image: " + uri);
            Glide.with(iv.getContext()).load(uri).into(iv);
        }
        public void displayImg(File file, ImageView iv) {
            if (iv == null || file == null) return;
            Glide.with(iv.getContext()).load(file).into(iv);
        }
        public void displayImg(Uri uri, ImageView iv) {
            if (iv == null || uri == null) return;
            Glide.with(iv.getContext()).load(uri).into(iv);
        }
    }
}

