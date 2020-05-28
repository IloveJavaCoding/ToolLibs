package com.example.toollibs.OverWriteClass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.toollibs.R;
import com.example.toollibs.Util.BitmapUtil;

import java.util.Timer;
import java.util.TimerTask;

public class PosterView extends RelativeLayout {
    private final int[] IMAGE_IDS = {R.drawable.img_ad, R.drawable.img_ad1};
    private Bitmap[] IMAGES;

    private int DOT_IV_SIZE = 15;
    private int DOT_IV_MARGIN = 7;// 点点的间距
    private int SCROLL_PERIOD_MILLISECONDS = 5000;//滑动间隔

    private Timer timer = null;
    private ViewPager viewPager = null;
    private MyPagerAdapter adapter = null;
    private ImageView[] posterIvs = null; // 广告图片数组
    private LinearLayout dotLayout = null;// 装点点的容器
    private ImageView[] dotIvs = null; // 装点点的数组

    private int currentPosition = 0;//当前显示的页面位置
    private boolean isChange = false;// 页面是否改变
    private boolean isActionDown = false;//手指是否已经按下
    private long actionUpTime = 0;// 手指按下的时间，大于1秒的时候进行切换

    //==================================================================
    public PosterView(Context context) {
        super(context);
        init(context);
    }

    public PosterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PosterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        IMAGES = BitmapUtil.getBitmapsFromRes(context, IMAGE_IDS);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View parent = inflater.inflate(R.layout.layout_poster_view, this, true);
        viewPager = parent.findViewById(R.id.viewpager);
        dotLayout = parent.findViewById(R.id.dot_layout);
        adapter = new MyPagerAdapter();
        viewPager.setAdapter(adapter);

        setPosterImages(context, IMAGES);
        setListener();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isActionDown || viewPager == null || posterIvs == null || posterIvs.length <= 1) {
                    return;
                }
                if(actionUpTime != 0) {
                    if(System.currentTimeMillis() - actionUpTime > SCROLL_PERIOD_MILLISECONDS) {
                        actionUpTime = 0;
                    } else {
                        return;
                    }
                }
                int count = posterIvs.length;
                int nextItem = (viewPager.getCurrentItem() + 1) % count;
                handler.sendEmptyMessage(nextItem);
            }
        }, 1000, SCROLL_PERIOD_MILLISECONDS);
    }

    private void setPosterImages(Context context, Bitmap[] images) {
        if (images == null || images.length == 0) {
            return;
        }
        dotLayout.removeAllViews();
        if(images.length == 1) {
            //如果只有一张图则不允许滑动
            posterIvs = new ImageView[1];
            posterIvs[0] = new ImageView(context);
            posterIvs[0].setImageBitmap(images[0]);
        } else {
            //如果有多张图,则运行循环滚动,并在图片下方显示指示点
            posterIvs = new ImageView[images.length + 2];
            dotIvs = new ImageView[images.length];
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DOT_IV_SIZE, DOT_IV_SIZE);
            params.setMargins(DOT_IV_MARGIN, DOT_IV_MARGIN, DOT_IV_MARGIN, DOT_IV_MARGIN);

            for (int i = 0, resid, dotIndex; i < posterIvs.length; i++) {
                posterIvs[i] = new ImageView(context);
                if (i == 0) {
                    posterIvs[i].setImageBitmap(images[images.length - 1]);
                } else if (i == (posterIvs.length - 1)) {
                    posterIvs[i].setImageBitmap(images[0]);
                } else {
                    dotIndex = i - 1;
                    posterIvs[i].setImageBitmap(images[dotIndex]);
                    dotIvs[dotIndex] = new ImageView(context);
                    if (dotIndex == 0) {
                        resid = R.drawable.shape_dot_focus;
                    } else {
                        resid = R.drawable.shape_dot_unfocus;
                    }
                    dotIvs[dotIndex].setBackgroundResource(resid);
                    dotLayout.addView(dotIvs[dotIndex], params);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void setListener(){
        viewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isActionDown = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        isActionDown = false;
                        break;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if(posterIvs.length <= 1) {
                    return;
                }
                currentPosition = position;
                for (int i = 0, resid = 0; i < dotIvs.length; i++) {
                    if (i == (position - 1) || (position == 0 && i == (dotIvs.length - 1))) {
                        resid = R.drawable.shape_dot_focus;
                    } else {
                        resid = R.drawable.shape_dot_unfocus;
                    }
                    dotIvs[i].setBackgroundResource(resid);
                }
                if (currentPosition == 0) {
                    currentPosition = posterIvs.length - 2;
                    isChange = true;
                } else if (currentPosition == posterIvs.length - 1) {
                    currentPosition = 1;
                    isChange = true;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}

            @Override
            public void onPageScrollStateChanged(int state) {
                // SCROLL_STATE_IDLE: pager处于空闲状态
                // SCROLL_STATE_DRAGGING： pager处于正在拖拽中
                // SCROLL_STATE_SETTLING： pager正在自动沉降，相当于松手后，pager恢复到一个完整pager的过程
                if (isChange && state == ViewPager.SCROLL_STATE_IDLE) {
                    viewPager.setCurrentItem(currentPosition, false);
                }
            }
        });
    }

    //====================================================================
    public void setIMAGES(Bitmap[] IMAGES) {
        this.IMAGES = IMAGES;
    }

    public void setDOT_IV_SIZE(int DOT_IV_SIZE) {
        this.DOT_IV_SIZE = DOT_IV_SIZE;
    }

    public void setDOT_IV_MARGIN(int DOT_IV_MARGIN) {
        this.DOT_IV_MARGIN = DOT_IV_MARGIN;
    }

    public void setSCROLL_PERIOD_MILLISECONDS(int SCROLL_PERIOD_MILLISECONDS) {
        this.SCROLL_PERIOD_MILLISECONDS = SCROLL_PERIOD_MILLISECONDS;
    }

    //=====================================================================

    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            viewPager.setCurrentItem(msg.what, true);
        }
    };

    class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if (posterIvs == null || posterIvs.length == 0) {
                return 0;
            }
            return posterIvs.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = posterIvs[position];
            container.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(posterIvs[position]);
        }
    }
}
