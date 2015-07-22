package com.example.kieuptn.demoapplication;

import java.io.Serializable;

/**
 * Created by KieuPTN on 7/9/2015.
 */
public class ObjectItem implements Serializable{
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private String imageURLString;
    public String getImageURLString() {
        return imageURLString;
    }

    public void setImageURLString(String imageURLString) {
        this.imageURLString = imageURLString;
    }

    private String song;
    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }


    private String artist;
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    private String linkString;

    public String getLinkString() {
        return linkString;
    }

    public void setLinkString(String linkString) {
        this.linkString = linkString;
    }

    private String type;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
