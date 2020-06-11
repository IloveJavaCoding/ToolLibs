package com.example.toollibs.Util;

import android.content.Intent;

/**
 * setAction require add <intent-filter> </intent-filter> into <activity> </activity>
 */
public class IntentUtil {
    //start activity


    //open file use third app


    //read file return file path
    public static void readImageFile(int requestCode){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(i, requestCode);
    }




    //<intent-filter>
    //  <action android:name="android.intent.action.DIAL"/>
    //</intent-filter>
    public static Intent go2Dial(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);

        return intent;
    }
}
