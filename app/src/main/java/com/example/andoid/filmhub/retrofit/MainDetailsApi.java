package com.example.andoid.filmhub.retrofit;

import com.google.gson.annotations.SerializedName;


public class MainDetailsApi {

    @SerializedName("id")
    private final String id;

    @SerializedName("poster_path")
    private final String image;

    @SerializedName("title")
    private final String title;

    @SerializedName("name")
    private final String name;


    public MainDetailsApi(String id, String image, String title, String name) {
        this.id = id;
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

    public String getId() {
        return id;
    }
}
