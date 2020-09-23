package com.nepalese.toollibs.Bean;

/**
 * @author nepalese on 2020/9/23 17:13
 * @usage
 */
public class DownloadItem {
    private String url;
    private String fileName;
    private String savePath;

    public DownloadItem(String url, String fileName, String savePath) {
        this.url = url;
        this.fileName = fileName;
        this.savePath = savePath;
    }

    public String getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }

    public String getSavePath() {
        return savePath;
    }
}
