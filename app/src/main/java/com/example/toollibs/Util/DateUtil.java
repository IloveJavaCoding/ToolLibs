package com.example.toollibs.Util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static String Date2String(Date date, String type){//yyyy-MM-dd HH:mm:ss
        return new SimpleDateFormat(type).format(date);
    }

    public static Date String2Date(String time, String type){
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
        return Date2String(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String Long2String(long time, String type){
        Date date = new Date(time);
        return Date2String(date, type);
    }

    //get the hh:mm:ss from millSecs
    public static String FormatTime(long millSec){
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
    public static int GetAge(Date birth){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int year1 = Integer.parseInt(Date2String(birth,"yyyy"));

        return year - year1;
    }

    //get the number of day in certain month and year
    public static int GetDayNumByYearMonth(String year, String month) {
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
}
