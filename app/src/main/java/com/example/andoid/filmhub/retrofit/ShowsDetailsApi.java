package com.example.andoid.filmhub.retrofit;

import com.google.gson.annotations.SerializedName;

public class ShowsDetailsApi extends CommonDetailsApi {

    // Shows
    @SerializedName("first_air_date")
    private final String firstReleased;

    @SerializedName("last_air_date")
    private final String lastEpisode;

    @SerializedName("number_of_seasons")
    private final int totalSeasons;

    @SerializedName("in_production")
    private final String production;

    @SerializedName("name")
    private final String name;

    @SerializedName("number_of_episodes")
    private final int totalEpisodes;


    public ShowsDetailsApi(String firstReleased, String lastEpisode, int totalSeasons, String production, String name, int totalEpisodes) {
        this.firstReleased = firstReleased;
        this.lastEpisode = lastEpisode;
        this.totalSeasons = totalSeasons;
        this.production = production;
        this.name = name;
        this.totalEpisodes = totalEpisodes;
    }

    public String getFirstReleased() {
        return firstReleased;
    }

    public String getLastEpisode() {
        return lastEpisode;
    }

    public int getTotalSeasons() {
        return totalSeasons;
    }

    public String getProduction() {
        return production;
    }

    public String getName() {
        return name;
    }

    public int getTotalEpisodes() {
        return totalEpisodes;
    }
}
