package com.example.toollibs.Activity.Config;

import com.example.toollibs.Activity.Demo.LiveSource;

public class Constant {
    //string
    public static final String CONFIG_FILE = "config";
    public static final String URL_FILE = "url";

    public static final String TYPE_TOTAL = "total";
    public static final String TYPE_FREE = "free";
    public static final String TYPE_USED = "used";

    public static final String[] LANGUAGE = {"简体中文", "English"};

    //live
    public static final String CITY = "city";
    public static final String FREQUENCY = "frequency";
    public static final String AUD_PID = "audPid";
    public static final String VID_PID = "vidPid";
    public static final String VID_TYPE = "vidType";
    public static final String AUD_TYPE = "audType";

    public static final LiveSource DEFAULT_LIVE_SOURCE =
            new LiveSource("厦门","666000.6875.64",4115,4114,1,2);

    public static final LiveSource[] LIVE_SOURCES = {
            new LiveSource("XiaMen","666000.6875.64",4115,4114,1,2),
            new LiveSource("FuZhou","530000.6875.64",170,171,1,2),
            new LiveSource("SuZhou","490000.6875.64",773,772,9,2),
            new LiveSource("QingDao","722000.6875.64",62,63,5,0),
            new LiveSource("ChangSha","570000.6875.64",1237,1236,3,0),
            new LiveSource("YanCheng","610000.6875.64",59,58,1,0),
            new LiveSource("YangZhou","754000.6875.64",81,73,9,2),
            new LiveSource("ChangZhou1","554000.6875.64",514,513,9,2),
            new LiveSource("ChangZhou2","554000.6875.64",611,610,9,2),
            new LiveSource("HeFei","754000.6875.64",261,255,1,2),
            new LiveSource("WuHu","706000.6875.64",36,33,9,2)
    };

    //int
    public static final int LANGUAGE_CHINA = 0;
    public static final int LANGUAGE_ENGLISH = 1;
    public static final int MODE_PRIVATE = 0X0000;

    public static final int ORIENTATION_LANDSCAPE = 0;
    public static final int ORIENTATION_PORTRAIT = 1;

    //default value
    public static final boolean AUTO_START_DEFAULT = true;
    public static final int LANGUAGE_DEFAULT = 0;

    //key value
    public static final String AUTO_START_KEY = "auto_start";
    public static final String URL_KEY = "auto_start";
    public static final String LANGUAGE_KEY = "auto_start";
}
