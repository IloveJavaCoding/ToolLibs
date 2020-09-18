package com.example.toollibs.Activity.Config;

import com.example.toollibs.Activity.Demo.LiveSource;

public class Constant {
    //string
    public static final String TYPE_TOTAL = "total";
    public static final String TYPE_FREE = "free";
    public static final String TYPE_USED = "used";

    public static final String[] LANGUAGE = {"简体中文", "English"};
    public static final String[] WEEK = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    public static final String[] MONTH = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    //live
    public static final String CITY = "city";
    public static final String FREQUENCY = "frequency";
    public static final String AUD_PID = "audPid";
    public static final String VID_PID = "vidPid";
    public static final String VID_TYPE = "vidType";
    public static final String AUD_TYPE = "audType";

    public static final LiveSource DEFAULT_LIVE_SOURCE =
            new LiveSource("XiaMen","666000.6875.64",4115,4114,1,2);

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
    public static final int LANGUAGE_DEFAULT = 0;
    public static final int READ_MODE_DEFAULT = 1;
    public static final boolean AUTO_START_DEFAULT = true;
    public static final boolean ALARM_STATE_DEFAULT = false;
    public static final String ALARM_TIME_DEFAULT = "click to pick time";
    public static final String ALARM_SOUND_DEFAULT = "Default";
    public static final String AUDIO_DIR_DEFAULT = "/storage/emulated/0/music";
    public static final String URL_DEFAULT = "https://www.sohu.com";

    //action
    public static final String ACTION_RESET_SOUND = "com.example.ToolLibs.reset_sound";
    public static final String ACTION_RESET_SETTING_PAGE = "com.example.ToolLibs.reset_setting_page";

    //weather
    public static final int WEATHER_SUNNY_CODE = 100;
    public static final int WEATHER_CLOUDY_CODE = 101;
    public static final int WEATHER_FEW_CLOUDS_CODE = 102;
    public static final int WEATHER_PARTLY_CLOUDY_CODE = 103;
    public static final int WEATHER_OVERCAST_CODE = 104;
    public static final int WEATHER_WINDY_CODE = 200;
    public static final int WEATHER_CALM_CODE = 201;
    public static final int WEATHER_LIGHT_BREEZE_CODE = 202;
    public static final int WEATHER_MODERATE_BREEZE_CODE = 203;
    public static final int WEATHER_FRESH_BREEZE_CODE = 204;
    public static final int WEATHER_STRONG_BREEZE_CODE = 205;
    public static final int WEATHER_HIGH_WIND_CODE = 206;
    public static final int WEATHER_GALE_CODE = 207;
    public static final int WEATHER_STRONG_GALE_CODE = 208;
    public static final int WEATHER_STORM_CODE = 209;
    public static final int WEATHER_VIOLENT_STORM_CODE = 210;
    public static final int WEATHER_HURRICANE_CODE = 211;
    public static final int WEATHER_TORNADO_CODE = 212;
    public static final int WEATHER_TROPICAL_STORM_CODE = 213;
    public static final int WEATHER_SHOWER_RAIN_CODE = 300;
    public static final int WEATHER_HEAVY_SHOWER_RAIN_CODE = 301;
    public static final int WEATHER_THUNDERSHOWER_CODE = 302;
    public static final int WEATHER_HEAVY_THUNDERSHOWER_CODE = 303;
    public static final int WEATHER_THUNDERSHOWER_WITH_HAIL_CODE = 304;
    public static final int WEATHER_LIGHT_RAIN_CODE = 305;
    public static final int WEATHER_MODERATE_RAIN_CODE = 306;
    public static final int WEATHER_HEAVY_RAIN_CODE = 307;
    public static final int WEATHER_EXTREME_RAIN_CODE = 308;
    public static final int WEATHER_DRIZZLE_RAIN_CODE = 309;
    public static final int WEATHER_STORM_RAIN_CODE = 310;
    public static final int WEATHER_HEAVY_STORM_CODE = 311;
    public static final int WEATHER_SEVERE_STORM_CODE = 312;
    public static final int WEATHER_FREEZING_RAIN_CODE = 313;
    public static final int WEATHER_LIGHT_TO_MODERATE_RAIN_CODE = 314;
    public static final int WEATHER_MODERATE_TO_HEAVY_RAIN_CODE = 315;
    public static final int WEATHER_HEAVY_RAIN_TO_STORM_CODE = 316;
    public static final int WEATHER_STORM_TO_HEAVY_STORM_CODE = 317;
    public static final int WEATHER_HEAVY_TO_SEVERE_STORM_CODE = 318;
    public static final int WEATHER_RAIN_CODE = 399;
    public static final int WEATHER_LIGHT_SNOW_CODE = 400;
    public static final int WEATHER_MODERATE_SNOW_CODE = 401;
    public static final int WEATHER_HEAVY_SNOW_CODE = 402;
    public static final int WEATHER_SNOWSTORM_CODE = 403;
    public static final int WEATHER_SLEET_CODE = 404;
    public static final int WEATHER_RAIN_AND_SNOW_CODE = 405;
    public static final int WEATHER_SHOWER_SNOW_CODE = 406;
    public static final int WEATHER_SNOW_FLURRY_CODE = 407;
    public static final int WEATHER_LIGHT_TO_MODERATE_SNOW_CODE = 408;
    public static final int WEATHER_MODERATE_TO_HEAVY_SNOW_CODE = 409;
    public static final int WEATHER_HEAVY_SNOW_TO_SNOWSTORM_CODE = 410;
    public static final int WEATHER_SNOW_CODE = 499;
    public static final int WEATHER_MIST_CODE = 500;
    public static final int WEATHER_FOGGY_CODE = 501;
    public static final int WEATHER_HAZE_CODE = 502;
    public static final int WEATHER_SAND_CODE = 503;
    public static final int WEATHER_DUST_CODE = 504;
    public static final int WEATHER_DUSTSTORM_CODE = 507;
    public static final int WEATHER_SANDSTORM_CODE = 508;
    public static final int WEATHER_DENSE_FOG_CODE = 509;
    public static final int WEATHER_STRONG_FOG_CODE = 510;
    public static final int WEATHER_MODERATE_HAZE_CODE = 511;
    public static final int WEATHER_HEAVY_HAZE_CODE = 512;
    public static final int WEATHER_SEVERE_HAZE_CODE = 513;
    public static final int WEATHER_HEAVY_FOG_CODE = 514;
    public static final int WEATHER_EXTRA_HEAVY_FOG_CODE = 515;
    public static final int WEATHER_HOT_CODE = 900;
    public static final int WEATHER_COLD_CODE = 901;
    public static final int WEATHER_UNKNOWN_CODE = 999;
}
