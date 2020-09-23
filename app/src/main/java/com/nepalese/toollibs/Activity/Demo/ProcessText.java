package com.nepalese.toollibs.Activity.Demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

public class ProcessText {
    private final int SIZE = 900;//每一页的字节数 字节数固定 
    private int pages;//总页数
    private long bytes;//字节总数
    private int curPage;//当前页面
    private RandomAccessFile reader;

    /**
     * @param file text file
     * @param page start page, can be used for tag
     */
    public ProcessText(File file, int page){
        try {
            reader = new RandomAccessFile(file, "r");
            bytes = reader.length();
            pages = (int) (bytes/SIZE);
            curPage = page;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void seekTo(int page){
        if(page>0 && page <= pages){
            try {
                reader.seek(page);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String read(){
        byte[] bytes = new byte[SIZE + 30];
        try {
            reader.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(bytes, Charset.forName("UTF-8"));
    }

    public String getCurPage(){
        seekTo(curPage);
        return read();
    }

    public String getLastPage(){
        if(curPage==1){//the first page
            //
        }else{
            curPage--;
        }
        return getCurPage();
    }

    public String getNextPage(){
        if(curPage==pages){//last page
            //
        }else{
            curPage++;
        }
        return getCurPage();
    }
}
