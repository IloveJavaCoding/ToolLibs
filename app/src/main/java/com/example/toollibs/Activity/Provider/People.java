package com.example.toollibs.Activity.Provider;

import android.net.Uri;

public class People {
    //package="com.example.toollibs"
    public static final String MIME_DIR_PREFIX = "com.android.cursor.dir";
    public static final String MIME_ITEM_PREFIX = "com.android.cursor.item";
    public static final String MIME_ITEM = "com.example.people";

    public static final String MIME_TYPE_SINGLE = MIME_ITEM_PREFIX + "/" + MIME_ITEM ;
    public static final String MIME_TYPE_MULTIPLE = MIME_DIR_PREFIX + "/" + MIME_ITEM ;

    public static final String AUTHORITY = "com.example.peopleprovider";
    public static final String PATH_SINGLE = "people/#";
    public static final String PATH_MULTIPLE = "people";

    /**
     * 封裝標準的形式:content://<authority>/<data_path>/<id>
     */
    public static final String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + PATH_MULTIPLE;

    /**
     * 暴露和共享的URL，
     */
    public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);

    /**
     * 數據庫表的字段
     */
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_AGE = "age";
    public static final String KEY_HEIGHT = "height";
}
