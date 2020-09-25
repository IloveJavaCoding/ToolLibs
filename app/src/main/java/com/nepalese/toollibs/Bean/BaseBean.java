package com.nepalese.toollibs.Bean;

import com.nepalese.toollibs.Util.ConvertUtil;

import java.io.Serializable;

/**
 * @author nepalese on 2020/9/25 13:55
 * @usage
 */
public class BaseBean implements Serializable {
    public BaseBean(){
    }

    public String toJson(){
        return ConvertUtil.toJson(this);
    }
}
