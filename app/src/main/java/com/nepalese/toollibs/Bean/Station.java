package com.nepalese.toollibs.Bean;

import java.util.List;

/**
 * @author nepalese on 2020/12/22 16:51
 * @usage
 */
public class Station extends BaseBean{
    private String name;
    private List<Student> list;

    public Station(String name, List<Student> list) {
        this.name = name;
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getList() {
        return list;
    }

    public void setList(List<Student> list) {
        this.list = list;
    }
}
