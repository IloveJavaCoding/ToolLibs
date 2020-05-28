package com.example.toollibs.Activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.toollibs.OverWriteClass.EditTextDelIcon;
import com.example.toollibs.R;
import com.example.toollibs.Util.InternetUtil;

public class DelEditText_Activity extends AppCompatActivity {
    private Button bSearch;
    private EditTextDelIcon dEt;
    private TextView tvShow;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_edit_text_);

        init();
        setListener();
    }

    private void init() {
        bSearch = findViewById(R.id.bSearch);
        dEt = findViewById(R.id.edlET);
        tvShow = findViewById(R.id.tvShow);
        image = findViewById(R.id.image);
    }

    private void setListener() {
        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String input = dEt.getText().toString();
                tvShow.setText("Result: " + input);

                if(input.contains("http://")||input.contains("https://")){
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            //String url = "https://pic.baike.soso.com/ugc/baikepic2/8366/20160729193243-1422261970.jpg/0";
                            Bitmap bitmap = InternetUtil.GetBitmapFromUrl(input);
                            Message msg = Message.obtain();
                            msg.what = 1;
                            msg.obj = bitmap;
                            handler.sendMessage(msg);
                        }
                    }.start();
                }
            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    image.setImageBitmap((Bitmap) msg.obj);
                    break;
            }
        }
    };
}
