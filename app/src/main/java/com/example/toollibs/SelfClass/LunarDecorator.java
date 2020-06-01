package com.example.toollibs.SelfClass;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class LunarDecorator implements DayViewDecorator {
    private CalendarDay day;

    public LunarDecorator(CalendarDay day) {
        this.day = day;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        if (day.getDate().equals(this.day.getDate())) {
            this.day = day;
            return true;
        }
        return false;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new LineBackgroundSpan() {
            @Override
            public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
                Paint paint = new Paint();
                paint.setTextSize(20);
                paint.setColor(Color.parseColor("#D81B60"));
                String time = new LunarCalendar().getLunarDate(day.getYear(), day.getMonth()+1,day.getDay(), false);
                c.drawText(time, (right - left) / 2 - 20, (bottom - top) / 2 + 17*2, paint);
            }
        });
    }
}
