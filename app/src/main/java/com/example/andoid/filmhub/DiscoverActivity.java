package com.example.andoid.filmhub;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.andoid.filmhub.adapters.RecyclerAdapter;
import com.example.andoid.filmhub.retrofit.ApiClient;
import com.example.andoid.filmhub.retrofit.ApiInterface;
import com.example.andoid.filmhub.retrofit.MainDetailsApi;
import com.example.andoid.filmhub.retrofit.MainResultsApi;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DiscoverActivity extends AppCompatActivity {

    private static final String TAG = "DiscoverActivity";

    String numberMovies, orderByMovies, categoryMovies;
    String numberShows, orderByShows, categoryShows;

    ApiInterface apiInterface;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    Call<MainResultsApi> call;
    GridLayoutManager layoutManager;

    int getIntentKey;
    int currentPage = 1;
    int totalPages;
    private boolean isLoading;

    LottieAnimationView lottieloading;
    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vertical_recycler);

        // Getting the key
        getIntentKey = getIntent().getIntExtra("intentKey", 0);

        // refresh
        pullToRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        response();
    }

    /**
     * method to perform Retrofit call , check conditions , pagination
     **/
    private void response() {

        // Linding and setting recycler, adapter and layoutManager
        recyclerView = findViewById(R.id.verticalRecyclerView);
        recyclerAdapter = new RecyclerAdapter(new ArrayList<>());
        recyclerView.setAdapter(recyclerAdapter);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        // Scroll Listener so we can detect elements and call pagination for retrofit .
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {

                // check if layoutmanager is null so we can exit the method .
                if (layoutManager == null) return;

                // check if boolean isloading is true so we can exit the mothod and so we avoid multiple calls on pagination .
                if (isLoading) return;

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    currentPage++;
                    isLoading = true;
                    performPagination();
                }
            }
        });

        // Setting the interface
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        // Check conditions
        checkPreferenceConditions();

        // Performing call
        call.enqueue(new Callback<MainResultsApi>() {
            @Override
            public void onResponse(@NotNull Call<MainResultsApi> call, @NotNull Response<MainResultsApi> response) {
                MainResultsApi mainResultsApi = response.body();
                List<MainDetailsApi> mainDetailsApi = mainResultsApi.getResults();
                recyclerAdapter.addPages(mainDetailsApi);

                // Conditions to display error message is there are no results
                if (mainDetailsApi.size() == 0) {
                    TextView textView = (TextView) findViewById(R.id.errorTextView);
                    textView.setVisibility(View.VISIBLE);
                } else {
                    TextView textView = (TextView) findViewById(R.id.errorTextView);
                    textView.setVisibility(View.INVISIBLE);
                }

                // setVisibility GONE for lottie on positive response
                loadingIndicatorGone();
            }

            @Override
            public void onFailure(@NotNull Call<MainResultsApi> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());

                // Setting VISIBLE for lottie if failure
                loadingIndicatorVisible();
            }
        });
    }

    /**
     * method to perform pagination ( Load next page )
     **/
    private void performPagination() {

        //Check conditions
        checkPreferenceConditions();

        // Perform call
        call.enqueue(new Callback<MainResultsApi>() {
            @Override
            public void onResponse(@NotNull Call<MainResultsApi> call, @NotNull Response<MainResultsApi> response) {
                MainResultsApi mainResultsApi = response.body();
                currentPage = mainResultsApi.getCurrentPage();
                totalPages = mainResultsApi.getTotalPages();
                if (currentPage < totalPages) {
                    List<MainDetailsApi> mainDetailsApi = mainResultsApi.getResults();
                    recyclerAdapter.addPages(mainDetailsApi);
                }
                isLoading = false;
            }

            @Override
            public void onFailure(@NotNull Call<MainResultsApi> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    /**
     * Conditions to hold preference values
     **/
    private void checkPreferenceConditions() {
        if (getIntentKey == 0) {
            getSupportActionBar().setTitle("Discover Movies");
            preferenceMovies();
            call = apiInterface.getDiscoverMovie(currentPage, orderByMovies, numberMovies, categoryMovies);
        } else {
            getSupportActionBar().setTitle("Discover Shows");
            preferenceShows();
            call = apiInterface.getDiscoverShows(currentPage, orderByShows, numberShows, categoryShows);
        }
    }

    /**
     * Preference for shows
     **/
    private void preferenceShows() {
        SharedPreferences sharedPrefsShows = PreferenceManager.getDefaultSharedPreferences(this);

        numberShows = sharedPrefsShows.getString(
                getString(R.string.settings_min_number_key_shows),
                getString(R.string.settings_min_number_default_shows));

        orderByShows = sharedPrefsShows.getString(
                getString(R.string.settings_order_by_key_shows),
                getString(R.string.settings_order_by_default_shows));

        categoryShows = sharedPrefsShows.getString(
                getString(R.string.settings_category_by_key_shows),
                getString(R.string.settings_category_by_default_shows));
    }

    /**
     * Preference for movies
     **/
    private void preferenceMovies() {
        SharedPreferences sharedPrefsMovies = PreferenceManager.getDefaultSharedPreferences(this);
        numberMovies = sharedPrefsMovies.getString(
                getString(R.string.settings_min_number_key_movies),
                getString(R.string.settings_min_number_default_movies));

        orderByMovies = sharedPrefsMovies.getString(
                getString(R.string.settings_order_by_key_movies),
                getString(R.string.settings_order_by_default_movies));

        categoryMovies = sharedPrefsMovies.getString(
                getString(R.string.settings_category_by_key_movies),
                getString(R.string.settings_category_by_default_movies));
    }

    /**
     * inflate options menu
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int menuId;
        if (getIntentKey == 0) {
            menuId = R.menu.discover_settings;
        } else {
            menuId = R.menu.discover_settings_secound;
        }
        getMenuInflater().inflate(menuId, menu);
        return true;
    }

    /**
     * Behavior of options menu
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.discover_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            settingsIntent.putExtra("categoryDiscoverKey", 0);
            startActivity(settingsIntent);
            return true;
        } else if (id == R.id.discover_settings_secound) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            settingsIntent.putExtra("categoryDiscoverKey", 1);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Lotie loading GONE
     **/
    public void loadingIndicatorGone() {
        lottieloading = (LottieAnimationView) findViewById(R.id.mainLottieLoadingAnimation);
        lottieloading.setVisibility(View.GONE);
    }

    /**
     * Lottie loading Visible
     **/
    private void loadingIndicatorVisible() {
        lottieloading = (LottieAnimationView) findViewById(R.id.mainLottieLoadingAnimation);
        lottieloading.setVisibility(View.VISIBLE);
    }

    /**
     * Refresh
     **/
    private void pullToRefresh() {
        pullToRefresh = findViewById(R.id.pullToRefreshLayout);
        pullToRefresh.setOnRefreshListener(() -> {
            response();
            pullToRefresh.setRefreshing(false);

        });
    }
}