package com.nepalese.toollibs.Activity.Demo;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nepalese.toollibs.Activity.ComponentThird.EffectView;
import com.nepalese.toollibs.R;
import com.nepalese.toollibs.Util.BitmapUtil;

public class Demo_Effect_View_Activity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgBg, imgAlbum;
    private EffectView effectView;
    private Button bPlay, bColor, bAncient, bElectronic, bSurround, bLonely;

    private Bitmap bitmap;
    private MediaPlayer mediaPlayer;
    private ObjectAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_effect_view);

        init();
        setData();
        setListener();
    }

    private void init() {
        imgBg = findViewById(R.id.imgBG);
        imgAlbum = findViewById(R.id.imgAlbum);
        effectView = findViewById(R.id.effectView);

        bPlay = findViewById(R.id.play);
        bColor = findViewById(R.id.palette);

        bAncient = findViewById(R.id.ancient);
        bElectronic = findViewById(R.id.electronic);
        bSurround = findViewById(R.id.surround);
        bLonely = findViewById(R.id.lonely);
    }

    private void setData() {
        //init player
        mediaPlayer = MediaPlayer.create(this, R.raw.youth_meng);
        mediaPlayer.setLooping(true);
        bitmap = BitmapUtil.getBitmapFromRes(this, R.drawable.img_portrait);
        Log.d("tag", "bitmap: "+ bitmap.getWidth());
        //set bg -- blur
        imgBg.setImageBitmap(BitmapUtil.blurBitmap(this, bitmap, 20));
        //set album and get circle
        imgAlbum.setImageBitmap(BitmapUtil.getCircleBitmap(bitmap));
        //imgAlbum.setImageBitmap(bitmap);
        animator = BitmapUtil.rotateIV(imgAlbum, 5000);

        //default effect
        effectView.setAncientEffectDrawable();
        setPalette(bitmap);
    }

    private void setPalette(Bitmap bitmap){
        Palette.PaletteAsyncListener listener = new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                int vibrant = palette.getVibrantColor(0x000000);
                int vibrantLight = palette.getLightVibrantColor(0x000000);
                int vibrantDark = palette.getDarkVibrantColor(0x000000);
                int muted = palette.getMutedColor(0x000000);
                int mutedLight = palette.getLightMutedColor(0x000000);
                int mutedDark = palette.getDarkMutedColor(0x000000);
                effectView.setColor(mutedLight);
                effectView.setColors(new int[]{mutedLight, muted, vibrantLight, vibrant});

            }
        };
        Palette.from(bitmap).generate(listener);
    }

    private void setListener() {
        bPlay.setOnClickListener(this);
        bColor.setOnClickListener(this);

        bAncient.setOnClickListener(this);
        bElectronic.setOnClickListener(this);
        bSurround.setOnClickListener(this);
        bLonely.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                play();
                break;
            case R.id.palette:
                setPalette(bitmap);
                break;
            case R.id.ancient:
                effectView.setAncientEffectDrawable();
                break;
            case R.id.electronic:
                effectView.setElectronicEffectDrawable();
                break;
            case R.id.surround:
                effectView.setSurroundEffectDrawable();
                break;
            case R.id.lonely:
                effectView.setLonelyEffectDrawable();
                break;
        }
    }

    private void play() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            animator.pause();
        }else{
            mediaPlayer.start();
            animator.start();
        }
    }
}
