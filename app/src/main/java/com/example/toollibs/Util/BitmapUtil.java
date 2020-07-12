package com.example.toollibs.Util;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.toollibs.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BitmapUtil {
    //get bitmap read from file
    public static Bitmap GetBitmapFromFile(String path){
        if(TextUtils.isEmpty(path)){
            return null;
        }
        File file = new File(path);
        if(!file.exists()){
            return null;
        }

        FileInputStream inputStream;
        Bitmap bitmap = null;
        try {
            inputStream = new FileInputStream(path);
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    //save bitmap to local
    public static void Bitmap2Local(Bitmap bitmap, String path, String fileName){
        File file = new File(path+"/"+fileName);
        if(file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Bitmap2Local(Bitmap bitmap, String path){
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //get bitmap from res file
    public static Bitmap getBitmapFromRes(Context context, int id){
        return BitmapFactory.decodeResource(context.getResources(), id);
    }

    public static Bitmap[] getBitmapsFromRes(Context context, int[] id){
        Bitmap[] bitmaps = new Bitmap[id.length];
        for(int i=0; i<id.length; i++){
            bitmaps[i] = BitmapFactory.decodeResource(context.getResources(), id[i]);
        }
        return bitmaps;
    }

    //string <--> bitmap
    public static String bitmap2String(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();

        try {
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static Bitmap string2Bitmap(String data){
        byte[] bits = Base64.decode(data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bits,0, bits.length);
    }

    //operation on bitmap
    //rotate
    public static Bitmap rotateBitmap(Bitmap bitmap, final int degree){
        if(degree%360 == 0){
            return bitmap;
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setRotate(degree, w/2, h/2);
        Bitmap newBm = Bitmap.createBitmap(bitmap,0,0, w, h, matrix,true);//bg

        if(newBm!=bitmap){
            bitmap.recycle();
        }

        return newBm;
    }

    public static Bitmap rotateBitmap2(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        float targetX, targetY;
        if (orientationDegree == 90) {
            targetX = bm.getHeight();
            targetY = 0;
        } else {
            targetX = bm.getHeight();
            targetY = bm.getWidth();
        }

        final float[] values = new float[9];
        m.getValues(values);

        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];

        m.postTranslate(targetX - x1, targetY - y1);
        Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        Canvas canvas = new Canvas(bm1);
        canvas.drawBitmap(bm, m, paint);

        return bm1;
    }

    public static Bitmap getCircleBitmap(Bitmap bitmap){
        bitmap = cutBitmap(bitmap);

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap aimBm = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(aimBm);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0,0, w, h);//w,h
        final RectF rectF = new RectF(rect);
        float roundP = w>h ? h/2.0f : w/2.0f;

        paint.setAntiAlias(true);
        canvas.drawARGB(0,0,0,0);
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(rectF,roundP,roundP,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        final Rect src = new Rect(0,0,w,h);//w,h
        canvas.drawBitmap(bitmap, src, rect, paint);

        return aimBm;
    }

    //cut bitmap w = h
    public static Bitmap cutBitmap(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if(width==height){
            return bitmap;
        }

        int len;
        int offset;
        Bitmap newBm;
        if(width>height){
            len = height;
            offset= (width-len)/2;
            newBm = Bitmap.createBitmap(bitmap,offset,0,len, len, null,true);
            bitmap.recycle();
            return newBm;
        }else{
            len = width;
            offset= (height-len)/2;
            newBm = Bitmap.createBitmap(bitmap,0,offset,len, len, null,true);
            bitmap.recycle();
            return newBm;
        }
    }

    //zoom
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleW = (float)w/width ;
        float scaleH = (float)h/height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleW,scaleH);

        Bitmap newBm = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        bitmap.recycle();

        return newBm;
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, final float scale){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.postScale(scale,scale);

        Bitmap newBm = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        bitmap.recycle();

        return newBm;
    }

    //scale
    public static Bitmap scaleBitmap(Bitmap bitmap, int aimW, int aimH, boolean isRecycle){
        if(bitmap.getWidth()==aimW && bitmap.getHeight()==aimH){
            return bitmap;
        }

        Bitmap aimBm = Bitmap.createScaledBitmap(bitmap,aimW,aimH,false);
        if(isRecycle && aimBm!=bitmap){
            bitmap.recycle();
        }

        return aimBm;
    }

    //blur bitmap
    private static final float BITMAP_SCALE = 0.4f;
    //25f是最大模糊度
    public static Bitmap blurBitmap(Context context, Bitmap bitmap, float degree){
        // 计算图片缩小后的长宽
        int width = Math.round(bitmap.getWidth() * BITMAP_SCALE);
        int height = Math.round(bitmap.getHeight() * BITMAP_SCALE);

        // 将缩小后的图片做为预渲染的图片
        Bitmap inputBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        // 创建一张渲染后的输出图片
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context);
        // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(degree);
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut);

        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    //使用时，将图片缩小后再使用，避免oom
    public static Bitmap fastBlurBitmap(Context context, Bitmap sentBitmap, int radius) {//200
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        if (radius < 1) {
            return (null);
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

    //get color in the bitmap
    public static List<Integer> colorCapture4Bitmap(Bitmap bitmap){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pixels = new int[w * h];
        HashMap<Integer, Integer> colors = new HashMap<>();
        TreeMap<Integer, Integer> sortedColors = new TreeMap<>();
        List<Integer> result = new ArrayList<>();
        bitmap.getPixels(pixels, 0, w, 0, 0, w, h);
        for (int pixel : pixels) {
            Integer num = colors.get(pixel);
            if (num == null) {
                colors.put(pixel, 1);
            } else {
                num += 1;
                colors.put(pixel, num);
            }
        }
        for (Map.Entry<Integer, Integer> entry : colors.entrySet()) {
            sortedColors.put(entry.getValue(), entry.getKey());
        }
        for (Map.Entry<Integer, Integer> entry : sortedColors.entrySet()) {
            result.add(entry.getValue());
            Log.d("bitmapUtil", "run: color:"+entry.getValue()+",count:"+entry.getKey());
        }

        return result;
    }

    /**
     * set alpha of image
     * @param bitmap
     * @param alpha 0-100
     * @return
     */
    public static Bitmap setAlpha(Bitmap bitmap, int alpha){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pixels = new int[w * h];
        bitmap.getPixels(pixels, 0, w, 0, 0, w, h);

        alpha = alpha / 100 * 255;
        for(int i=0; i<pixels.length; i++){
            pixels[i] = (alpha<<24) | (pixels[i] & 0x00ffffff);
        }

        bitmap = Bitmap.createBitmap(pixels, w, h, Bitmap.Config.ARGB_8888);

        return bitmap;
    }

    //rotate ImageView
    public static ObjectAnimator rotateIV(ImageView imgView, int mm){
        ObjectAnimator animator = ObjectAnimator.ofFloat(imgView, "rotation", 0f,360f);
        animator.setDuration(mm);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);

        return animator;
    }

    /**
     * public void startRotateAnimation() {
     *         mRotateAnimator.cancel();
     *         mRotateAnimator.start();
     *     }
     *
     *     public void cancelRotateAnimation() {
     *         mLastAnimationValue = 0;
     *         mRotateAnimator.cancel();
     *     }
     *
     *     public void pauseRotateAnimation() {
     *         mLastAnimationValue = mRotateAnimator.getCurrentPlayTime();
     *         mRotateAnimator.cancel();
     *     }
     *
     *     public void resumeRotateAnimation() {
     *         mRotateAnimator.start();
     *         mRotateAnimator.setCurrentPlayTime(mLastAnimationValue);
     *     }
     */
}
