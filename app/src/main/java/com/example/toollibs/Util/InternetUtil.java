package com.example.toollibs.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * to connect the internet, it must be run in a background thread
 */
public class InternetUtil {
    //deal image link
    public static Bitmap GetBitmapFromUrl(String imgUrl){
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

    public static void DownloadImgFromUrl(String imgUrl, String path, String fileName) {
        Bitmap bitmap = GetBitmapFromUrl((imgUrl));
        BitmapUtil.Bitmap2Local(bitmap, path, fileName);
    }

    //download file from url
    public static void downloadFile(String strUrl, String path, String fileName) {
        File file = new File(path+"/"+fileName);

        if (file.exists()) {
            //--------------------------
        } else {
            try {
                URL url = new URL(strUrl);
                URLConnection conn = url.openConnection();
                InputStream inputStream = conn.getInputStream();

                byte[] buffer = new byte[1024];
                int len;
                OutputStream outputStream = new FileOutputStream(file);
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
