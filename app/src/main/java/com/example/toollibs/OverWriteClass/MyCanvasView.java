package com.example.toollibs.OverWriteClass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.toollibs.R;

public class MyCanvasView extends View {
    private Paint strokePaint, fillPaint;
    private final float[] points = {10,10, 30,10, 50,10, 50,30, 50,50, 30,50, 30,10, 10,70, 10,90,
    30,110, 50,110, 50,130, 50,150, 30,140, 10,130, 60,80, 60,100, 60,120, 60,140, 80,10, 800,300,
            80,50, 80,70, 80,90, 80,110, 80,130, 80,150, 100,140, 120,130, 140,10, 120,30, 100,50,
            100,100, 120,120, 140,140};
    public MyCanvasView(Context context) {
        super(context);
    }

    public MyCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        strokePaint = new Paint();
        strokePaint.setColor(getResources().getColor(R.color.colorPrimary));
        strokePaint.setAntiAlias(true);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeCap(Paint.Cap.ROUND);
        strokePaint.setStrokeWidth(10f);

        fillPaint = new Paint();
        fillPaint.setColor(getResources().getColor(R.color.colorPrimary));
        fillPaint.setAntiAlias(true);
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setStrokeWidth(10f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPoints(points, fillPaint);

        canvas.drawText("zhangshuanliang", 100,100, strokePaint);

        canvas.drawCircle(200,200, 50, strokePaint);
        canvas.drawLine(200,200, 200,150, strokePaint);
        canvas.drawLine(200,200, 250,200, strokePaint);
        canvas.drawLine(200,200, 200,250, strokePaint);
        canvas.drawLine(200,200, 150,200, strokePaint);
    }

    private void sleep(int mm){
        try {
            Thread.sleep(mm);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
