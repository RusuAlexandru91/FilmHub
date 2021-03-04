package com.example.andoid.filmhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.andoid.filmhub.adapters.RecyclerAdapterCast;
import com.example.andoid.filmhub.retrofit.ApiClient;
import com.example.andoid.filmhub.retrofit.ApiInterface;
import com.example.andoid.filmhub.retrofit.CommonDetailsApi;
import com.example.andoid.filmhub.retrofit.CreditsDetailsApi;
import com.example.andoid.filmhub.retrofit.CreditsApi;
import com.example.andoid.filmhub.retrofit.GenreDetailsApi;
import com.example.andoid.filmhub.retrofit.MovieDetailsApi;
import com.example.andoid.filmhub.retrofit.TrailerDetailsApi;
import com.example.andoid.filmhub.retrofit.ShowsDetailsApi;
import com.example.andoid.filmhub.retrofit.TrailerApi;
import com.example.andoid.filmhub.roomdb.Items;
import com.example.andoid.filmhub.roomdb.ItemsDatabase;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressLint("SetTextI18n")
public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = "DetailsActivity";
    RecyclerAdapterCast recyclerAdapterCast;
    RecyclerView recyclerViewCast;
    ApiInterface apiInterface;

    Call<ShowsDetailsApi> callShows;
    Call<MovieDetailsApi> callMovies;
    Call<TrailerDetailsApi> callTrailer;
    Call<CreditsApi> callCast;

    CommonDetailsApi testObject;
    String id;
    String key;
    boolean typeValidation;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getSupportActionBar().hide();

        fab = findViewById(R.id.floatingActionButton);

        Intent intent = getIntent();
        id = intent.getStringExtra("titleId");
        typeValidation = intent.getBooleanExtra("typeValidation", false);

        onBackButtonPressed();
        castCallBack();
        trailerCallBack();
        detailCallBacks();
        fabFunction();
        setFabIconStatus();

    }

    private void setFabIconStatus() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSaved = checkItemIsSaved();

                if (!isSaved) {
                    fab.setImageResource(R.drawable.ic_favorite_empty);
                } else {
                    fab.setImageResource(R.drawable.ic_favorite);
                }
            }
        });
        thread.start();
    }

    private boolean checkItemIsSaved() {

        Items items = ItemsDatabase.getInstance(DetailsActivity.this).userDao().findItemId(id);
        return items != null;
    }

    private void fabFunction() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean isSaved = checkItemIsSaved();

                        if (!isSaved) {
                            Items testItems = null;
                            if (testObject instanceof ShowsDetailsApi) {
                                ShowsDetailsApi showDetails = (ShowsDetailsApi) testObject;
                                testItems = new Items(id, showDetails.getName(), showDetails.getBackdrop(), showDetails.getOverview(), false);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(DetailsActivity.this, showDetails.getName() + " saved to Favorites", Toast.LENGTH_SHORT).show();
                                        fab.setImageResource(R.drawable.ic_favorite);
                                    }
                                });

                            } else if (testObject instanceof MovieDetailsApi) {
                                MovieDetailsApi showDetails = (MovieDetailsApi) testObject;
                                testItems = new Items(id, showDetails.getTitle(), showDetails.getBackdrop(), showDetails.getOverview(), true);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(DetailsActivity.this, showDetails.getTitle() + " saved to Favorites", Toast.LENGTH_SHORT).show();
                                        fab.setImageResource(R.drawable.ic_favorite);
                                    }
                                });

                            } else {
                                Log.d(TAG, "Wrong instance ...." + testObject);
                            }
                            long insertId = ItemsDatabase.getInstance(DetailsActivity.this).userDao().insert(testItems);
                            Log.d(TAG, "insert id este egla cu:   -> " + insertId);
                        } else {
                            ItemsDatabase.getInstance(DetailsActivity.this).userDao().deleteItemWhereId(id);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fab.setImageResource(R.drawable.ic_favorite_empty);
                                    Toast.makeText(DetailsActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                thread.start();
            }
        });
    }

    private void setTitleFromResponseResult(String title) {
        CollapsingToolbarLayout headerTitle = (CollapsingToolbarLayout) findViewById(R.id.toolbarTitle);
        headerTitle.setTitle(title);
    }

    private void setGenresFromResponseResult(List<GenreDetailsApi> listOfGenres) {

        TextView genresTextView = (TextView) findViewById(R.id.genders_textView);
        String genresTextViewFinal = "";
        for (int i = 0; i < listOfGenres.size(); i++) {
            String genresBuilder = listOfGenres.get(i).getNumeDeGen();
            genresTextViewFinal += genresBuilder + " | ";
        }
        genresTextViewFinal = genresTextViewFinal.substring(0, genresTextViewFinal.length() - 2);
        genresTextView.setText(genresTextViewFinal);
    }

    private void setOverviewFromResponseResult(String overview) {
        TextView textView = (TextView) findViewById(R.id.overviewContentTextView);
        textView.setText(overview);

    }

    private void setScoreFromResponseResult(String score) {
        TextView textView = (TextView) findViewById(R.id.vote_average_textView);
        textView.setText(score);
        if (score.isEmpty()) {
            textView.setText(getString(R.string.noRateig));
        }
    }

    private void setImageFromResponseResult(String url) {
        ImageView imageView = (ImageView) findViewById(R.id.imageHeader);

        String imageUrl = ApiClient.BASE_IMG_URL + url;
        Glide.with(this).load(imageUrl).into(imageView);
    }

    private void setRuntimeFromResposeResult(int runtime) {
        TextView textView = (TextView) findViewById(R.id.information_placeholder);

        String minutes = Integer.toString(runtime % 60);
        minutes = minutes.length() == 1 ? "0" + minutes : minutes;

        textView.setText(getString(R.string.duration_title) + runtime / 60 + "H " + ": " + minutes + " Min");
        if (runtime == 0) {
            textView.setVisibility(View.GONE);
        }
    }

    private void setDateFromResponseResult(String date) {
        TextView textView = (TextView) findViewById(R.id.information_placeholder2);
        textView.setText(getString(R.string.release_date_title) + date);
        if (date.isEmpty()) {
            textView.setVisibility(View.GONE);
        }
    }

    private void setBugetFromResponseResult(int buget) {
        TextView textView = (TextView) findViewById(R.id.information_placeholder3);
        String bugetFormat = Integer.toString(buget);

        double amount = Double.parseDouble(bugetFormat);
        DecimalFormat formatter = new DecimalFormat("#,###");
        textView.setText(getString(R.string.buget_title) + formatter.format(amount) + " $");

        if (buget == 0) {
            textView.setVisibility(View.GONE);
        }
    }

    private void setSeasonsFromResponseResult(int seasons) {
        TextView textView = (TextView) findViewById(R.id.information_placeholder3);
        textView.setText(getString(R.string.seasons_title) + seasons);
        if (seasons == 0) {
            textView.setVisibility(View.GONE);
        }
    }

    private void setStatusFromResponseResult(String status) {
        TextView textView = (TextView) findViewById(R.id.information_placeholder4);
        textView.setText(getString(R.string.status_title) + status);
        if (status.isEmpty()) {
            textView.setVisibility(View.GONE);
        }
    }

    private void setFirstReleaseDateFromResponseResult(String firstReleaseDate) {
        TextView textView = (TextView) findViewById(R.id.information_placeholder);
        textView.setText(getString(R.string.first_episode_date) + firstReleaseDate);
    }

    private void setTaglineFromResponseResult(String tagline) {
        TextView textView = (TextView) findViewById(R.id.information_placeholder5);
        textView.setText(getString(R.string.tagline_title) + tagline);
        if (tagline.isEmpty()) {
            textView.setVisibility(View.GONE);
        }
    }

    private void setLastReleaseDateFromResponseResult(String lastReleaseDate) {
        TextView textView = (TextView) findViewById(R.id.information_placeholder2);
        textView.setText(getString(R.string.last_episode_date) + lastReleaseDate);
        if (lastReleaseDate.isEmpty()) {
            textView.setVisibility(View.GONE);
        }
    }

    private void setEpsodesFromResponseResult(int episodes) {
        TextView textView = (TextView) findViewById(R.id.episodes);
        if (episodes >= 0) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(getString(R.string.episodes_title) + episodes);
        }

    }

    private void setTrailerFromResponseResult(String url) {
        ImageView imageView = (ImageView) findViewById(R.id.trailerImageView);

        String imageUrl = ApiClient.BASE_IMG_URL + url;
        Glide.with(this).load(imageUrl).into(imageView);
    }

    private void trailerCallBack() {

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        if (!typeValidation) {
            Log.d(TAG, "trailerCallBack: " + typeValidation);
            callTrailer = apiInterface.getTrailerMovie(id);
        } else {
            callTrailer = apiInterface.getTrailerShows(id);
            Log.d(TAG, "trailerCallBack: " + typeValidation);
        }

        callTrailer.enqueue(new Callback<TrailerDetailsApi>() {
            @Override
            public void onResponse(@NotNull Call<TrailerDetailsApi> call, @NotNull Response<TrailerDetailsApi> response) {
                TrailerDetailsApi trailerDetailsApi = response.body();
                List<TrailerApi> trailerApi = trailerDetailsApi.getTrailerResults();
                key = trailerApi.get(0).getTrailerKey();
                Log.d(TAG, "onResponse key: " + key);

                ImageView imageView = (ImageView) findViewById(R.id.trailerImageViewPlay);
                imageView.setClickable(true);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setPackage("com.google.android.youtube");
                        intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + key));
                        intent.putExtra("force_fullscreen", true);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(@NotNull Call<TrailerDetailsApi> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void detailCallBacks() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        callShows = apiInterface.getDetailsShows(id);
        callMovies = apiInterface.getDetailsMovie(id);

        if (typeValidation) {
            callShows.enqueue(new Callback<ShowsDetailsApi>() {
                @Override
                public void onResponse(@NotNull Call<ShowsDetailsApi> call, @NotNull Response<ShowsDetailsApi> response) {
                    Log.d(TAG, "onResponse: " + response.body().getName());
                    ShowsDetailsApi showDetails = response.body();
                    testObject = showDetails;
                    // Image
                    String string = showDetails.getBackdrop();
                    setImageFromResponseResult(string);
                    //Name
                    setTitleFromResponseResult(showDetails.getName());
                    //Tagline
                    String tagline = showDetails.getTagline();
                    setTaglineFromResponseResult(tagline);
                    // Status
                    String status = showDetails.getStatus();
                    setStatusFromResponseResult(status);
                    //Genres
                    List<GenreDetailsApi> genres = showDetails.getGenres();
                    setGenresFromResponseResult(genres);
                    // Overview
                    String overview = showDetails.getOverview();
                    setOverviewFromResponseResult(overview);
                    // Score
                    double score = showDetails.getVote();
                    setScoreFromResponseResult(Double.toString(score));
                    //Seasons
                    int seasons = showDetails.getTotalSeasons();
                    setSeasonsFromResponseResult(seasons);
                    //Episodes
                    int episodes = showDetails.getTotalEpisodes();
                    setEpsodesFromResponseResult(episodes);
                    // First Release Date
                    String firstRelease = showDetails.getFirstReleased();
                    setFirstReleaseDateFromResponseResult(firstRelease);
                    // Last Release Date
                    String lastReleaseDate = showDetails.getLastEpisode();
                    setLastReleaseDateFromResponseResult(lastReleaseDate);
                    // Trailer
                    String trailerString = showDetails.getBackdrop();
                    setTrailerFromResponseResult(trailerString);

                }

                @Override
                public void onFailure(@NotNull Call<ShowsDetailsApi> call, @NotNull Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                }
            });
        } else {
            callMovies.enqueue(new Callback<MovieDetailsApi>() {
                @Override
                public void onResponse(@NotNull Call<MovieDetailsApi> call, @NotNull Response<MovieDetailsApi> response) {
                    Log.d(TAG, "onResponse Title : " + response.body().getTitle());
                    MovieDetailsApi movieDetails = response.body();
                    testObject = movieDetails;
                    // Image
                    String string = movieDetails.getBackdrop();
                    setImageFromResponseResult(string);
                    // Title
                    setTitleFromResponseResult(movieDetails.getTitle());
                    //Tagline
                    String tagline = movieDetails.getTagline();
                    setTaglineFromResponseResult(tagline);
                    // Status
                    String status = movieDetails.getStatus();
                    setStatusFromResponseResult(status);
                    // Genres
                    List<GenreDetailsApi> genres = movieDetails.getGenres();
                    setGenresFromResponseResult(genres);
                    // Overview
                    String overview = movieDetails.getOverview();
                    setOverviewFromResponseResult(overview);
                    // Score
                    double score = movieDetails.getVote();
                    setScoreFromResponseResult(Double.toString(score));
                    //Buget
                    int buget = movieDetails.getBudget();
                    setBugetFromResponseResult(buget);
                    // Date
                    String date = movieDetails.getReleaseDatemovie();
                    setDateFromResponseResult(date);
                    // Runtime
                    int runtime = movieDetails.getRuntime();
                    setRuntimeFromResposeResult(runtime);
                    // Trailer
                    String trailerString = movieDetails.getBackdrop();
                    setTrailerFromResponseResult(trailerString);
                }

                @Override
                public void onFailure(@NotNull Call<MovieDetailsApi> call, @NotNull Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                }
            });
        }
    }

    private void onBackButtonPressed() {

        ImageView imageView = (ImageView) findViewById(R.id.back_button);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void castCallBack() {

        recyclerViewCast = findViewById(R.id.recyclerViewCast);
        recyclerViewCast.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCast.setAdapter(recyclerAdapterCast);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        if (typeValidation) {
            callCast = apiInterface.getCreditsShow(id);
        } else {
            callCast = apiInterface.getCreditsMovie(id);
        }

        callCast.enqueue(new Callback<CreditsApi>() {
            @Override
            public void onResponse(@NotNull Call<CreditsApi> call, @NotNull Response<CreditsApi> response) {
                CreditsApi creditsApi = response.body();
                List<CreditsDetailsApi> creditsDetailsApis = creditsApi.getCast();
                recyclerAdapterCast = new RecyclerAdapterCast(creditsDetailsApis);
                recyclerViewCast.setAdapter(recyclerAdapterCast);
                Log.d(TAG, "onResponse: Onject" + creditsApi);
                Log.d(TAG, "onResponse: Array" + creditsDetailsApis);
            }

            @Override
            public void onFailure(@NotNull Call<CreditsApi> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
}
