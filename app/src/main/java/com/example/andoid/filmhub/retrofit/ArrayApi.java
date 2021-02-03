package com.example.andoid.filmhub.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArrayApi {

    @SerializedName("id")
    private int id;

    @SerializedName("poster_path")
    private String image;

    @SerializedName("backdrop_path")
    private String backdrop;

    @SerializedName("title")
    private String title;

    @SerializedName("name")
    private String name;

    @SerializedName("release_date")
    private String releaseDatemovie;

    @SerializedName("first_air_date")
    private String realeaseDateShows;

    @SerializedName("genre_ids")
    private List<Integer> genres;

    @SerializedName("overview")
    private String overview;

    @SerializedName("vote_average")
    private double vote;



    public ArrayApi(String image, String title, String name) {
        this.image = image;
        this.title = title;
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }
}
