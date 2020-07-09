package com.example.toollibs.Activity.Demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.toollibs.Activity.Bean.Books;
import com.example.toollibs.Activity.DataBase.DBHelper;
import com.example.toollibs.Activity.Events.RefershBookTagEvent;
import com.example.toollibs.OverWriteClass.BookView;
import com.example.toollibs.OverWriteClass.OnDoubleClickListener;
import com.example.toollibs.R;
import com.example.toollibs.Util.SystemUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ReaderActivity extends AppCompatActivity {
    private final String TAG = "READER";
    private LinearLayout layout;
    private ImageView imgBack;
    private TextView tvName;
    private BookView bookView;

    private DBHelper helper;
    private Books book;
    private final int HIDE_CODE = 0x001;
    private int w,h;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        getData();
        init();
        setData();
        setListener();
    }

    private void getData() {
        book = (Books) getIntent().getExtras().getSerializable("book");
        Log.d(TAG, "get book "+ book.getName());

        DisplayMetrics dm = SystemUtil.GetScreenDM(this);
        w = dm.widthPixels;
        h = dm.heightPixels;
    }

    private void init() {
        layout = findViewById(R.id.layoutTitle);
        imgBack = findViewById(R.id.imgBack2);
        tvName = findViewById(R.id.tvBookName2);
        bookView = findViewById(R.id.bookView);
        helper = DBHelper.getInstance(this);
    }

    private void setData() {
        tvName.setText(book.getName());
        bookView.setFilePath(book.getPath());
        bookView.seekTo(book.getTag());
        bookView.getWindowWH(w, h);
    }

    private void setListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bookView.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
            @Override
            public void onDoubleClick() {
                layout.setVisibility(View.VISIBLE);
                hideLayout();
            }
        }));
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
                    break;
            }
        }
    };

    //处理事件
    @Subscribe//(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventMainThread(RefershBookTagEvent event){
        helper.updateBook(book.getId(), event.getTag());
        Log.d(TAG, "change tag: " + event.getTag());
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
