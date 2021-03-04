package com.example.andoid.filmhub.retrofit;

import com.google.gson.annotations.SerializedName;


public class TrailerApi {

    @SerializedName("key")
    private final String trailerKey;

    public TrailerApi(String trailerKey) {
        this.trailerKey = trailerKey;
    }

    public String getTrailerKey() {
        return trailerKey;
    }
}
