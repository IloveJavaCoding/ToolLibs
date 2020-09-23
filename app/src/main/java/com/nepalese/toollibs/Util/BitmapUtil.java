package com.nepalese.toollibs.Util;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
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
    private static final String TAG = "BitmapUtil";

    public static enum ScaleType {
        NORMAL, HORIZONTAL, VERTICAL
    }

    //====================================bitmap 数据类型转换========================================
    //drawable <--> Bitmap
    public static Drawable bitmap2Drawable(Context context, Bitmap bmp) {
        return new BitmapDrawable(context.getResources(), bmp); //BitmapDrawable(bmp) 已被弃用
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            return bitmapDrawable.getBitmap();
        }
        return null;
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

    //byte[] <--> Bitmap
    public static byte[] bitmap2Bytes(Bitmap bitmap) {
        if (bitmap == null) return new byte[0];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap bytes2Bitmap(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * get bitmap from res file
     * @param context
     * @param id 资源文件id
     * @return
     */
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

    //======================================bitmap 存储与读取========================================
    //从文件中读取bitmap
    public static Bitmap getBitmapFromFile(String path){
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

    /**
     * 存储Bitmap到本地
     * @param bitmap
     * @param path 本地路径
     * @param fileName 存储文件名
     */
    public static void bitmap2Local(Bitmap bitmap, String path, String fileName){
        File file = new File(path+"/"+fileName);
        if(file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            //这里输出问jpg文件，可更改如：png
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //=========================================bitmap 操控==========================================
    /**
     * 质量压缩图片方法
     * @param bitmap    图片
     * @param limitSize 大于多少 k 就压缩。
     * @return 图片
     */
    public static Bitmap compressBitmap(Bitmap bitmap, int limitSize) {
        if (bitmap == null) return null;
        if (limitSize < 1) limitSize = 1;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        //第一个参数： 图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;

        //循环判断如果压缩后图片是否大于compressNum KB,大于继续压缩
        while (baos.toByteArray().length / 1024 > limitSize && options > 20) {
            baos.reset();//即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
    }

    /**
     * 设置图片旋转度, 以及镜像情况,( 将图片按照某个角度进行旋转, 然后再镜像)
     * @param bitmap     需要旋转的图片
     * @param degree 旋转角度
     * @param scaleType 旋转轴
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degree, ScaleType scaleType) {
        if (bitmap == null) return null;
        if (degree == 0 && scaleType == ScaleType.NORMAL) return bitmap;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        if ((degree % 360) != 0) {
            matrix.postRotate(degree % 360);
        }
        if (scaleType == ScaleType.VERTICAL) {//垂直方向翻转
            matrix.postScale(1, -1);
        } else if (scaleType == ScaleType.HORIZONTAL) { //水平方向翻转
            matrix.postScale(-1, 1);
        }
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            Bitmap returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return returnBm;
        } catch (OutOfMemoryError e) {
            Log.e(TAG, "创建图片异常：" + e);
            gc();
            return bitmap;
        }
    }

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

    /**
     * 裁剪圆形图片
     * @param bitmap
     * @return
     */
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

    /**
     * 裁剪正方形图片
     * @param bitmap
     * @return
     */
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

    /**
     * 裁切图片
     * @param bitmap
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return
     */
    public static Bitmap clipBitmap(Bitmap bitmap, int left, int top, int right, int bottom) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int rawArea = (right - left) * (bottom - top);
        int targetArea = width * height * 4;

        int resultArea = rawArea;

        while (resultArea > targetArea) {
            options.inSampleSize *= 2;
            resultArea = rawArea / (options.inSampleSize * options.inSampleSize);
        }

        if (options.inSampleSize > 1) {
            options.inSampleSize /= 2;
        }

        try {
            left /= options.inSampleSize;
            top /= options.inSampleSize;
            right /= options.inSampleSize;
            bottom /= options.inSampleSize;

            Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, left, top, right, bottom);
            bitmap.recycle();
            return croppedBitmap;
        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * 缩放
     * @param bitmap
     * @param aimW 想要的宽度
     * @param amiH 想要的高度
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int aimW, int amiH){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleW = (float)aimW/width ;
        float scaleH = (float)amiH/height;

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

    public static Bitmap scaleBitmap(Bitmap bitmap, int aimW, int aimH){
        if(bitmap.getWidth()==aimW && bitmap.getHeight()==aimH){
            return bitmap;
        }

        Bitmap aimBm = Bitmap.createScaledBitmap(bitmap,aimW,aimH,false);
        if(aimBm!=bitmap){
            bitmap.recycle();
        }

        return aimBm;
    }

    //模糊
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
    public static Bitmap fastBlurBitmap(Bitmap sentBitmap, int radius) {//200
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

    /**
     * 设置图片效果，根据色调，饱和度，亮度来调节图片效果, 会创建新图
     * @param bitmap:要处理的图像
     * @param hue:色调
     * @param saturation:饱和度
     * @param lum:亮度
     */
    public static Bitmap bitmapEffect(Bitmap bitmap, float hue, float saturation, float lum) {
        hue = hue < -180.0f ? -180.0f : hue > 180.0f ? 180.0f : hue;
        saturation = saturation < 0.0f ? 0.0f : saturation > 2.0f ? 2.0f : saturation;
        lum = lum < 0.0f ? 0.0f : lum > 2.0f ? 2.0f : lum;

        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        ColorMatrix hueMatrix = new ColorMatrix();
        hueMatrix.setRotate(0, hue);    //0代表R，红色
        hueMatrix.setRotate(1, hue);    //1代表G，绿色
        hueMatrix.setRotate(2, hue);    //2代表B，蓝色

        ColorMatrix saturationMatrix = new ColorMatrix();
        saturationMatrix.setSaturation(saturation);

        ColorMatrix lumMatrix = new ColorMatrix();
        lumMatrix.setScale(lum, lum, lum, 1);

        ColorMatrix imageMatrix = new ColorMatrix();
        imageMatrix.postConcat(hueMatrix);
        imageMatrix.postConcat(saturationMatrix);
        imageMatrix.postConcat(lumMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return bmp;
    }

    //==========================================other===============================================
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
     * 合并两个Bitmap
     * @param backBitmap
     * @param frontBitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap mergeBitmap(Bitmap backBitmap, Bitmap frontBitmap, int width, int height) {
        if (backBitmap == null || backBitmap.isRecycled()
                || frontBitmap == null || frontBitmap.isRecycled()) {
            Log.e(TAG, "backBitmap=" + backBitmap + ";frontBitmap=" + frontBitmap);
            return null;
        }
        Bitmap bitmap = backBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Log.i(TAG, "mergeBitmap: width=" + width + " height=" + height);
        int leftGap = (width - frontBitmap.getWidth()) / 2;
        int topGap = (height - frontBitmap.getHeight()) / 2;
        Log.i(TAG, "mergeBitmap: leftGap=" + leftGap + " topGap=" + topGap);
        Log.i(TAG, "mergeBitmap: frontBitmap.getWidth()=" + frontBitmap.getWidth() + " frontBitmap.getHeight()=" + frontBitmap.getHeight());
        Rect baseRect = new Rect(0, 0, frontBitmap.getWidth(), frontBitmap.getHeight());
        Rect frontRect = new Rect(leftGap, topGap, leftGap + frontBitmap.getWidth(), topGap + frontBitmap.getHeight());
        canvas.drawBitmap(frontBitmap, baseRect, frontRect, null);
        return bitmap;
    }

    /**
     * 读取图片文件的类型，仅图片文件！！！
     * @param file 图片文件
     */
    public static String readBitmapType(File file) {
        String type = "image/jpeg";
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;// 只读边,不读内容
            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            type = options.outMimeType;
        }
        return type;
    }

    /**
     * 回收
     */
    public static void gc() {
        System.gc();
        // 表示java虚拟机会做一些努力运行已被丢弃对象（即没有被任何对象引用的对象）的 finalize
        // 方法，前提是这些被丢弃对象的finalize方法还没有被调用过
        System.runFinalization();
    }

    /**
     * rotate ImageView
     * @param imgView
     * @param duration 转一圈时长
     * @return
     */
    public static ObjectAnimator rotateIV(ImageView imgView, int duration){
        ObjectAnimator animator = ObjectAnimator.ofFloat(imgView, "rotation", 0f,360f);
        animator.setDuration(duration);
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
