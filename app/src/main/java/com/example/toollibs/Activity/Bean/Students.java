package com.example.toollibs.Activity.Bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

@Entity
public class Students implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @Id(autoincrement = true)
    private Long id;
    private String studentId;
    private String name;
    private String gender;//can be null
    private int age;//can be null
    private int grade;

    @Generated(hash = 174834727)
    public Students() {
    }

    @Generated(hash = 19919161)
    public Students(Long id, String studentId, String name, String gender, int age, int grade) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.grade = grade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
