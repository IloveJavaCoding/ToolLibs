package com.example.toollibs.SelfClass;

public class AudioFile {
    private long sId;
    private String sName;
    private String sDName;
    private String sSinger;
    private String sPath;
    private String sAlbum;
    private long sLogo;
    private int sLength;//duration
    private long sSize;

    public String getsDName() {
        return sDName;
    }

    public void setsDName(String sDname) {
        this.sDName = sDname;
    }

    public long getsLogo() {
        return sLogo;
    }

    public void setsLogo(long sLogo) {
        this.sLogo = sLogo;
    }

    public long getsId() {
        return sId;
    }

    public void setsId(long sId) {
        this.sId = sId;
    }

    public long getsSize() {
        return sSize;
    }

    public void setsSize(long sSize) {
        this.sSize = sSize;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsSinger() {
        return sSinger;
    }

    public void setsSinger(String sSinger) {
        this.sSinger = sSinger;
    }

    public String getsPath() {
        return sPath;
    }

    public void setsPath(String sPath) {
        this.sPath = sPath;
    }

    public String getsAlbum() {
        return sAlbum;
    }

    public void setsAlbum(String sAlbum) {
        this.sAlbum = sAlbum;
    }

    public int getsLength() {
        return sLength;
    }

    public void setsLength(int sLength) {
        this.sLength = sLength;
    }
}
