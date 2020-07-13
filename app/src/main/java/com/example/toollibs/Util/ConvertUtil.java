package com.example.toollibs.Util;

import android.content.Context;
import android.os.Build;
import android.util.TypedValue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConvertUtil {
    //
    public static String string2Hex(String str){
        String out = "";
        for(int i=0;i<str.length();i++){
            int ch = str.charAt(i);
            String temp = Integer.toHexString(ch);
            out += temp;
        }

        return out;
    }

    //dp to px
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //sp to px
    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
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
        //1.
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
        //1.
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
}
