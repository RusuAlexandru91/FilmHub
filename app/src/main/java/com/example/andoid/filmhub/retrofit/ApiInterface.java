package com.example.andoid.filmhub.retrofit;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("trending/movie/week?api_key=" + ApiClient.API_KEY)
    Call<MainResultsApi> getTrandingMovies(@Query("page") int page);

    @GET("movie/popular?api_key=" + ApiClient.API_KEY)
    Call<MainResultsApi> getPopularMovies(@Query("page") int page);

    @GET("movie/top_rated?api_key=" + ApiClient.API_KEY)
    Call<MainResultsApi> getTopRatedMovies(@Query("page") int page);

    @GET("movie/upcoming?api_key=" + ApiClient.API_KEY)
    Call<MainResultsApi> getUpcomingMovies(@Query("page") int page);

    // Shows
    @GET("trending/tv/week?api_key=" + ApiClient.API_KEY)
    Call<MainResultsApi> getTrandingSeries(@Query("page") int page);

    @GET("tv/popular?api_key=" + ApiClient.API_KEY)
    Call<MainResultsApi> getpopularSeries(@Query("page") int page);

    @GET("tv/top_rated?api_key=" + ApiClient.API_KEY)
    Call<MainResultsApi> getTopRatedSeries(@Query("page") int page);

    @GET("tv/on_the_air?api_key=" + ApiClient.API_KEY)
    Call<MainResultsApi> getOnAirSeries(@Query("page") int page);

    // Discover Movies
    @GET("discover/movie?api_key=" + ApiClient.API_KEY + "&include_adult=false&include_video=false")
    Call<MainResultsApi> getDiscoverMovie(@Query("page") int page,
                                          @Query("sort_by") String number,
                                          @Query("year") String year,
                                          @Query("with_genres") String genres);

    // Discover Shows
    @GET("discover/tv?api_key=" + ApiClient.API_KEY + "&include_adult=false&include_video=false")
    Call<MainResultsApi> getDiscoverShows(@Query("page") int page,
                                          @Query("sort_by") String number,
                                          @Query("year") String year,
                                          @Query("with_genres") String genres);

    // Search Movies
    @GET("search/movie?api_key=" + ApiClient.API_KEY + "&page=1&include_adult=false")
    Call<MainResultsApi> getSearchMovies(@Query("query") CharSequence searchValue);

    // Search Shows
    @GET("search/tv?api_key=" + ApiClient.API_KEY + "&page=1&include_adult=false")
    Call<MainResultsApi> getSearchShows(@Query("query") CharSequence searchValue);

    // Details
    @GET("/3/movie/{Id}?api_key=" + ApiClient.API_KEY)
    Call<MovieDetailsApi> getDetailsMovie(@Path("Id") String itemId);

    @GET("/3/tv/{Id}?api_key=" + ApiClient.API_KEY)
    Call<ShowsDetailsApi> getDetailsShows(@Path("Id") String itemId);

    // Trailers
    @GET("/3/movie/{Id}/videos?api_key=" + ApiClient.API_KEY)
    Call<TrailerDetailsApi> getTrailerMovie(@Path("Id") String itemId);

    @GET("/3/tv/{Id}/videos?api_key=" + ApiClient.API_KEY)
    Call<TrailerDetailsApi> getTrailerShows(@Path("Id") String itemId);

    // Cast
    @GET("/3/movie/{Id}/credits?api_key=" + ApiClient.API_KEY)
    Call<CreditsApi> getCreditsMovie(@Path("Id") String itemId);

    @GET("/3/tv/{Id}/credits?api_key=" + ApiClient.API_KEY)
    Call<CreditsApi> getCreditsShow(@Path("Id") String itemId);
}
