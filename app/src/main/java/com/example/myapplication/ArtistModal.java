package com.example.myapplication;

import java.util.ArrayList;

public class ArtistModal {
    public ArrayList<String> albumsImgs;

    public String artistID;

    public String artistImg;

    public String artistName;

    public int followers;

    public int popularity;

    public String spotifyLink;


    public ArtistModal(ArrayList<String> albumsImgs, String artistID, String artistImg, String artistName, int followers, int popularity, String spotifyLink) {
        this.albumsImgs = albumsImgs;
        this.artistID = artistID;
        this.artistImg = artistImg;
        this.artistName = artistName;
        this.followers = followers;
        this.popularity = popularity;
        this.spotifyLink = spotifyLink;
    }


    public ArrayList<String> getAlbumsImgs() {
        return albumsImgs;
    }

    public String getArtistID() {
        return artistID;
    }

    public String getArtistImg() {
        return artistImg;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getFollowers() {
        return followers;
    }

    public int getPopularity() {
        return popularity;
    }

    public String getSpotifyLink() {
        return spotifyLink;
    }
}
