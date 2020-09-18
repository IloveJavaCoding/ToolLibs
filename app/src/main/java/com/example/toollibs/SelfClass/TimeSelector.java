package com.example.toollibs.SelfClass;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toollibs.Activity.ComponentThird.PickerView;
import com.example.toollibs.Util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.example.toollibs.R;
import com.example.toollibs.Util.ScreenUtil;

public class TimeSelector {
    private ResultHandler handler;//return the chosen time
    private Context context;

    private Dialog selectorDialog;
    private PickerView pvYear;
    private PickerView pvMonth;
    private PickerView pvDay;
    private PickerView pvHour;
    private PickerView pvMinute;

    private TextView tvCancel;
    private TextView tvConfirm;
    private TextView tvHourHint;
    private TextView tvMinuteHint;

    private int MAXHOUR = 23;
    private int MINHOUR = 0;
    private final int MAXMINUTE = 59;
    private final int MINMINUTE = 0;
    private final int MAXMONTH = 12;
    private final long ANIMATORDELAY = 200L;
    private final long CHANGEDELAY = 90L;
    private final String FORMAT_STR = "yyyy-MM-dd HH:mm";

    public interface ResultHandler {
        void handle(Date time);
    }
    public enum SCROLLTYPE {
        HOUR(1),
        MINUTE(2);
        SCROLLTYPE(int value) {
            this.value = value;
        }
        public int value;
    }
    public enum MODE {
        YMD(1),
        YMDHM(2);
        MODE(int value) {
            this.value = value;
        }
        public int value;
    }
    private MODE mDefaultMode = MODE.YMDHM;
    private int scrollUnits = SCROLLTYPE.HOUR.value + SCROLLTYPE.MINUTE.value;

    private ArrayList<String> year, month, day, hour, minute;
    private int startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute,
            minute_workStart, minute_workEnd, hour_workStart, hour_workEnd;//...
    private boolean spanYear, spanMon, spanDay, spanHour, spanMin;//

    private Calendar startCalendar, endCalendar;
    private Calendar selectedCalender = Calendar.getInstance();//used to record the selected time.

    //constructor
    public TimeSelector(Context context, ResultHandler resultHandler, String startDate, String endDate) {
        this.context = context;
        this.handler = resultHandler;
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        startCalendar.setTime(DateUtil.string2Date(startDate, FORMAT_STR));
        endCalendar.setTime(DateUtil.string2Date(endDate, FORMAT_STR));

        initDialog();//initial self defined dialog(show time)
        initView();//initial the components on the dialog
    }

