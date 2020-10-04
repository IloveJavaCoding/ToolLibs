package com.nepalese.toollibs.Util.Helper;

import android.content.Context;
import android.view.animation.AnimationUtils;

import com.nepalese.toollibs.Activity.Component.VirgoImageView;
import com.nepalese.toollibs.R;

import java.util.List;

/**
 * @author nepalese on 2020/9/8 13:36
 * @usage
 */
public class VirgoImageHelper {
    private static final String TAG = "VirgoImageHelper";

    public static final int ANIM_TYPE_FADE_IN = 0;
    public static final int ANIM_TYPE_FADE_OUT = 1;
    public static final int ANIM_TYPE_LEFT_IN = 2;
    public static final int ANIM_TYPE_LEFT_OUT = 3;
    public static final int ANIM_TYPE_RIGHT_IN = 4;
    public static final int ANIM_TYPE_RIGHT_OUT = 5;
    public static final int ANIM_TYPE_SCALE_IN = 7;
    public static final int ANIM_TYPE_SCALE_OUT = 8;

    public static VirgoImageView createMyImageView(Context context){
        VirgoImageView virgoImageView = new VirgoImageView(context);
        //默认淡入淡出
        setAnimation(virgoImageView, ANIM_TYPE_FADE_IN, ANIM_TYPE_FADE_OUT);
        return virgoImageView;
    }

    public static void setAnimation(VirgoImageView imageView, int animIn, int animOut){
        if(imageView==null){
            return;
        }

        Context context = imageView.getContext();
        imageView.setDuration(0);
        switch (animIn){
            case ANIM_TYPE_FADE_IN:
                imageView.setAnimationIN(AnimationUtils.loadAnimation(context, R.anim.miv_anim_fade_in));
                break;
            case ANIM_TYPE_FADE_OUT:
                imageView.setAnimationIN(AnimationUtils.loadAnimation(context, R.anim.miv_anim_fade_out));
                break;
            case ANIM_TYPE_LEFT_IN:
                imageView.setAnimationIN(AnimationUtils.loadAnimation(context, R.anim.miv_anim_transfor_left_in));
                break;
            case ANIM_TYPE_LEFT_OUT:
                imageView.setAnimationIN(AnimationUtils.loadAnimation(context, R.anim.miv_anim_transfor_left_out));
                break;
            case ANIM_TYPE_RIGHT_IN:
                imageView.setAnimationIN(AnimationUtils.loadAnimation(context, R.anim.miv_anim_transfor_right_in));
                break;
            case ANIM_TYPE_RIGHT_OUT:
                imageView.setAnimationIN(AnimationUtils.loadAnimation(context, R.anim.miv_anim_transfor_right_out));
                break;
            case ANIM_TYPE_SCALE_IN:
                imageView.setAnimationIN(AnimationUtils.loadAnimation(context, R.anim.miv_anim_scale_center_in));
                break;
            case ANIM_TYPE_SCALE_OUT:
                imageView.setAnimationIN(AnimationUtils.loadAnimation(context, R.anim.miv_anim_scale_center_out));
                break;
        }
        switch (animOut){
            case ANIM_TYPE_FADE_IN:
                imageView.setAnimationOUT(AnimationUtils.loadAnimation(context, R.anim.miv_anim_fade_in));
                break;
            case ANIM_TYPE_FADE_OUT:
                imageView.setAnimationOUT(AnimationUtils.loadAnimation(context, R.anim.miv_anim_fade_out));
                break;
            case ANIM_TYPE_LEFT_IN:
                imageView.setAnimationOUT(AnimationUtils.loadAnimation(context, R.anim.miv_anim_transfor_left_in));
                break;
            case ANIM_TYPE_LEFT_OUT:
                imageView.setAnimationOUT(AnimationUtils.loadAnimation(context, R.anim.miv_anim_transfor_left_out));
                break;
            case ANIM_TYPE_RIGHT_IN:
                imageView.setAnimationOUT(AnimationUtils.loadAnimation(context, R.anim.miv_anim_transfor_right_in));
                break;
            case ANIM_TYPE_RIGHT_OUT:
                imageView.setAnimationOUT(AnimationUtils.loadAnimation(context, R.anim.miv_anim_transfor_right_out));
                break;
            case ANIM_TYPE_SCALE_IN:
                imageView.setAnimationOUT(AnimationUtils.loadAnimation(context, R.anim.miv_anim_scale_center_in));
                break;
            case ANIM_TYPE_SCALE_OUT:
                imageView.setAnimationOUT(AnimationUtils.loadAnimation(context, R.anim.miv_anim_scale_center_out));
                break;
        }

    }

    public static void setImgUri(VirgoImageView imageView, List<String> list, int delayIndex){
        if(imageView==null){
            return;
        }

        imageView.setImgUri(list);
        imageView.showNextDelay(getDuration(delayIndex));
    }

    private static long getDuration(int delayIndex) {
        long mDuration;
        switch (delayIndex) {
            case 0:
                mDuration = 3;
                break;
            case 2:
                mDuration = 10;
                break;
            case 3:
                mDuration = 15;
                break;
            case 4:
                mDuration = 30;
                break;
            case 5:
                mDuration = 45;
                break;
            case 6:
                mDuration = 60;
                break;
            case 7:
                mDuration = 90;
                break;
            default:
                mDuration = 5;
                break;
        }
        return mDuration * 1000;
    }
}
