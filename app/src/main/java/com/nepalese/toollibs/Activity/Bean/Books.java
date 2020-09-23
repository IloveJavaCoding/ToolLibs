package com.nepalese.toollibs.Activity.Bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

@Entity
public class Books implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String author;
    private String path;
    private String album;
    private int tag;

    @Generated(hash = 2016280518)
    public Books() {
    }

    @Generated(hash = 696536510)
    public Books(Long id, String name, String author, String path, String album, int tag) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.path = path;
        this.album = album;
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
