package com.example.toollibs.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * to connect the internet, it must be run in a background thread
 */
public class InternetUtil {
    private static final String TAG = "InternetUtil";
    //从有效图片URL连接获取数据流，解析为bitmap便于显示
    public static Bitmap getBitmapFromUrl(String imgUrl){
        Bitmap bitmap = null;
        try {
            URL url = new URL(imgUrl);
            InputStream inputStream = url.openStream();

            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //将图片url先解析成bitmap, 然后保存到本地
    public static void downloadImgFromUrl(String imgUrl, String path, String fileName) {
        Bitmap bitmap = getBitmapFromUrl((imgUrl));
        BitmapUtil.bitmap2Local(bitmap, path, fileName);
    }

    //从有效资源文件url下载资源到本地（类型不限）
    public static void downloadFile(String strUrl, String path, String fileName) {
        File file = new File(path+File.separator+fileName);

        if (file.exists()) {
            //--------------------------
        } else {
            try {
                URL url = new URL(strUrl);

                //===============================================
                HttpURLConnection http = (HttpURLConnection) url.openConnection();;
                http.connect();
                int code = http.getResponseCode();
                if(code!=200){
                    //connect error
                    Log.d(TAG, "connect failed!!!");
                    return;
                }
                //length of file
                int length = http.getContentLength();
                //================================================

                InputStream inputStream = http.getInputStream();

                byte[] buffer = new byte[1024];
                int len;
                OutputStream outputStream = new FileOutputStream(file);
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                Log.d(TAG, "download successful!");
                outputStream.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
