package com.example.toollibs.Util;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

public class VolumeUtil {
    private static final String TAG = "VOLUME_UTIL";

    public static  int getVolume(Context context){
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return manager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public static void setVolume(Context context, float value){
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (manager!=null){
            int maxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int volume = (int) (value/ 100 * maxVolume);
            manager.setStreamVolume(AudioManager.STREAM_MUSIC,volume,AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        }else {
            Log.w(TAG, "AudioManager == null");
        }
    }

    public static void bootResetVolume(Context context) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (manager != null) {
            int volume = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (volume != manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) {
                manager.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE,
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                manager.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER,
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            } else {
                manager.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER,
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                manager.adjustStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE,
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }
        }
    }
}
