package com.nepalese.toollibs.Activity.ComponentThird;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <h4>${TITLE}</h4>.
 * <p>
 * <b>作者：</b>yangdewang.
 * <b>版本：</b>$VERSION$.
 * 创建于 2020/11/03 09:55.
 * <div class="special reference">
 * <hr/>
 * <i>Copyright (c) 2020 Harine. Inc.All Rights Reserved..</i>
 * <hr/>
 * </div>
 */
public class MarqueeBean implements Parcelable {
    private String msg;
    private float len;

    public MarqueeBean(String msg, float len) {
        this.msg = msg;
        this.len = len;
    }

    protected MarqueeBean(Parcel in) {
        msg = in.readString();
        len = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(msg);
        dest.writeFloat(len);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MarqueeBean> CREATOR = new Creator<MarqueeBean>() {
        @Override
        public MarqueeBean createFromParcel(Parcel in) {
            return new MarqueeBean(in);
        }

        @Override
        public MarqueeBean[] newArray(int size) {
            return new MarqueeBean[size];
        }
    };

    public String getMsg() {
        return msg;
    }

    public float getLen() {
        return len;
    }

    @Override
    public String toString() {
        return "{ " +
                " \"clzName\": \"MarqueeBean\"" +
                ", \"msg\": \"" + msg + '\"' +
                ", \"len\": " + len +
                '}';
    }
}
