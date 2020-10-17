package com.nepalese.toollibs.Util.Helper;

import android.text.TextUtils;
import android.util.Log;

import com.nepalese.toollibs.Activity.MyApplication;

import java.io.File;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * @author nepalese on 2020/10/17 11:29
 * @usage
 */
public class CompressImgHelper {
    private static final String TAG = "CompressImgHelper";

    public static void compress(String src, final String desPath, final Callback callback, int limitSize) {
        if (src != null) {
            //创建鲁班压缩
            Luban.with(MyApplication.getContext())
                    .ignoreBy(limitSize) //不压缩的阈值，单位为K
                    .filter(new CompressionPredicate() {
                        @Override
                        public boolean apply(String path) {
                            return !(TextUtils.isEmpty(path) /*|| path.toLowerCase().endsWith(".gif")*/);
                        }
                    })
                    .load(src)
                    .setTargetDir(desPath)  //缓存压缩图片路径
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                            //  压缩开始前调用，可以在方法内启动 loading UI
                            Log.v(TAG, ("onCompress start"));
                        }

                        @Override
                        public void onSuccess(File file) {
                            //  压缩成功后调用，返回压缩后的图片文件
                            if (file != null) {
                                Log.v(TAG, "压缩成功 = " + file.getAbsolutePath() + " \nsize = " + file.length());
                                if (callback != null) {
                                    callback.onCompressResult(file.getAbsolutePath());
                                }
                            } else {
                                if (callback != null) {
                                    callback.onCompressResult("");
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            //  当压缩过程出现问题时调用
                            Log.e(TAG, "压缩异常" + e);
                            if (callback != null) {
                                callback.onCompressResult("");
                            }
                        }
                    }).launch();
        }
    }

    public interface Callback{
        void onCompressResult(String path);
    }
}

/**example
 *  File file = new File(path);
    *
    * CompressImgHelper.compress(path, file.getParent(), new CompressImgHelper.Callback() {
        *  @Override
        *  public void onCompressResult(String path) {
        *   Log.i(TAG, "压缩后的截屏图片 = " + path);
        *  });
 */