    private void initDialog() {
        if (selectorDialog == null) {
            selectorDialog = new Dialog(context, R.style.time_dialog);
            selectorDialog.setCancelable(false);
            selectorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            selectorDialog.setContentView(R.layout.time_selector);//layout of the dialog

            //set the width of dialog be same as window and at the bottom
            Window window = selectorDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);//location -- bottom
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = ScreenUtil.getScreenDM(context).widthPixels;
            window.setAttributes(lp);
        }
    }

    private void initView() {
        pvYear = selectorDialog.findViewById(R.id.pvYear);
        pvMonth = selectorDialog.findViewById(R.id.pvMonth);
        pvDay = selectorDialog.findViewById(R.id.pvDay);
        pvHour = selectorDialog.findViewById(R.id.pvHour);
        pvMinute = selectorDialog.findViewById(R.id.pvMinute);

        tvCancel = selectorDialog.findViewById(R.id.tvCancel);
        tvConfirm = selectorDialog.findViewById(R.id.tvConfirm);
        tvHourHint = selectorDialog.findViewById(R.id.tvHourHint);
        tvMinuteHint = selectorDialog.findViewById(R.id.tvMinuteHint);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectorDialog.dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.handle(selectedCalender.getTime());//
                selectorDialog.dismiss();
            }
        });
    }


    public void show() {
        if (startCalendar.getTime().getTime() >= endCalendar.getTime().getTime()) {
            Toast.makeText(context, "start>end", Toast.LENGTH_LONG).show();
            return;
        }

        initParameter();
        initTimer();
        addListener();
        selectorDialog.show();
    }

    private void initParameter() {
        startYear = startCalendar.get(Calendar.YEAR);
        startMonth = startCalendar.get(Calendar.MONTH) + 1;
        startDay = startCalendar.get(Calendar.DAY_OF_MONTH);
        startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
        startMinute = startCalendar.get(Calendar.MINUTE);

        endYear = endCalendar.get(Calendar.YEAR);
        endMonth = endCalendar.get(Calendar.MONTH) + 1;
        endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
        endHour = endCalendar.get(Calendar.HOUR_OF_DAY);
        endMinute = endCalendar.get(Calendar.MINUTE);

        spanYear = startYear != endYear;//true
        spanMon = (!spanYear) && (startMonth != endMonth);
        spanDay = (!spanMon) && (startDay != endDay);
        spanHour = (!spanDay) && (startHour != endHour);
        spanMin = (!spanHour) && (startMinute != endMinute);
        //default choose the day of today
        selectedCalender.setTime(Calendar.getInstance().getTime());
    }

    private void initTimer() {
        initArrayList();

        if (spanYear) {
            for (int i = startYear; i <= endYear; i++) {
                year.add(String.valueOf(i));
            }
            for (int i = startMonth; i <= MAXMONTH; i++) {
                month.add(formatTimeUnit(i));
            }
            for (int i = startDay; i <= startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(formatTimeUnit(i));
            }
            if ((scrollUnits & SCROLLTYPE.HOUR.value) != SCROLLTYPE.HOUR.value) {
                hour.add(formatTimeUnit(startHour));
            } else {
                for (int i = startHour; i <= MAXHOUR; i++) {
                    hour.add(formatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLLTYPE.MINUTE.value) != SCROLLTYPE.MINUTE.value) {
                minute.add(formatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= MAXMINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }

        } else if (spanMon) {
            year.add(String.valueOf(startYear));
            for (int i = startMonth; i <= endMonth; i++) {
                month.add(formatTimeUnit(i));
            }
            for (int i = startDay; i <= startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(formatTimeUnit(i));
            }
            if ((scrollUnits & SCROLLTYPE.HOUR.value) != SCROLLTYPE.HOUR.value) {
                hour.add(formatTimeUnit(startHour));
            } else {
                for (int i = startHour; i <= MAXHOUR; i++) {
                    hour.add(formatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLLTYPE.MINUTE.value) != SCROLLTYPE.MINUTE.value) {
                minute.add(formatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= MAXMINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }
        } else if (spanDay) {
            year.add(String.valueOf(startYear));
            month.add(formatTimeUnit(startMonth));
            for (int i = startDay; i <= endDay; i++) {
                day.add(formatTimeUnit(i));
            }
            if ((scrollUnits & SCROLLTYPE.HOUR.value) != SCROLLTYPE.HOUR.value) {
                hour.add(formatTimeUnit(startHour));
            } else {
                for (int i = startHour; i <= MAXHOUR; i++) {
                    hour.add(formatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLLTYPE.MINUTE.value) != SCROLLTYPE.MINUTE.value) {
                minute.add(formatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= MAXMINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }

        } else if (spanHour) {
            year.add(String.valueOf(startYear));
            month.add(formatTimeUnit(startMonth));
            day.add(formatTimeUnit(startDay));

            if ((scrollUnits & SCROLLTYPE.HOUR.value) != SCROLLTYPE.HOUR.value) {
                hour.add(formatTimeUnit(startHour));
            } else {
                for (int i = startHour; i <= endHour; i++) {
                    hour.add(formatTimeUnit(i));
                }
            }

            if ((scrollUnits & SCROLLTYPE.MINUTE.value) != SCROLLTYPE.MINUTE.value) {
                minute.add(formatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= MAXMINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }

        } else if (spanMin) {
            year.add(String.valueOf(startYear));
            month.add(formatTimeUnit(startMonth));
            day.add(formatTimeUnit(startDay));
            hour.add(formatTimeUnit(startHour));

            if ((scrollUnits & SCROLLTYPE.MINUTE.value) != SCROLLTYPE.MINUTE.value) {
                minute.add(formatTimeUnit(startMinute));
            } else {
                for (int i = startMinute; i <= endMinute; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }
        }
        loadComponent();
    }

    private String formatTimeUnit(int unit) {
        return unit < 10 ? "0" + unit : String.valueOf(unit);
    }

    private void initArrayList() {
        if (year == null) year = new ArrayList<>();
        if (month == null) month = new ArrayList<>();
        if (day == null) day = new ArrayList<>();
        if (hour == null) hour = new ArrayList<>();
        if (minute == null) minute = new ArrayList<>();
        year.clear();
        month.clear();
        day.clear();
        hour.clear();
        minute.clear();
    }

    private void addListener() {
        pvYear.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.YEAR, Integer.parseInt(text));
                monthChange();
            }
        });

        pvMonth.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.DAY_OF_MONTH, 1);
                selectedCalender.set(Calendar.MONTH, Integer.parseInt(text) - 1);
                dayChange();
            }
        });

        pvDay.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(text));
                hourChange();
            }
        });

        pvHour.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(text));
                minuteChange();
            }
        });

        pvMinute.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.MINUTE, Integer.parseInt(text));
            }
        });
    }

    private void loadComponent() {
        pvYear.setData(year);
        pvMonth.setData(month);
        pvDay.setData(day);
        pvHour.setData(hour);
        pvMinute.setData(minute);

        Date now =  Calendar.getInstance().getTime();//default shows the date of today
        pvYear.setSelected(DateUtil.date2String(now,"yyyy"));
        pvMonth.setSelected(DateUtil.date2String(now, "MM"));
        pvDay.setSelected(DateUtil.date2String(now, "dd"));
        pvHour.setSelected(0);
        pvMinute.setSelected(0);
        executeScroll();
    }

    private void executeScroll() {
        pvYear.setCanScroll(year.size() > 1);
        pvMonth.setCanScroll(month.size() > 1);
        pvDay.setCanScroll(day.size() > 1);
        pvHour.setCanScroll(hour.size() > 1 && (scrollUnits & SCROLLTYPE.HOUR.value) == SCROLLTYPE.HOUR.value);
        pvMinute.setCanScroll(minute.size() > 1 && (scrollUnits & SCROLLTYPE.MINUTE.value) == SCROLLTYPE.MINUTE.value);
    }

    private void monthChange() {
        month.clear();
        int selectedYear = selectedCalender.get(Calendar.YEAR);
        if (selectedYear == startYear) {
            for (int i = startMonth; i <= MAXMONTH; i++) {
                month.add(formatTimeUnit(i));
            }
        } else if (selectedYear == endYear) {
            for (int i = 1; i <= endMonth; i++) {
                month.add(formatTimeUnit(i));
            }
        } else {
            for (int i = 1; i <= MAXMONTH; i++) {
                month.add(formatTimeUnit(i));
            }
        }
        selectedCalender.set(Calendar.MONTH, Integer.parseInt(month.get(0)) - 1);
        pvMonth.setData(month);
        pvMonth.setSelected(0);
        executeAnimator(ANIMATORDELAY, pvMonth);
        pvMonth.postDelayed(new Runnable() {
            @Override
            public void run() {
                dayChange();
            }
        }, CHANGEDELAY);
    }

    private void dayChange() {
        day.clear();
        int selectedYear = selectedCalender.get(Calendar.YEAR);
        int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
        if (selectedYear == startYear && selectedMonth == startMonth) {
            for (int i = startDay; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(formatTimeUnit(i));
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth) {
            for (int i = 1; i <= endDay; i++) {
                day.add(formatTimeUnit(i));
            }
        } else {
            for (int i = 1; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                day.add(formatTimeUnit(i));
            }
        }
        selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day.get(0)));
        pvDay.setData(day);
        pvDay.setSelected(0);
        executeAnimator(ANIMATORDELAY, pvDay);
        pvDay.postDelayed(new Runnable() {
            @Override
            public void run() {
                hourChange();
            }
        }, CHANGEDELAY);
    }

    private void hourChange() {
        if ((scrollUnits & SCROLLTYPE.HOUR.value) == SCROLLTYPE.HOUR.value) {
            hour.clear();
            int selectedYear = selectedCalender.get(Calendar.YEAR);
            int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
            int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);

            if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay) {
                for (int i = startHour; i <= MAXHOUR; i++) {
                    hour.add(formatTimeUnit(i));
                }
            } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay) {
                for (int i = MINHOUR; i <= endHour; i++) {
                    hour.add(formatTimeUnit(i));
                }
            } else {
                for (int i = MINHOUR; i <= MAXHOUR; i++) {
                    hour.add(formatTimeUnit(i));
                }
            }
            selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour.get(0)));
            pvHour.setData(hour);
            pvHour.setSelected(0);
            executeAnimator(ANIMATORDELAY, pvHour);
        }
        pvHour.postDelayed(new Runnable() {
            @Override
            public void run() {
                minuteChange();
            }
        }, CHANGEDELAY);
    }

    private void minuteChange() {
        if ((scrollUnits & SCROLLTYPE.MINUTE.value) == SCROLLTYPE.MINUTE.value) {
            minute.clear();
            int selectedYear = selectedCalender.get(Calendar.YEAR);
            int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
            int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
            int selectedHour = selectedCalender.get(Calendar.HOUR_OF_DAY);

            if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay && selectedHour == startHour) {
                for (int i = startMinute; i <= MAXMINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay && selectedHour == endHour) {
                for (int i = MINMINUTE; i <= endMinute; i++) {
                    minute.add(formatTimeUnit(i));
                }
            } else if (selectedHour == hour_workStart) {
                for (int i = minute_workStart; i <= MAXMINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            } else if (selectedHour == hour_workEnd) {
                for (int i = MINMINUTE; i <= minute_workEnd; i++) {
                    minute.add(formatTimeUnit(i));
                }
            } else {
                for (int i = MINMINUTE; i <= MAXMINUTE; i++) {
                    minute.add(formatTimeUnit(i));
                }
            }
            selectedCalender.set(Calendar.MINUTE, Integer.parseInt(minute.get(0)));
            pvMinute.setData(minute);
            pvMinute.setSelected(0);
            executeAnimator(ANIMATORDELAY, pvMinute);
        }
        executeScroll();
    }

    private void executeAnimator(long ANIMATORDELAY, View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f,
                0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f,
                1.3f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f,
                1.3f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(ANIMATORDELAY).start();
    }

    public int disScrollUnit(SCROLLTYPE... scrolltypes) {
        if (scrolltypes == null || scrolltypes.length == 0)
            scrollUnits = SCROLLTYPE.HOUR.value + SCROLLTYPE.MINUTE.value;
        for (SCROLLTYPE scrolltype : scrolltypes) {
            scrollUnits ^= scrolltype.value;
        }
        return scrollUnits;
    }

    public void setMode(MODE mode) {
        mDefaultMode = mode;
        switch (mode.value) {
            case 1:
                disScrollUnit(SCROLLTYPE.HOUR, SCROLLTYPE.MINUTE);
                pvHour.setVisibility(View.GONE);
                pvMinute.setVisibility(View.GONE);
                tvHourHint.setVisibility(View.GONE);
                tvMinuteHint.setVisibility(View.GONE);
                break;
            case 2:
                disScrollUnit();
                pvHour.setVisibility(View.VISIBLE);
                pvMinute.setVisibility(View.VISIBLE);
                tvHourHint.setVisibility(View.VISIBLE);
                tvMinuteHint.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setIsLoop(boolean isLoop) {
        this.pvYear.setIsLoop(isLoop);
        this.pvMonth.setIsLoop(isLoop);
        this.pvDay.setIsLoop(isLoop);
        this.pvHour.setIsLoop(isLoop);
        this.pvMinute.setIsLoop(isLoop);
    }
}
