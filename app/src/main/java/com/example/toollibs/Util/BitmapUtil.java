package com.example.toollibs.Util;

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
import android.text.TextUtils;
import android.util.Base64;

import com.example.toollibs.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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
    public static String Bitmap2String(Bitmap bitmap){
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

    public static Bitmap String2Bitmap(String data){
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

    //get circle shape
    public static Bitmap GetCircleBitmap(Bitmap bitmap){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap aimBm = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(aimBm);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0,0, w, h);
        final RectF rectF = new RectF(rect);

        float roundPx;
        if(w>h){
            roundPx = h / 2.0f;
        }else{
            roundPx = w / 2.0f;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0,0,0,0);
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(rectF,roundPx,roundPx,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        final Rect src = new Rect(0,0,w,h);
        canvas.drawBitmap(bitmap, src, rect, paint);

        return aimBm;
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
    public static Bitmap ScaleBitmap(Bitmap bitmap, int aimW, int aimH, boolean isRecycle){
        if(bitmap.getWidth()==aimW && bitmap.getHeight()==aimH){
            return bitmap;
        }

        Bitmap aimBm = Bitmap.createScaledBitmap(bitmap,aimW,aimH,false);
        if(isRecycle && aimBm!=bitmap){
            bitmap.recycle();
        }

        return aimBm;
    }
}
