package com.nepalese.toollibs.Bean;

public class FlowInfo extends BaseBean{
    private String packageName;
    private String appNAme;
    private long upKb;
    private long downKb;

    public FlowInfo() {
    }

    public FlowInfo(String packageName, String appNAme, long upKb, long downKb) {
        this.packageName = packageName;
        this.appNAme = appNAme;
        this.upKb = upKb;
        this.downKb = downKb;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppNAme() {
        return appNAme;
    }

    public void setAppNAme(String appNAme) {
        this.appNAme = appNAme;
    }

    public void setUpKb(long upKb) {
        this.upKb = upKb;
    }

    public void setDownKb(long downKb) {
        this.downKb = downKb;
    }

    public long getUpKb() {
        return upKb;
    }

    public long getDownKb() {
        return downKb;
    }
}
