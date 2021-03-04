package com.example.andoid.filmhub.retrofit;

import com.google.gson.annotations.SerializedName;

public class CreditsDetailsApi {

    @SerializedName("profile_path")
    String actorPhoto;

    @SerializedName("name")
    String actorName;

    @SerializedName("character")
    String actorMovieName;

    public CreditsDetailsApi(String actorPhoto, String actorName, String actorMovieName) {
        this.actorPhoto = actorPhoto;
        this.actorName = actorName;
        this.actorMovieName = actorMovieName;
    }

    public String getActorPhoto() {
        return actorPhoto;
    }

    public String getActorName() {
        return actorName;
    }

    public String getActorMovieName() {
        return actorMovieName;
    }
}
