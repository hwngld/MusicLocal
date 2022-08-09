package com.sildev.musiclocal.model;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favouriteSongTable")
public class Song {
    private String name;
    private String album;
    private String singer;
    private String path;
    private int duration;
    @PrimaryKey
    private long id;
    private long albumId;
    private String albumArt;

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    @Ignore
    public Song() {
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Song(String name, String album, String singer, String path, int duration, long id, long albumId, String albumArt) {
        this.name = name;
        this.album = album;
        this.singer = singer;
        this.path = path;
        this.duration = duration;
        this.id = id;
        this.albumId = albumId;
        this.albumArt = albumArt;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(Uri albumArt) {
        this.albumArt = albumArt.toString();
    }
}
