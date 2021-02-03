package com.example.andoid.filmhub.retrofit;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("trending/movie/week?api_key="+ ApiClient.API_KEY)
    Call<ObjectApi> getTrandingMovies(@Query("page") int page);


    @GET("movie/popular?api_key="+ ApiClient.API_KEY)
    Call<ObjectApi> getPopularMovies(@Query("page") int page);

    @GET("movie/top_rated?api_key="+ ApiClient.API_KEY)
    Call<ObjectApi> getTopRatedMovies(@Query("page") int page);

    @GET("movie/upcoming?api_key="+ ApiClient.API_KEY)
    Call<ObjectApi> getUpcomingMovies(@Query("page") int page);

    // Shows
    @GET("trending/tv/week?api_key="+ ApiClient.API_KEY)
    Call<ObjectApi> getTrandingSeries(@Query("page") int page);

    @GET("tv/popular?api_key="+ ApiClient.API_KEY)
    Call<ObjectApi> getpopularSeries(@Query("page") int page);

    @GET("tv/top_rated?api_key="+ ApiClient.API_KEY)
    Call<ObjectApi> getTopRatedSeries(@Query("page") int page);

    @GET("tv/on_the_air?api_key="+ ApiClient.API_KEY)
    Call<ObjectApi> getOnAirSeries(@Query("page") int page);



}
