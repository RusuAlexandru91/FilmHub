package com.example.andoid.filmhub.fragments;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.andoid.filmhub.ContainerActivity;
import com.example.andoid.filmhub.MainActivity;
import com.example.andoid.filmhub.R;
import com.example.andoid.filmhub.adapters.TabAdapter;
import com.google.android.material.tabs.TabLayout;

public class SearchFragment extends Fragment {

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

        adapter = new TabAdapter(getFragmentManager());

        adapter.addFragment(new SearchContentFragment(), "Movies");
        adapter.addFragment(new SearchContentFragment(), "Shows");

        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((ContainerActivity) getActivity()).setActionBarTitle("Search");
    }
}
