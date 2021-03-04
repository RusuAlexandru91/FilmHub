package com.example.andoid.filmhub.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonDetailsApi {
    // Both categories
    @SerializedName("backdrop_path")
    private String backdrop;

    @SerializedName("genres")
    private List<GenreDetailsApi> genres;

    @SerializedName("vote_average")
    private double vote;

    @SerializedName("tagline")
    private String tagline;

    @SerializedName("status")
    private String status;

    @SerializedName("overview")
    private String overview;

    public String getBackdrop() {
        return backdrop;
    }

    public List<GenreDetailsApi> getGenres() {
        return genres;
    }

    public double getVote() {
        return vote;
    }

    public String getTagline() {
        return tagline;
    }

    public String getStatus() {
        return status;
    }

    public String getOverview() {
        return overview;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public void setGenres(List<GenreDetailsApi> genres) {
        this.genres = genres;
    }

    public void setVote(double vote) {
        this.vote = vote;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
