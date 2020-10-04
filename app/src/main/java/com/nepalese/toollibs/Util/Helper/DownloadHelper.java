package com.nepalese.toollibs.Util.Helper;

import android.text.TextUtils;
import android.util.Log;

import com.nepalese.toollibs.Util.DateUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;

/**
 * @author nepalese on 2020/9/8 09:49
 * @usage
 */
public class DownloadHelper {
    private static final String TAG = "DownloadHelper";
    private static final String tag_file = "file";//用来区分下载的文件类型

    public static void restore() {
        //恢复数据
        OkDownload.restore(DownloadManager.getInstance().getAll());
    }

    public static void downloadAllError() {
        OkDownload.getInstance().startAll();
    }

    public static void downloadFile(String url, String fileName, String savePath) {
        //任务存在，继续下载
        if (OkDownload.getInstance().hasTask(url)) {
            DownloadTask task = OkDownload.getInstance().getTask(url);
            if (task.progress.status != Progress.FINISH) {
                Log.i(TAG, "downloadConfigZip 继续下载 = " + url);
                task.start();
            }
            return;
        }

        //新任务
        File file = new File(savePath+File.separator+fileName);
        if(file.exists()) {
            Log.i(TAG, "文件已存在！");
            //以当下时间重命名文件
            fileName = DateUtil.getCurTime()+getFileSuffix(fileName);
        }

        Log.i(TAG, "downloadConfigZip 新的下载 = " + url);
        GetRequest<File> request = OkGo.<File>get(url);//
        // 同时下载量最大5个，核心3个，
        //这里第一个参数是tag，代表下载任务的唯一标识，传任意字符串都行，需要保证唯一,我这里用url作为了tag
        OkDownload.request(url, request)//
                .folder(savePath) // 存储的文件夹
                .fileName(fileName)
                .priority(1)// 优先级
                .save()//
                .register(new DownloadCallback(tag_file))
                .start();
    }

    /**
     * 提取文件名的后缀
     * @param fileName
     * @return 如：.jpg, .png .mp3 ...
     */
    private static String getFileSuffix(String fileName){
        if(TextUtils.isEmpty(fileName)){
            return null;
        }

        return fileName.substring(fileName.lastIndexOf("."));
    }

    private static class DownloadCallback extends DownloadListener {
        private int lastRate = 0;
        private long time;

        DownloadCallback(Object tag) {
            super(tag);
        }

        @Override
        public void onStart(Progress progress) {
            lastRate = 0;
            float rate = progress.currentSize * 1f / progress.totalSize;
            int count = (int) (rate * 100);
            Log.i(TAG, "初始进度 = " + count);
            Log.i(TAG, "开始下载 " + progress.url + " 时间 " + (time = System.currentTimeMillis()));
        }

        @Override
        public void onProgress(Progress progress) {
            if (TextUtils.equals(tag_file, String.valueOf(tag))) {
                float rate = progress.currentSize * 1f / progress.totalSize;
                int count = (int) (rate * 100);
                if (count != lastRate) {
                    lastRate = count;
                    Log.i(TAG, "进度 = " + lastRate + " progress = " + progress.toString());
                }
            }
        }

        @Override
        public void onError(Progress progress) {
            Throwable throwable = progress.exception;
            if (throwable != null) throwable.printStackTrace();
            Log.e(TAG, tag + " 下载失败 url = " + progress.url);

            DownloadTask task = OkDownload.getInstance().removeTask(progress.tag);
            task.unRegister(this);
        }

        @Override
        public void onFinish(File file, Progress progress) {
            long t = System.currentTimeMillis();
            Log.e(TAG, tag + " 下载完成 = " + file.getAbsolutePath()
                    + "\r\n url = " + progress.url + "\r\n 结束下载时间 " + t + " 总时间 = " + ((t - time)));
            }

        @Override
        public void onRemove(Progress progress) {

        }
    }
}
