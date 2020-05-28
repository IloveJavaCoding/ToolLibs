package com.example.toollibs.Util;

import android.content.Intent;

/**
 * setAction require add <intent-filter> </intent-filter> into <activity> </activity>
 */
public class IntentUtil {
    //<intent-filter>
    //  <action android:name="android.intent.action.DIAL"/>
    //</intent-filter>
    public static Intent go2Dial(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);

        return intent;
    }
}
