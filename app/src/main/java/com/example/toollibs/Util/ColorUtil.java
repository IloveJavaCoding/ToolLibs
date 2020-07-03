package com.example.toollibs.Util;

public class ColorUtil {
    public static int[] getColors(int color, int count, int offset) {
        int[] colors = new int[count];

        int a = (color >> 24) & 0xff;
        int r = (color >> 16) & 0xff;
        int g = (color >> 8) & 0xff;
        int b = (color) & 0xff;

        int c;
        if (g > b) {
            if (r > g) {
                c = r;
            } else {
                c = g;
            }
        } else {
            if (r > b) {
                c = r;
            } else {
                c = b;
            }
        }

        for (int i = 0; i < count; i++) {
            int temp = (c + offset * (i - count / 2)) % 0XFF;

            if (i == 1) {
                colors[i] = createColor(a, r, temp, b);
            } else if (i == 2) {
                colors[i] = createColor(a, r, g, temp);
            } else {
                colors[i] = createColor(a, temp, g, b);
            }
        }

        return colors;
    }

    private static int createColor(int a, int r, int g, int b) {
        return (a << 24) |
                (r << 16) |
                (g << 8) |
                b;
    }

    public static int setAlpha(int color, int alpha) {
//        int a = (color >> 24) & 0xff;
        int r = (color >> 16) & 0xff; //>>右移位16位
        int g = (color >> 8) & 0xff; //& 与
        int b = (color) & 0xff;
        return createColor(alpha, r, g, b);
    }
}
