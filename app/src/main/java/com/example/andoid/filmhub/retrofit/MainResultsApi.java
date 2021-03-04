package com.example.andoid.filmhub.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainResultsApi {

    @SerializedName("page")
    int currentPage;

    @SerializedName("total_pages")
    int totalPages;

    @SerializedName("results")
    List<MainDetailsApi> results;

    public MainResultsApi(int currentPage, int totalPages, List<MainDetailsApi> results) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.results = results;

    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<MainDetailsApi> getResults() {
        return results;
    }

}
