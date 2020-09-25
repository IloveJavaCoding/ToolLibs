package com.nepalese.toollibs.Bean;

public class Song extends BaseBean{
    private String title;
    private String displayName;
    private String artist;
    private String album;
    private String path;
    private int duration;

    public Song() {
    }

    @Override
    String getString() {
        return null;
    }

    public Song(String title, String displayName, String artist, String album, String path, int duration) {
        this.title = title;
        this.displayName = displayName;
        this.artist = artist;
        this.album = album;
        this.path = path;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
