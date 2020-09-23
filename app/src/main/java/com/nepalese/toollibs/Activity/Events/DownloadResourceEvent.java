package com.nepalese.toollibs.Activity.Events;

import com.nepalese.toollibs.Bean.DownloadItem;

/**
 * @author nepalese on 2020/9/23 17:19
 * @usage
 */
public class DownloadResourceEvent {
    private DownloadItem item;

    public DownloadResourceEvent(DownloadItem item) {
        this.item = item;
    }

    public DownloadItem getItem() {
        return item;
    }
}
