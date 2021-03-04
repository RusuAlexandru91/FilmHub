package com.example.andoid.filmhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.andoid.filmhub.fragments.CategoryFragment;
import com.example.andoid.filmhub.fragments.SearchFragment;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ContainerActivity extends AppCompatActivity {

    MeowBottomNavigation meow;
    int keyExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        // Getting the key
        keyExtra = getIntent().getExtras().getInt("key");

        // Calling the methods
        bottomNavigation();
        fragmentsBehavior();


    }

    /**
     * method to monitor the actionBat title
     **/
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    /**
     * Setting the fragments behavior
     **/
    public void fragmentsBehavior() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        /// Mainactivit  ----> CategoryFragment
        if (keyExtra == 1 || keyExtra == 2) {
            CategoryFragment categoryFragment = new CategoryFragment();
            // Bundle is like an intent but for fragments :))
            // Bundle for Categories
            Bundle bundle = new Bundle();
            bundle.putInt("key", keyExtra);
            categoryFragment.setArguments(bundle);
            ft.replace(R.id.container_categories, categoryFragment);
            ft.commit();
        } else if (keyExtra == 4) {
            // Bundle for Search
            SearchFragment searchFragment = new SearchFragment();
            Bundle bundle1 = new Bundle();
            bundle1.putInt("key", keyExtra);
            searchFragment.setArguments(bundle1);
            ft.replace(R.id.container_categories, searchFragment);
            ft.commit();
            meow.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Setting the bottom navigation
     **/
    public void bottomNavigation() {
        meow = findViewById(R.id.navigation_bar);

        meow.add(new MeowBottomNavigation.Model(1, R.drawable.ic_tv));
        meow.add(new MeowBottomNavigation.Model(2, R.drawable.ic_home));
        meow.add(new MeowBottomNavigation.Model(3, R.drawable.ic_tv_series));

        if (keyExtra == 1) {
            meow.show(1, false);
            getSupportActionBar().setTitle("Movies");
        } else {
            meow.show(3, false);
            getSupportActionBar().setTitle("Shows");
        }

        meow.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                if (model.getId() == 1) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intentToOpenContainerActivity = new Intent(ContainerActivity.this, ContainerActivity.class);
                            intentToOpenContainerActivity.putExtra("key", 1);
                            startActivity(intentToOpenContainerActivity);
                        }
                    }, 250);

                } else if (model.getId() == 2) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intentToGoBackHome = new Intent(ContainerActivity.this, MainActivity.class);
                            startActivity(intentToGoBackHome);
                        }
                    }, 250);

                } else if (model.getId() == 3) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intentToOpenContainerActivity = new Intent(ContainerActivity.this, ContainerActivity.class);
                            intentToOpenContainerActivity.putExtra("key", 2);
                            startActivity(intentToOpenContainerActivity);
                        }
                    }, 250);
                }
                return null;
            }
        });
    }
}
