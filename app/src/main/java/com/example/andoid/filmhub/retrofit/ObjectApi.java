package com.example.andoid.filmhub.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ObjectApi {

    @SerializedName("page")
    int currentPage;

    @SerializedName("total_pages")
    int totalPages;

    @SerializedName("results")
    List<ArrayApi> results;

    public ObjectApi(int currentPage, int totalPages, List<ArrayApi> results) {
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

    public List<ArrayApi> getResults() {
        return results;
    }
}
