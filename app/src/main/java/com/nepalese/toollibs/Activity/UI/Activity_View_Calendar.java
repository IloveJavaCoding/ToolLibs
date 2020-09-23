package com.nepalese.toollibs.Activity.UI;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;

import com.nepalese.toollibs.R;
import com.nepalese.toollibs.Util.Helper.LunarDecoratorHelper;
import com.nepalese.toollibs.Util.SystemUtil;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Activity_View_Calendar extends AppCompatActivity {
    private CalendarView calendarView;
    private MaterialCalendarView materialCalendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__view__calendar);

        init();
        setData();
        setListener();
    }

    private void init() {
        //default current date be the chosen day
        calendarView = findViewById(R.id.calenderView);
        materialCalendarView = findViewById(R.id.calendarView2);
    }

    private void setData() {
        //default is Sunday
        //calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        CalendarDay date = CalendarDay.today();
        materialCalendarView.setCurrentDate(date);
        materialCalendarView.setShowOtherDates(MaterialCalendarView.SHOW_OTHER_MONTHS);
        materialCalendarView
                .state()
                .edit()
                //设置一周的第一天是周日还是周一
                .setFirstDayOfWeek(Calendar.SUNDAY)
                //设置日期范围
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        //设置周的文本
        materialCalendarView.setWeekDayLabels(new String[]{"日", "一", "二", "三", "四", "五", "六"});
        //设置年月的title
        materialCalendarView.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                StringBuffer buffer = new StringBuffer();
                int yearOne = day.getYear();
                int monthOne = day.getMonth() + 1;
                buffer.append(yearOne).append("年").append(monthOne).append("月");
                return buffer;
            }
        });
    }

    private void setListener() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                SystemUtil.showToast(getApplicationContext(), year + " - "+ month + " - " + dayOfMonth);
            }
        });

        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                CalendarDay temp;
                List<LunarDecoratorHelper> lunarDecoratorHelpers = new ArrayList<>();
                for (int i = 18; i <= 31; i++) {
                    temp = CalendarDay.from(date.getYear(), date.getMonth() - 1, i);
                    add(temp, lunarDecoratorHelpers);
                }
                for (int i = 0; i <= 31; i++) {
                    temp = CalendarDay.from(date.getYear(), date.getMonth(), i);
                    add(temp, lunarDecoratorHelpers);
                }
                for (int i = 0; i < 14; i++) {
                    temp = CalendarDay.from(date.getYear(), date.getMonth() + 1, i);
                    add(temp, lunarDecoratorHelpers);
                }

                materialCalendarView.addDecorators(lunarDecoratorHelpers);
            }
        });
    }

    private void add(CalendarDay date, List<LunarDecoratorHelper> lunarDecoratorHelpers) {
        LunarDecoratorHelper decorator = new LunarDecoratorHelper(date);
        lunarDecoratorHelpers.add(decorator);
    }
}
