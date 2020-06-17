package com.example.toollibs.Activity.Demo;

import com.example.toollibs.Activity.Config.Constant;

public class LiveSource {
    private String city;
    private String frequency;
    private int audPid;
    private int vidPid;
    private int vidType;
    private int audType;

    public LiveSource(String city, String frequency, int audPid, int vidPid, int vidType, int audType) {
        this.city = city;
        this.frequency = frequency;
        this.audPid = audPid;
        this.vidPid = vidPid;
        this.vidType = vidType;
        this.audType = audType;
    }

    public LiveSource(){}

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public int getAudPid() {
        return audPid;
    }

    public void setAudPid(int audPid) {
        this.audPid = audPid;
    }

    public int getVidPid() {
        return vidPid;
    }

    public void setVidPid(int vidPid) {
        this.vidPid = vidPid;
    }

    public int getVidType() {
        return vidType;
    }

    public void setVidType(int vidType) {
        this.vidType = vidType;
    }

    public int getAudType() {
        return audType;
    }

    public void setAudType(int audType) {
        this.audType = audType;
    }

    public void setValue(String field,String value){
        switch (field){
            case Constant.CITY:
                city = value;
                break;
            case Constant.FREQUENCY:
                frequency = value;
                break;
            case Constant.AUD_PID:
                audPid = Integer.parseInt(value);
                break;
            case Constant.VID_PID:
                vidPid = Integer.parseInt(value);
                break;
            case Constant.VID_TYPE:
                vidType = Integer.parseInt(value);
                break;
            case Constant.AUD_TYPE:
                audType  = Integer.parseInt(value);
                break;
        }
    }

    @Override
    public String toString() {
        return "LiveSource{" +
                "city='" + city + '\'' +
                ", frequency='" + frequency + '\'' +
                ", audPid=" + audPid +
                ", vidPid=" + vidPid +
                ", vidType=" + vidType +
                ", audType=" + audType +
                '}';
    }
}
