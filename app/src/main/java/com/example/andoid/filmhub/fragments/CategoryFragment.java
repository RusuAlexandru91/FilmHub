package com.example.andoid.filmhub.fragments;

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

import com.example.andoid.filmhub.adapters.RecyclerAdapter;
import com.example.andoid.filmhub.R;
import com.example.andoid.filmhub.retrofit.ApiClient;
import com.example.andoid.filmhub.retrofit.ApiInterface;
import com.example.andoid.filmhub.retrofit.ArrayApi;
import com.example.andoid.filmhub.retrofit.ObjectApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryFragment extends Fragment {
    private static final String TAG = "CategotyFragment";

    ApiInterface apiInterface;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    int currentPage = 1;
    int totalPages;
    Call<ObjectApi> call;

    private boolean isLoading;
    GridLayoutManager layoutManager;
    int categoryFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vertical_recycler, container, false);

        // categoryFragment defined as global so we can call it after the view is created .
        categoryFragment =  getArguments().getInt("categoryType");

        recyclerView = view.findViewById(R.id.vertical_recycler);

        // Adapter defined and set initialy as blank so we can avoid errors and it will be populated in retrofit call with addlist*** method that is defined is RecylerAdapter .
        recyclerAdapter = new RecyclerAdapter(new ArrayList<>());
        recyclerView.setAdapter(recyclerAdapter);

        // Setting LayoutManager based on orientation ( portrait/landscape )
        switch (getResources().getConfiguration().orientation) {
            case 1:
                layoutManager = new GridLayoutManager(getContext(), 2);
                recyclerView.setLayoutManager(layoutManager);
                break;
            case 2:
                layoutManager = new GridLayoutManager(getContext(), 4);
                recyclerView.setLayoutManager(layoutManager);
                break;
        }

        // Scroll Listener so we can detect elements and call pagination for retrofit .
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                // check if layoutmanager is null so we can exit the method .
                if(layoutManager == null) return;

                // check if boolean isloading is true so we can exit the mothod and so we avoid multiple calls on pagination .
                if(isLoading) return;

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


        // Initialize the Api Interface
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        // Get key from MainFragment.
        checkConditions();


        call.enqueue(new Callback<ObjectApi>() {
            @Override
            public void onResponse(Call<ObjectApi> call, Response<ObjectApi> response) {
                ObjectApi objectApi = response.body();
                List<ArrayApi> arrayApi = objectApi.getResults();
                recyclerAdapter.addPages(arrayApi);
            }

            @Override
            public void onFailure(Call<ObjectApi> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
        return view;
    }


    private void performPagination() {
        checkConditions();
        call.enqueue(new Callback<ObjectApi>() {
            @Override
            public void onResponse(Call<ObjectApi> call, Response<ObjectApi> response) {
                ObjectApi objectApi = response.body();
                currentPage = objectApi.getCurrentPage();
                totalPages = objectApi.getTotalPages();
                if (currentPage < totalPages) {
                    List<ArrayApi> arrayApi = objectApi.getResults();
                    recyclerAdapter.addPages(arrayApi);
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<ObjectApi> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
    private void checkConditions(){

        // If/else to populate every fragment
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





