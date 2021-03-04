package com.example.andoid.filmhub.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andoid.filmhub.R;
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


public class SearchContentFragment extends Fragment {

    private static final String TAG = "SearchContentFragment";

    int searchContentFragment;

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ApiInterface apiInterface;
    Call<MainResultsApi> call;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_layout, container, false);
        // Get the key as global int
        assert getArguments() != null;
        searchContentFragment = getArguments().getInt("searchType");

        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(true);
        searchView.requestFocus();
        if (searchContentFragment == 1) {
            searchView.setQueryHint("Search Movies");
        } else if (searchContentFragment == 2) {
            searchView.setQueryHint("Search Shows");
        }


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                recyclerView = view.findViewById(R.id.searchRecyclerView);
                recyclerAdapter = new RecyclerAdapter(new ArrayList<>());
                recyclerView.setAdapter(recyclerAdapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));


                apiInterface = ApiClient.getClient().create(ApiInterface.class);
                if (searchContentFragment == 1) {
                    call = apiInterface.getSearchMovies(query);
                } else {
                    call = apiInterface.getSearchShows(query);
                }

                call.enqueue(new Callback<MainResultsApi>() {
                    @Override
                    public void onResponse(@NotNull Call<MainResultsApi> call, @NotNull Response<MainResultsApi> response) {
                        MainResultsApi mainResultsApi = response.body();
                        assert mainResultsApi != null;
                        List<MainDetailsApi> mainDetailsApi = mainResultsApi.getResults();
                        recyclerAdapter = new RecyclerAdapter(mainDetailsApi);
                        recyclerView.setAdapter(recyclerAdapter);
                        if (mainDetailsApi.size() == 0) {
                            TextView textView = view.findViewById(R.id.errorTextView);
                            textView.setVisibility(View.VISIBLE);
                        } else {
                            TextView textView = view.findViewById(R.id.errorTextView);
                            textView.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<MainResultsApi> call, @NotNull Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                        Toast.makeText(getActivity(), "Something went wrong !", Toast.LENGTH_LONG).show();
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return view;
    }
}
