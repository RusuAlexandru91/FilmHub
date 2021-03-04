package com.example.andoid.filmhub.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.andoid.filmhub.adapters.RecyclerAdapter;
import com.example.andoid.filmhub.R;
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


public class CategoryContentFragment extends Fragment {
    private static final String TAG = "CategotyFragment";

    ApiInterface apiInterface;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    int currentPage = 1;
    int totalPages;
    Call<MainResultsApi> call;

    private boolean isLoading;
    GridLayoutManager layoutManager;
    int categoryFragment;

    LottieAnimationView lottieloading;
    SwipeRefreshLayout pullToRefresh;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SwitchIntDef")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vertical_recycler, container, false);

        // Find and call pull to refresh method
        pullToRefresh = view.findViewById(R.id.pullToRefreshLayout);
        pulToRefresh();

        // Find lottie animation and set in in retrofit call ( OnResponse = GONE , onFailure = VISIBLE )
        lottieloading = (LottieAnimationView) view.findViewById(R.id.mainLottieLoadingAnimation);

        // Find and set RecylerView, Adapter and LayoutManager so it can be called in Retrofit call
        // Adapter defined and set initialy as blank so we can avoid errors and it will be populated in retrofit call with addlist*** method that is defined is RecylerAdapter .
        recyclerView = view.findViewById(R.id.verticalRecyclerView);
        recyclerAdapter = new RecyclerAdapter(new ArrayList<>());
        recyclerView.setAdapter(recyclerAdapter);
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        // calling below functions
        retrofitResponse();
        scrollListener();

        return view;
    }
    /**
     * Scroll listener with conditions for pagination
     **/
    public void scrollListener(){
        // Scroll Listener so we can detect elements and call pagination for retrofit .
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {

                // check if layoutmanager is null so we can exit the method .
                if (layoutManager == null) return;

                // check if boolean isloading is true so we can exit the mothod and so we avoid multiple calls on pagination .
                if (isLoading) return;

                // Conditions for pagination
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
    }

    /**
     * Pull to refresh method
     **/
    private void pulToRefresh() {
        pullToRefresh.setOnRefreshListener(() -> {
            //make the Retrofit call again on pull to refresh with all its conditions.
            retrofitResponse();
            pullToRefresh.setRefreshing(false);

        });
    }

    /**
     * Populate recycler with movies/Shows depending on selected menu
     **/
    private void retrofitResponse() {
        // Initialize the Api Interface
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        // Get key from MainFragment.
        checkConditions();

        // make the call
        call.enqueue(new Callback<MainResultsApi>() {
            @Override
            public void onResponse(@NotNull Call<MainResultsApi> call, @NotNull Response<MainResultsApi> response) {
                MainResultsApi mainResultsApi = response.body();
                assert mainResultsApi != null;
                List<MainDetailsApi> mainDetailsApi = mainResultsApi.getResults();
                recyclerAdapter.addPages(mainDetailsApi);
                // Set lottie animation to GONE if the response is recived/
                lottieloading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<MainResultsApi> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure response: " + t.getLocalizedMessage());
                // Set lottie animation to VISIBLE if response failed.
                lottieloading.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Pagination method to load more pages when scroll
     **/
    private void performPagination() {
        // Check conditions
        checkConditions();
        // Perform call with conditions
        call.enqueue(new Callback<MainResultsApi>() {
            @Override
            public void onResponse(@NotNull Call<MainResultsApi> call, @NotNull Response<MainResultsApi> response) {
                MainResultsApi mainResultsApi = response.body();
                assert mainResultsApi != null;
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
                Log.e(TAG, "onFailure pagination: " + t.getLocalizedMessage());
            }
        });
    }

    /**
     * Conditions to see how the frament is populated
     **/
    private void checkConditions() {
        // Conditions to populate every fragment by key ( stored in global int )
        assert getArguments() != null;
        categoryFragment = getArguments().getInt("categoryType");

        if (categoryFragment == 1) {
            call = apiInterface.getTrandingMovies(currentPage);
        } else if (categoryFragment == 2) {
            call = apiInterface.getPopularMovies(currentPage);
        } else if (categoryFragment == 3) {
            call = apiInterface.getTopRatedMovies(currentPage);
        } else if (categoryFragment == 4) {
            call = apiInterface.getUpcomingMovies(currentPage);
        } else if (categoryFragment == 5) {
            call = apiInterface.getTrandingSeries(currentPage);
        } else if (categoryFragment == 6) {
            call = apiInterface.getpopularSeries(currentPage);
        } else if (categoryFragment == 7) {
            call = apiInterface.getTopRatedSeries(currentPage);
        } else {
            call = apiInterface.getOnAirSeries(currentPage);
        }
    }

}





