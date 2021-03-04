package com.example.andoid.filmhub;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.andoid.filmhub.adapters.ExpandableListAdapter;
import com.example.andoid.filmhub.adapters.RecyclerAdapter;
import com.example.andoid.filmhub.retrofit.ApiClient;
import com.example.andoid.filmhub.retrofit.ApiInterface;
import com.example.andoid.filmhub.retrofit.MainDetailsApi;
import com.example.andoid.filmhub.retrofit.MainResultsApi;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    // Retrofit Var
    ApiInterface apiInterface;
    // Recycler and Adapter Var
    RecyclerView moviesRecyclerView;
    RecyclerView showsRecyclerView;
    RecyclerAdapter moviesRecyclerAdapter;
    RecyclerAdapter showsRecyclerAdapter;
    // Drawer Var
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    // Exp ListView Var
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    //Bottom Nav Var
    MeowBottomNavigation meow;
    // Lottie Var
    LottieAnimationView movieslottieloading, showsLottieLoading;
    // Swipe to refresh Var
    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Swipe refresh
        pullToRefresh();

        //Meow navigation
        meowNavigation();

        // Recyler clickListeners
        setClickListeners();

        // Mehods for expandable listView and Drawer
        createTheDrawer();
        getListViewDrawer();

        // Retrofit method
        resposeForMovies();
        resposeForShows();
    }

    /**
     * Meow Bottom navigationbar
     **/
    public void meowNavigation() {
        meow = findViewById(R.id.navigation_bar);

        // makeing the nav bar
        meow.add(new MeowBottomNavigation.Model(1, R.drawable.ic_tv));
        meow.add(new MeowBottomNavigation.Model(2, R.drawable.ic_home));
        meow.add(new MeowBottomNavigation.Model(3, R.drawable.ic_tv_series));
        meow.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {

            // behavior of navbar
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                if (model.getId() == 1) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intentToOpenContainerActivity = new Intent(MainActivity.this, ContainerActivity.class);
                            intentToOpenContainerActivity.putExtra("key", 1);
                            startActivity(intentToOpenContainerActivity);
                        }
                    }, 250);
                } else if (model.getId() == 2) {
                    Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                } else if (model.getId() == 3) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intentToOpenContainerActivity = new Intent(MainActivity.this, ContainerActivity.class);
                            intentToOpenContainerActivity.putExtra("key", 2);
                            startActivity(intentToOpenContainerActivity);
                        }
                    }, 250);
                }
                return null;
            }
        });
    }

    /**
     * Method to create the drawer menu
     **/
    private void createTheDrawer() {

        drawerLayout = findViewById(R.id.drawer_layout);

        // Setting the drawer behavior
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Method to populate the drawer menu
     **/
    private void getListViewDrawer() {
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        // Seting listener for expandable list view from drawer
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (groupPosition == 1 && childPosition == 0) {
                    Intent child0Intent = new Intent(Intent.ACTION_VIEW);
                    child0Intent.setData(Uri.parse("https://www.linkedin.com/in/alexandru-rusu-982975203/"));
                    startActivity(child0Intent);
                } else if (groupPosition == 1 && childPosition == 1) {
                    Intent child1Intent = new Intent(Intent.ACTION_VIEW);
                    child1Intent.setData(Uri.parse("https://github.com/kiwibv"));
                    startActivity(child1Intent);
                } else if (groupPosition == 1 && childPosition == 2) {
                    Intent child2intent = new Intent(Intent.ACTION_SENDTO);
                    child2intent.setData(Uri.parse("mailto:kiwibv@gmail.com"));
                    startActivity(child2intent);
                } else if (groupPosition == 0 && childPosition == 0) {
                    Intent intentToOpenDiscoverActivity = new Intent(MainActivity.this, DiscoverActivity.class);
                    intentToOpenDiscoverActivity.putExtra("intentKey", 0);
                    startActivity(intentToOpenDiscoverActivity);
                } else if (groupPosition == 0 && childPosition == 1) {
                    Intent intentToOpenDiscoverActivity = new Intent(MainActivity.this, DiscoverActivity.class);
                    intentToOpenDiscoverActivity.putExtra("intentKey", 1);
                    startActivity(intentToOpenDiscoverActivity);
                }
                return false;
            }
        });

        // Move ExpandableListView cursor to right
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                300, r.getDisplayMetrics());
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expListView.setIndicatorBounds(width - px, width);
        } else {
            expListView.setIndicatorBoundsRelative(width - px, width);
        }
    }

    /**
     * Populate exapandable list from Drawer
     **/
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding parent data
        listDataHeader.add("Discover");
        listDataHeader.add("Contact");

        List<String> Discover = new ArrayList<>();
        Discover.add("Movies");
        Discover.add("Shows");

        // Adding child data
        List<String> Contact = new ArrayList<>();
        Contact.add("Linkedin");
        Contact.add("Github");
        Contact.add("Mail");

        // Header, Child data
        listDataChild.put(listDataHeader.get(0), Discover);
        listDataChild.put(listDataHeader.get(1), Contact);
    }

    /**
     * Setting click listeners
     **/
    private void setClickListeners() {
        // On click as object
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mainactivit  ---->
                if (v.getId() == R.id.moviesSeeAllTextView) {
                    Intent intentToOpenContainerActivity = new Intent(MainActivity.this, ContainerActivity.class);
                    intentToOpenContainerActivity.putExtra("key", 1);
                    startActivity(intentToOpenContainerActivity);
                } else if (v.getId() == R.id.showsSeeAllTextView) {
                    Intent intentToOpenContainerActivity = new Intent(MainActivity.this, ContainerActivity.class);
                    intentToOpenContainerActivity.putExtra("key", 2);
                    startActivity(intentToOpenContainerActivity);
                } else if (v.getId() == R.id.expandableMoviesTextView) {
                    Intent intentToOpenContainerActivity = new Intent(MainActivity.this, ContainerActivity.class);
                    intentToOpenContainerActivity.putExtra("key", 1);
                    startActivity(intentToOpenContainerActivity);
                } else if (v.getId() == R.id.expandableShowsTextView) {
                    Intent intentToOpenContainerActivity = new Intent(MainActivity.this, ContainerActivity.class);
                    intentToOpenContainerActivity.putExtra("key", 2);
                    startActivity(intentToOpenContainerActivity);
                } else if (v.getId() == R.id.expandableCreditsTextView) {
                    Intent intentToOpenCredits = new Intent(Intent.ACTION_VIEW);
                    intentToOpenCredits.setData(Uri.parse("https://www.themoviedb.org/"));
                    startActivity(intentToOpenCredits);
                } else if (v.getId() == R.id.expandableFavoriteTextView) {
                    Intent intentToOpenFavoritesActivity = new Intent(MainActivity.this, FavoriteActivity.class);
                    startActivity(intentToOpenFavoritesActivity);
                }
            }
        };

        // Find views
        TextView movies = findViewById(R.id.moviesSeeAllTextView);
        TextView shows = findViewById(R.id.showsSeeAllTextView);
        TextView movies_drawer = findViewById(R.id.expandableMoviesTextView);
        TextView shows_drawer = findViewById(R.id.expandableShowsTextView);
        TextView credits_drawer = findViewById(R.id.expandableCreditsTextView);
        TextView favorite_drawer = findViewById(R.id.expandableFavoriteTextView);

        // Set clickListener by views
        movies.setOnClickListener(listener);
        movies_drawer.setOnClickListener(listener);
        shows.setOnClickListener(listener);
        shows_drawer.setOnClickListener(listener);
        credits_drawer.setOnClickListener(listener);
        favorite_drawer.setOnClickListener(listener);
    }

    /**
     * Populate the movieRecyclerView
     **/
    private void resposeForMovies() {

        moviesRecyclerView = findViewById(R.id.recyclerViewMovieHorizontal);
        moviesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        moviesRecyclerAdapter = new RecyclerAdapter(new ArrayList<>());
        moviesRecyclerView.setAdapter(moviesRecyclerAdapter);

        // Makeing the call for movies
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<MainResultsApi> call = apiInterface.getTrandingMovies(1);
        call.enqueue(new Callback<MainResultsApi>() {
            @Override
            public void onResponse(@NotNull Call<MainResultsApi> call, @NotNull Response<MainResultsApi> response) {
                MainResultsApi mainResultsApi = response.body();
                List<MainDetailsApi> mainDetailsApi = mainResultsApi.getResults();
                moviesRecyclerAdapter = new RecyclerAdapter(mainDetailsApi);
                moviesRecyclerView.setAdapter(moviesRecyclerAdapter);
                lottieLoaderInvisible();
            }

            @Override
            public void onFailure(@NotNull Call<MainResultsApi> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure movies: " + t.getLocalizedMessage());
                lottieLoaderVisible();
            }
        });
    }

    /**
     * Populate the showRecyclerView
     **/
    private void resposeForShows() {

        showsRecyclerView = findViewById(R.id.recyclerViewShowsHorizonal);
        showsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        moviesRecyclerAdapter = new RecyclerAdapter(new ArrayList<>());
        moviesRecyclerView.setAdapter(moviesRecyclerAdapter);

        // Makeing the call for shows
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<MainResultsApi> call = apiInterface.getTrandingSeries(1);
        call.enqueue(new Callback<MainResultsApi>() {
            @Override
            public void onResponse(@NotNull Call<MainResultsApi> call, @NotNull Response<MainResultsApi> response) {
                MainResultsApi mainResultsApi = response.body();
                List<MainDetailsApi> mainDetailsApi = mainResultsApi.getResults();
                showsRecyclerAdapter = new RecyclerAdapter(mainDetailsApi);
                showsRecyclerView.setAdapter(showsRecyclerAdapter);
            }

            @Override
            public void onFailure(@NotNull Call<MainResultsApi> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure shows: " + t.getLocalizedMessage());
                lottieLoaderVisible();
            }
        });
    }

    /**
     * Inflate options menus
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Behavior for options menus
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.search_settings) {
            Intent intentToOpenSearchActivity = new Intent(MainActivity.this, ContainerActivity.class);
            intentToOpenSearchActivity.putExtra("key", 4);
            startActivity(intentToOpenSearchActivity);
            // Adding animation to fragment
            Animatoo.animateSlideUp(this);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Make loader visibile for fail Retrofit response
     **/
    public void lottieLoaderVisible() {
        movieslottieloading = (LottieAnimationView) findViewById(R.id.moviesLottieLoading);
        movieslottieloading.setVisibility(View.VISIBLE);
        showsLottieLoading = (LottieAnimationView) findViewById(R.id.showsLottieLoading);
        showsLottieLoading.setVisibility(View.VISIBLE);
    }

    /**
     * Make loader invisible for succcess Retrofit response
     **/
    public void lottieLoaderInvisible() {
        movieslottieloading = (LottieAnimationView) findViewById(R.id.moviesLottieLoading);
        movieslottieloading.setVisibility(View.GONE);
        showsLottieLoading = (LottieAnimationView) findViewById(R.id.showsLottieLoading);
        showsLottieLoading.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
    }

    /**
     * Make meow animation visible for MainActivity
     **/
    @Override
    protected void onResume() {
        super.onResume();
        meow.show(2, true);
    }

    /**
     * Pull to refresh option
     **/
    private void pullToRefresh() {
        pullToRefresh = findViewById(R.id.pullToRefreshLayout);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Make retrofit call again on pull to refresh
                resposeForMovies();
                resposeForShows();
                pullToRefresh.setRefreshing(false);
            }
        });
    }
}