package com.example.toollibs.Util;

public class ConvertUtil {
    //
    public static String String2Hex(String str){
        String out = "";
        for(int i=0;i<str.length();i++){
            int ch = str.charAt(i);
            String temp = Integer.toHexString(ch);
            out += temp;
        }

        return out;
    }
}
