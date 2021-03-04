package com.example.andoid.filmhub.retrofit;

import com.google.gson.annotations.SerializedName;

public class GenreDetailsApi {
    @SerializedName("name")
    String numeDeGen;

    public GenreDetailsApi(String genres) {
        this.numeDeGen = genres;
    }

    public String getNumeDeGen() {
        return numeDeGen;
    }
}
