package com.example.andoid.filmhub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.andoid.filmhub.ContainerActivity;
import com.example.andoid.filmhub.R;
import com.example.andoid.filmhub.adapters.TabAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class SearchFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabs, container, false);


        // Finding the Views
        ViewPager viewPager = view.findViewById(R.id.pagerCategory);
        TabLayout tableLayout = view.findViewById(R.id.tabsCategory);

        TabAdapter adapter = new TabAdapter(getFragmentManager());

        SearchContentFragment searchMovieFragment = fragmentBundleGenerator(1);
        SearchContentFragment searchShowsFragment = fragmentBundleGenerator(2);

        adapter.addFragment(searchMovieFragment, "Movies");
        adapter.addFragment(searchShowsFragment, "Shows");

        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);
        return view;

    }
    @Override
    public void onResume() {
        super.onResume();
        // Setting the action bar title
        ((ContainerActivity) Objects.requireNonNull(getActivity())).setActionBarTitle("Search");
    }

    private SearchContentFragment fragmentBundleGenerator(int searchType){
        // Generate a Bundle so we know how to populate the fragments
        SearchContentFragment searchContentFragment = new SearchContentFragment();
        Bundle bundleCategory = new Bundle();
        bundleCategory.putInt("searchType", searchType);
        searchContentFragment.setArguments(bundleCategory);
        return searchContentFragment;
    }
}
