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

public class CategoryFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabs, container, false);

        // Finding the Views
        ViewPager viewPager = view.findViewById(R.id.pagerCategory);
        TabLayout tableLayout = view.findViewById(R.id.tabsCategory);

        // Getting the key
        assert getArguments() != null;
        int intentFragment = getArguments().getInt("key");

        TabAdapter adapter = new TabAdapter(getFragmentManager());

        if(intentFragment == 1){

            CategoryContentFragment featuredMovieFragment = fragmentBundleGenerator(1);
            CategoryContentFragment popularMovieFragment = fragmentBundleGenerator(2);
            CategoryContentFragment topRatedMovieFragment = fragmentBundleGenerator(3);
            CategoryContentFragment upcomingMovieFragment = fragmentBundleGenerator(4);

            adapter.addFragment(featuredMovieFragment, "Featured");
            adapter.addFragment(popularMovieFragment, "Popular");
            adapter.addFragment(topRatedMovieFragment, "Rated");
            adapter.addFragment(upcomingMovieFragment, "Coming");

        }else{

            CategoryContentFragment featuredShowFragment = fragmentBundleGenerator(5);
            CategoryContentFragment popularShowFragment = fragmentBundleGenerator(6);
            CategoryContentFragment topRatedShowFragment = fragmentBundleGenerator(7);
            CategoryContentFragment onTvShowFragment = fragmentBundleGenerator(8);


            adapter.addFragment(featuredShowFragment, "Featured");
            adapter.addFragment(popularShowFragment, "Popular");
            adapter.addFragment(topRatedShowFragment, "Rated");
            adapter.addFragment(onTvShowFragment, "On Tv");
        }

        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);
        return view;
    }

    private CategoryContentFragment fragmentBundleGenerator(int categoryType){
        // Generate a Bundle so we know how to populate the fragments
        CategoryContentFragment categoryContentFragment = new CategoryContentFragment();
        Bundle bundleCategory = new Bundle();
        bundleCategory.putInt("categoryType", categoryType);
        categoryContentFragment.setArguments(bundleCategory);
        return categoryContentFragment;
    }
}
