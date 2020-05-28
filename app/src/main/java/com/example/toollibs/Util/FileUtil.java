package com.example.toollibs.Util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * some common api for file operation
 */
public class FileUtil {
    //get external file path
    public static String GetRootPath(){
        //storage/emulated/0
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static String GetAppRootPth(Context context){
        ///storage/emulated/0/Android/data/pack_name/files
        return context.getExternalFilesDir("").getAbsolutePath();
    }

    public static String getInternalPath(){
        // /data
        return Environment.getDataDirectory().getAbsolutePath();
    }

    public static String getInternalAppPath(Context context){
        // /data/user/0/packname/files
        return context.getFilesDir().getAbsolutePath();
    }

    //create file/dir
    public static boolean CreateFile(String path, String fileName){
        File file = new File(path+"/"+fileName);

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }else{
            //
            return false;
        }
    }

    public static boolean CreateDirs(String folder){
        File file = new File(folder);

        if(!file.exists()) {
            file.mkdirs();
            return true;
        }
        return false;
    }

    //delete file/dir
    public static void DeleteDir(String dir){
        File file = new File(dir);
        if(file.exists()){
            DeleteDirWithFile(file);
        }
    }

    private static void DeleteDirWithFile(File file) {
        if(!file.isDirectory() || !file.exists()) {
            return;
        }
        if(file.list().length>0){
            for(File f : file.listFiles()){
                if(f.isFile()){
                    f.delete();//delete all files
                }else if(f.isDirectory()){
                    DeleteDirWithFile(f);
                }
            }
        }

        file.delete();
    }

    //get file/dir info
    public static int GetFilesNumber(String dir){
        File file = new File(dir);

        return file.listFiles().length;
    }

    public static String[] GetFilesList(String dir){
        File file = new File(dir);

        return file.list();
    }

    //change file/dir name
    public static void ChangeFile_DirName(String path, String oldName, String newName){
        File oldFile = new File(path+"/"+oldName);
        File newFile = new File(path+"/"+newName);

        oldFile.renameTo(newFile);
    }

    //read and write content
    public static void WriteToFile(String content, String path, String fileName){
        File file = new File(path+fileName);
        if(!file.exists()){
            CreateFile(path, fileName);
        }

        RandomAccessFile randomAccessFile;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(0);//rewrite
            randomAccessFile.write(content.getBytes());

            randomAccessFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String ReadContents(String path, String fileName, String format){
        if(format==null){
            format="utf-8";
        }

        File file = new File(path+fileName);
        if(!file.exists()){
            //
            return null;
        }

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, format);//'utf-8' 'GBK'
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder builder = new StringBuilder("");
        String line;
        try {
            while((line=reader.readLine())!=null){
                builder.append(line);
                builder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    //zip/unzip file/dir
    public static void unZip(String path, String aimPath, String password) throws ZipException {
        File file = new File(path);
        if(!file.exists()){
            return;
        }

        unZipFile(file, aimPath, password);
    }

    public static void unZipFile(File file, String aimPath, String password) throws ZipException {
        ZipFile zipFile = new ZipFile(file);
        zipFile.setFileNameCharset("GBK");

        if (!zipFile.isValidZipFile()) {
            throw new ZipException("压缩文件不合法,可能被损坏.");
        }

        if(!TextUtils.isEmpty(aimPath)){
            File destDir = new File(aimPath);
            if (destDir.isDirectory() && !destDir.exists()) {
                destDir.mkdir();
            }
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password.toCharArray());
            }
            zipFile.extractAll(aimPath);
        }else{//unzip to current path
            File parentDir = file.getParentFile();
            unZipFile(file, parentDir.getAbsolutePath(), password);
        }
    }

    public static boolean zipFile(String path, String aimPath, String password) {
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }

        aimPath = buildDestinationZipFilePath(file, aimPath);
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);            // 压缩方式
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);    // 压缩级别
        if (!TextUtils.isEmpty(password)) {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);    // 加密方式
            parameters.setPassword(password.toCharArray());
        }
        try {
            ZipFile zipFile = new ZipFile(aimPath);
            if (file.isDirectory()) {
                // 如果不创建目录的话,将直接把给定目录下的文件压缩到压缩文件,即没有目录结构
                File[] subFiles = file.listFiles();
                ArrayList<File> temp = new ArrayList<File>();
                Collections.addAll(temp, subFiles);
                zipFile.addFiles(temp, parameters);
            } else {
                zipFile.addFile(file, parameters);
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return true;
    }

    private static String buildDestinationZipFilePath(File srcFile, String destParam) {
        if (TextUtils.isEmpty(destParam)) {
            if (srcFile.isDirectory()) {
                destParam = srcFile.getParent() + File.separator + srcFile.getName() + ".zip";
            } else {
                String fileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));
                destParam = srcFile.getParent() + File.separator + fileName + ".zip";
            }
        } else {
            createDestDirectoryIfNecessary(destParam);    // 在指定路径不存在的情况下将其创建出来
            if (destParam.endsWith(File.separator)) {
                String fileName = "";
                if (srcFile.isDirectory()) {
                    fileName = srcFile.getName();
                } else {
                    fileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));
                }
                destParam += fileName + ".zip";
            }
        }
        return destParam;
    }

    private static void createDestDirectoryIfNecessary(String destParam) {
        File destDir = null;
        if (destParam.endsWith(File.separator)) {
            destDir = new File(destParam);
        } else {
            destDir = new File(destParam.substring(0, destParam.lastIndexOf(File.separator)));
        }
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }
}
