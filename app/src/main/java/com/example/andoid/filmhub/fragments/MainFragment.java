package com.example.andoid.filmhub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.andoid.filmhub.adapters.TabAdapter;
import com.example.andoid.filmhub.R;
import com.google.android.material.tabs.TabLayout;

public class MainFragment extends Fragment{

    private TabAdapter adapter;
    private TabLayout tableLayout;
    private ViewPager viewPager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabs, container, false);

        // Finding the Views
        viewPager = view.findViewById(R.id.category_pager);
        tableLayout = view.findViewById(R.id.category_tabs);

        // CategoryFragment ---->
        int moviesIntentFragment = getArguments().getInt("key");

        // int type = getArguments().getInt("key");
        adapter = new TabAdapter(getFragmentManager());

        if(moviesIntentFragment == 1){

            CategoryFragment featuredMovieFragment = fragmentBundleGenerator(1);
            CategoryFragment popularMovieFragment = fragmentBundleGenerator(2);
            CategoryFragment topRatedMovieFragment = fragmentBundleGenerator(3);
            CategoryFragment upcomingMovieFragment = fragmentBundleGenerator(4);

            adapter.addFragment(featuredMovieFragment, "Featured");
            adapter.addFragment(popularMovieFragment, "Popular");
            adapter.addFragment(topRatedMovieFragment, "Rated");
            adapter.addFragment(upcomingMovieFragment, "Coming");

        }else{

            CategoryFragment featuredShowFragment = fragmentBundleGenerator(5);
            CategoryFragment popularShowFragment = fragmentBundleGenerator(6);
            CategoryFragment topRatedShowFragment = fragmentBundleGenerator(7);
            CategoryFragment onTvShowFragment = fragmentBundleGenerator(8);


            adapter.addFragment(featuredShowFragment, "Featured");
            adapter.addFragment(popularShowFragment, "Popular");
            adapter.addFragment(topRatedShowFragment, "Rated");
            adapter.addFragment(onTvShowFragment, "On Tv");
        }

        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);
        return view;
    }

    private CategoryFragment fragmentBundleGenerator(int categoryType){

        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle bundleCategory = new Bundle();
        bundleCategory.putInt("categoryType", categoryType);
        categoryFragment.setArguments(bundleCategory);
        return categoryFragment;
    }
}
