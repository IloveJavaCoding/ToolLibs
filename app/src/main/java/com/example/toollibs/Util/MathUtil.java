package com.example.toollibs.Util;

import java.math.BigDecimal;

public class MathUtil {
    //get random number
    public static double getRandomNum(int a, int b) {
        return (Math.random() * (b - a)) + a;
    }

    public static int getRandomNumInt(int a, int b) {
        return (int) (Math.random() * (b - a)) + a;
    }

    //set the scale of decimal
    public static double setDecimalDot(double value, int scale) {
        BigDecimal bd = new BigDecimal(value);
        return bd.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        //String .format("%.2f",d);
    }
}
