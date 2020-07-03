package com.example.toollibs.Util;

import android.content.Context;

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
}
