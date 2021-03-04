package com.example.andoid.filmhub.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerDetailsApi {
    @SerializedName("results")
    List<TrailerApi> trailerResults;

    public TrailerDetailsApi(List<TrailerApi> trailerResults) {
        this.trailerResults = trailerResults;
    }

    public List<TrailerApi> getTrailerResults() {
        return trailerResults;
    }
}
