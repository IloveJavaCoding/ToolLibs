package com.nepalese.toollibs.Util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConvertUtil {
    //字符串转换为16进制字符串
    public static String string2Hex(String str){
        String out = "";
        for(int i=0;i<str.length();i++){
            int ch = str.charAt(i);
            String temp = Integer.toHexString(ch);
            out += temp;
        }

        return out;
    }

    //转换十六进制编码为字符串
    public static String toStringHex(String str) {
        if ("0x".equals(str.substring(0, 2)))
        {
            str = str.substring(2);
        }
        byte[] baKeyword = new byte[str.length() / 2];
        for (int i = 0; i < baKeyword.length; i++)
        {
            try
            {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(str.substring(
                        i * 2, i * 2 + 2), 16));
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        try
        {
            str = new String(baKeyword, "utf-8");//UTF-16le:Not
        } catch (Exception e1)
        {
            e1.printStackTrace();
        }
        return str;
    }

    //dp to px
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    //sp to px
    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * 根据屏幕密度, 将dp(device independent pixels 无关像素点) 转 px( 像素)
     */
    public static int dp2px(Context context, float dp) {
        if (context != null) {
            Resources mResources = context.getResources();
            if (mResources != null) {
                DisplayMetrics metrics = mResources.getDisplayMetrics();
                if (metrics != null) {
                    return (int) (dp * (metrics.densityDpi / 160f));
                }
            }
        }
        return 0;
    }
    /**
     * 根据屏幕密度, 将 px( 像素) 转 dp(device independent pixels 无关像素点)
     */
    public static int px2dp(Context context, float px) {
        if (context != null) {
            Resources mResources = context.getResources();
            if (mResources != null) {
                DisplayMetrics metrics = mResources.getDisplayMetrics();
                if (metrics != null) {
                    return (int) (px / (metrics.densityDpi / 160f));
                }
            }
        }
        return 0;
    }

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static float px2sp1(Context context, int size) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, size, context.getResources().getDisplayMetrics());
    }

    public static float sp2px1(Context context, int size) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, context.getResources().getDisplayMetrics());
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px(像素)
     */
    public static int sp2pix(Context context, float sp) {
        if (context != null ) {
            Resources mResources = context.getResources();
            if (mResources != null) {
                DisplayMetrics mMetrics = mResources.getDisplayMetrics();
                if (mMetrics != null) {
                    final float scale = mMetrics.scaledDensity;
                    return (int) (sp  * sp + 0.5f);
                }
            }
        }
        return 0;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     */
    public static int px2sp(Context context, float px) {
        if (context != null ) {
            Resources mResources = context.getResources();
            if (mResources != null) {
                DisplayMetrics mMetrics = mResources.getDisplayMetrics();
                if (mMetrics != null) {
                    final float scale = mMetrics.scaledDensity;
                    return (int) (px / scale + 0.5f);
                }
            }
        }
        return 0;
    }

    //温度： 华氏 ~ 摄氏度 Fahrenheit, Centigrade
    //C=（5/9）（F-32）
    public static float fahrenheit2Centigrade(float f){
        return (5/9) * (f-32f);
    }

    public static float centigrade2Fahrenheit(float c){
        return c*1.8f + 32;
    }

    //进制转换
    //16 <--> 10
    public static int hex2Decimal(String hex){
        int d = 0;
        //1.系统方法
        //d = Integer.valueOf(hex,16);

        //2. ff -> 255
        for(int i=0; i<hex.length(); i++){
            d += char2Int(hex.charAt(i))*(2<<(4*(hex.length()-1)-1));//16^hex.length()-1
        }

        return d;
    }

    private static int char2Int(char c){
        if(c>'f'||c>'F'){
            return -1;
        }
        switch (c){
            case 'f':
            case 'F':
                return 15;
            case 'e':
            case 'E':
                return 14;
            case 'd':
            case 'D':
                return 13;
            case 'c':
            case 'C':
                return 12;
            case 'b':
            case 'B':
                return 11;
            case 'a':
            case 'A':
                return 10;
            default:
                return c;
        }
    }

    public static String decimal2Hex(int d){
        String hex = "";
        List<Integer> m = new ArrayList<>();
        //1.系统方法
        //hex = Integer.toHexString(d);

        //2. a -> 商; m -> 余;
        int a = d;
        while(a>0){
            m.add(d%16);
            a = d/16;
        }

        for(int i=m.size()-1; i>=0; i--){
            hex += int2Str(m.get(i));
        }

        return hex;
    }

    private static String int2Str(Integer a) {//<15
        if(a>15){
            return "";
        }
        switch (a){
            case 15:
                return "f";
            case 14:
                return "e";
            case 13:
                return "d";
            case 12:
                return "c";
            case 11:
                return "b";
            case 10:
                return "a";
            default:
                return a.toString();
        }
    }

    //=======================array & list==================================
    // int[] 转 List<Integer>
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Object> int2List(int[] data) {
        List<Object> list = Arrays.stream(data).boxed().collect(Collectors.toList());
        return list;
    }

    // String[] 转 List<String>
    public static List<String> string2List(String[] strs){
        return Arrays.asList(strs);
    }

    //==================================gbk ? utf-8=========================
    public static Boolean isUtf8(File file) {
        boolean isUtf8 = true;
        byte[] buffer = FileUtil.readBytes(file.getPath());
        int end = buffer.length;
        for (int i = 0; i < end; i++) {
            byte temp = buffer[i];
            if ((temp & 0x80) == 0) {// 0xxxxxxx
                continue;
            } else if ((temp & 0xC0) == 0xC0 && (temp & 0x20) == 0) {// 110xxxxx 10xxxxxx
                if (i + 1 < end && (buffer[i + 1] & 0x80) == 0x80 && (buffer[i + 1] & 0x40) == 0) {
                    i = i + 1;
                    continue;
                }
            } else if ((temp & 0xE0) == 0xE0 && (temp & 0x10) == 0) {// 1110xxxx 10xxxxxx 10xxxxxx
                if (i + 2 < end && (buffer[i + 1] & 0x80) == 0x80 && (buffer[i + 1] & 0x40) == 0
                        && (buffer[i + 2] & 0x80) == 0x80 && (buffer[i + 2] & 0x40) == 0) {
                    i = i + 2;
                    continue;
                }
            } else if ((temp & 0xF0) == 0xF0 && (temp & 0x08) == 0) {// 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
                if (i + 3 < end && (buffer[i + 1] & 0x80) == 0x80 && (buffer[i + 1] & 0x40) == 0
                        && (buffer[i + 2] & 0x80) == 0x80 && (buffer[i + 2] & 0x40) == 0
                        && (buffer[i + 3] & 0x80) == 0x80 && (buffer[i + 3] & 0x40) == 0) {
                    i = i + 3;
                    continue;
                }
            }
            isUtf8 = false;
            break;
        }
        return isUtf8;
    }

    /**
     * 如果文件是gbk编码或者gb2312返回true,反之false
     * @param file
     * @return
     */
    public static Boolean isGbk(File file) {
        boolean isGbk = true;
        byte[] buffer = FileUtil.readBytes(file.getPath());
        int end = buffer.length;
        for (int i = 0; i < end; i++) {
            byte temp = buffer[i];
            if ((temp & 0x80) == 0) {
                continue;// B0A1-F7FE//A1A1-A9FE
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if ((Byte.toUnsignedInt(temp) < 0xAA && Byte.toUnsignedInt(temp) > 0xA0)
                        || (Byte.toUnsignedInt(temp) < 0xF8 && Byte.toUnsignedInt(temp) > 0xAF)) {
                    if (i + 1 < end) {
                        if (Byte.toUnsignedInt(buffer[i + 1]) < 0xFF && Byte.toUnsignedInt(buffer[i + 1]) > 0xA0
                                && Byte.toUnsignedInt(buffer[i + 1]) != 0x7F) {
                            i = i + 1;
                            continue;
                        }
                    } // 8140-A0FE
                } else if (Byte.toUnsignedInt(temp) < 0xA1 && Byte.toUnsignedInt(temp) > 0x80) {
                    if (i + 1 < end) {
                        if (Byte.toUnsignedInt(buffer[i + 1]) < 0xFF && Byte.toUnsignedInt(buffer[i + 1]) > 0x3F
                                && Byte.toUnsignedInt(buffer[i + 1]) != 0x7F) {
                            i = i + 1;
                            continue;
                        }
                    } // AA40-FEA0//A840-A9A0
                } else if ((Byte.toUnsignedInt(temp) < 0xFF && Byte.toUnsignedInt(temp) > 0xA9)
                        || (Byte.toUnsignedInt(temp) < 0xAA && Byte.toUnsignedInt(temp) > 0xA7)) {
                    if (i + 1 < end) {
                        if (Byte.toUnsignedInt(buffer[i + 1]) < 0xA1 && Byte.toUnsignedInt(buffer[i + 1]) > 0x3F
                                && Byte.toUnsignedInt(buffer[i + 1]) != 0x7F) {
                            i = i + 1;
                            continue;
                        }
                    }
                }
            }
            isGbk = false;
            break;
        }
        return isGbk;
    }

    //========================================================================
    public static String getFormatSize(float size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return "0Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    //=============================2/4 json====================================
    //需添加gson.jar
    public static Object getObject(String json, Type class1) {
        try {
            Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            Object object = gson.fromJson(json.trim(), class1);
            return object;
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static String toJson(Object object) {
        try {
            Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            return gson.toJson(object);
        } catch (Exception var2) {
            return "";
        }
    }
}
