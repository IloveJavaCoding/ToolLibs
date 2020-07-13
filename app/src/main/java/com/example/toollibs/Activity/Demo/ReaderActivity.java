package com.example.toollibs.Activity.Demo;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.toollibs.Activity.Bean.Books;
import com.example.toollibs.Activity.DataBase.DBHelper;
import com.example.toollibs.Activity.Events.ClickEvent;
import com.example.toollibs.Activity.Events.RefershBookTagEvent;
import com.example.toollibs.Activity.Events.SentTotalLineEvent;
import com.example.toollibs.OverWriteClass.BookView;
import com.example.toollibs.OverWriteClass.OnDoubleClickListener;
import com.example.toollibs.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ReaderActivity extends AppCompatActivity {
    private final String TAG = "READER";
    private LinearLayout layout, layoutControl;
    private ImageView imgBack;
    private TextView tvName, tvProcess;
    private BookView bookView;
    private SeekBar seekBar;

    private DBHelper helper;
    private Books book;
    private int totalLines;

    private final int HIDE_CODE = 0x001;
    private final int UPDATE_CODE = 0x002;
    private final int SHOW_CODE = 0x003;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        getData();
        setLayout();
        init();
        setData();
        setListener();
    }

    private void setLayout() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorEye));
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    private void getData() {
        book = (Books) getIntent().getExtras().getSerializable("book");
        Log.d(TAG, "get book "+ book.getName());
    }

    private void init() {
        layout = findViewById(R.id.layoutTitle);
        layoutControl = findViewById(R.id.layoutControl);
        imgBack = findViewById(R.id.imgBack2);
        tvName = findViewById(R.id.tvBookName2);
        bookView = findViewById(R.id.bookView);
        helper = DBHelper.getInstance(this);

        tvProcess = findViewById(R.id.tvProcess);
        seekBar = findViewById(R.id.sbBook);
    }

    private void setData() {
        tvName.setText(book.getName());
        bookView.setFilePath(book.getPath());
        bookView.setReadMode(BookView.MODE_SLIP);
        bookView.setTag(book.getTag());
        Log.d(TAG, "Last time read: " + book.getTag());

        seekBar.setMax(100);

        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.colorEye), PorterDuff.Mode.SRC_ATOP);//滑块
        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorEyeHalf), PorterDuff.Mode.SRC_ATOP);//进度条

        hideLayout();
    }

    private void setListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvProcess.setText(seekBar.getProgress() + "%");
                bookView.seekTo(seekBar.getProgress()*bookView.getTotalRows()/100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void hideLayout(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(HIDE_CODE);
            }
        }.start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HIDE_CODE:
                    layout.setVisibility(View.INVISIBLE);
                    layoutControl.setVisibility(View.INVISIBLE);
                    break;
                case UPDATE_CODE:
                    updateProcess((int) msg.obj);
                    break;
                case SHOW_CODE:
                    layout.setVisibility(View.VISIBLE);
                    layoutControl.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    //处理事件
    @Subscribe//(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventMainThread(RefershBookTagEvent event){
        Message message = new Message();
        message.what = UPDATE_CODE;
        message.obj = event.getTag()*100/totalLines;
        handler.sendMessage(message);

        helper.updateBook(book.getId(), event.getTag());
        Log.d(TAG, "change tag: " + event.getTag());
    }

    @Subscribe
    public void onEventMainThread(SentTotalLineEvent event){
        totalLines = event.getTotalLines();
        Log.d(TAG, "total lines: " + totalLines);

        Message message = new Message();
        message.what = UPDATE_CODE;
        message.obj = book.getTag()*100/totalLines;
        handler.sendMessage(message);
    }

    @Subscribe
    public void onEventMainThread(ClickEvent event){
        if(event.isShow()){
            //show
            handler.sendEmptyMessage(SHOW_CODE);
        }else{
            handler.sendEmptyMessage(HIDE_CODE);
        }
    }

    private void updateProcess(int rate){
        tvProcess.setText(rate*1f + "%");
        seekBar.setProgress(rate);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //如果你使用postSticky发送事件，那么可以不需要先注册，也能接受到事件，也就是一个延迟注册的过程。
        //EventBus.getDefault().removeAllStickyEvents();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
