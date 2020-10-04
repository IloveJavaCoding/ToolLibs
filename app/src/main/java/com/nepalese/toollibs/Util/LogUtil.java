package com.nepalese.toollibs.Util;

import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

/**
 * @author nepalese on 2020/9/11 11:37
 * @usage
 */
public class LogUtil {
    //textview 内容追加，自动滚动
    // tvLog.setMovementMethod(ScrollingMovementMethod.getInstance());

    public static void addLog(final TextView tvLog, final String strLog) {
        final String strText = strLog;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                String strDate = formatter.format(curDate);

                //已有的log
                String strLogs = tvLog.getText().toString().trim();
                if (strLogs.equals("")) {
                    strLogs = strDate + ": " + strText;
                } else {
                    strLogs += "\r\n" + strDate + ": " + strText;
                }
                //刷新添加新的log
                tvLog.setText(strLogs);
                //==================add auto scroll========================
                //log View自动滚动
                tvLog.post(new Runnable() {
                    @Override
                    public void run() {
                        int scrollAmount = tvLog.getLayout().getLineTop(tvLog.getLineCount()) - tvLog.getHeight();
                        if (scrollAmount > 0)
                            tvLog.scrollTo(0, scrollAmount);
                        else
                            tvLog.scrollTo(0, 0);
                    }
                });
            }
        });
    }
}
