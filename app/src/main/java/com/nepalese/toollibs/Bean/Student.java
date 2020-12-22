package com.nepalese.toollibs.Bean;

/**
 * @author nepalese on 2020/12/22 16:52
 * @usage
 */
public class Student extends BaseBean{
    private int id;
    private String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
