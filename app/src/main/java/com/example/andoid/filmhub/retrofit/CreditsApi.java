package com.example.andoid.filmhub.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreditsApi {

    @SerializedName("cast")
    List<CreditsDetailsApi> cast;

    public CreditsApi(List<CreditsDetailsApi> cast) {
        this.cast = cast;
    }

    public List<CreditsDetailsApi> getCast() {
        return cast;
    }
}
