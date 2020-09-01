package com.example.toollibs.Util;

import android.app.AlarmManager;
import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static String date2String(Date date, String type){//yyyy-MM-dd HH:mm:ss
        return new SimpleDateFormat(type).format(date);
    }

    public static Date string2Date(String time, String type){
        SimpleDateFormat format = new SimpleDateFormat(type);
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getCurTime(){
        Date date = Calendar.getInstance().getTime();
        return date2String(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String long2String(long time, String type){
        Date date = new Date(time);
        return date2String(date, type);
    }

    //get the hh:mm:ss from millSecs
    public static String formatTime(long millSec){
        int seconds =(int) millSec/1000;
        int hour, sec, min;
        String hours, secs, mins;

        if(seconds>60*60){//1 hour
            hour = seconds/3600;
            min = (seconds - hour*3600)/60;
            sec = seconds - hour*3600 - min*60;

            if(hour<10){
                hours = "0" + hour;
            }else{
                hours = Integer.toString(hour);
            }
            if(min<10){
                mins = "0" + min;
            }else{
                mins = Integer.toString(min);
            }
            if(sec<10){
                secs = "0" + sec;
            }else{
                secs = Integer.toString(sec);
            }

            return hours+":"+mins+":"+secs;
        }
        else{
            min = seconds/60;
            sec = seconds - min*60;
            if(min<10){
                mins = "0" + min;
            }else{
                mins = Integer.toString(min);
            }
            if(sec<10){
                secs = "0" + sec;
            }else{
                secs = Integer.toString(sec);
            }

            return mins+":"+secs;
        }
    }

    //calculate the age base on the birthday
    public static int getAge(Date birth){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int year1 = Integer.parseInt(date2String(birth,"yyyy"));

        return year - year1;
    }

    //get the number of day in certain month and year
    public static int getDayNumByYearMonth(String year, String month) {
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM");
        Calendar rightNow = Calendar.getInstance();

        try {
            rightNow.setTime(simpleDate.parse(year + "/" + month));
        } catch (ParseException var5) {
            var5.printStackTrace();
        }

        return rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int[] getYMDHMS_Date(Date time) {
        int[] arr = new int[7];
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);

        arr[0] = calendar.get(Calendar.YEAR);
        arr[1] = calendar.get(Calendar.MONTH)+1;
        arr[2] = calendar.get(Calendar.DAY_OF_MONTH);

        arr[3] = calendar.get(Calendar.HOUR_OF_DAY);
        arr[4] = calendar.get(Calendar.MINUTE);
        arr[5] = calendar.get(Calendar.SECOND);

        arr[6] = calendar.get(Calendar.DAY_OF_WEEK);

        return arr;
    }

    //===================设置系统时间，时区（须系统权限）============================
    public static void setTime(long mills){
        SystemClock.setCurrentTimeMillis(mills);
    }

    public static void setTimeZone(Context context, String timeZone){
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setTimeZone(timeZone);
    }
}
