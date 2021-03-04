package com.example.andoid.filmhub.retrofit;

import com.google.gson.annotations.SerializedName;

public class MovieDetailsApi extends CommonDetailsApi {

    // Movies
    @SerializedName("release_date")
    private final String releaseDatemovie;

    @SerializedName("runtime")
    private final int runtime;

    @SerializedName("budget")
    private final int budget;

    @SerializedName("title")
    private final String title;


    public MovieDetailsApi(String releaseDatemovie, int runtime, int budget, String title) {
        this.releaseDatemovie = releaseDatemovie;
        this.runtime = runtime;
        this.budget = budget;
        this.title = title;
    }

    public String getReleaseDatemovie() {
        return releaseDatemovie;
    }

    public int getRuntime() {
        return runtime;
    }

    public int getBudget() {
        return budget;
    }

    public String getTitle() {
        return title;
    }

}
