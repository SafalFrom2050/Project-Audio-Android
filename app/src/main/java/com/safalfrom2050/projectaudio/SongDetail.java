package com.safalfrom2050.projectaudio;

public class SongDetail {

    String title, artist, album;

    int duration;

    long id;

    public SongDetail(String title, String artist, String album, int duration, long id){
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public int getDuration() {
        return duration;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
