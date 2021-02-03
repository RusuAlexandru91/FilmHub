package com.example.andoid.filmhub;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.andoid.filmhub.adapters.ExpandableListAdapter;
import com.example.andoid.filmhub.adapters.RecyclerAdapter;
import com.example.andoid.filmhub.fragments.SearchFragment;
import com.example.andoid.filmhub.retrofit.ApiClient;
import com.example.andoid.filmhub.retrofit.ApiInterface;
import com.example.andoid.filmhub.retrofit.ArrayApi;
import com.example.andoid.filmhub.retrofit.ObjectApi;
import com.example.andoid.filmhub.util.HelperClass;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    ApiInterface apiInterface;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    RecyclerAdapter recyclerAdapter;
    RecyclerAdapter recyclerAdapter2;

    // varable for drawer
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    // Variables for expandable listview
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    MeowBottomNavigation meow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Meow navigation
        meowNavigation();

        // Methods for RecyclerVew
        setClickListeners();

        // Mehods for expandable listView and Drawer
        createTheDrawer();
        getListViewDrawer();

        // Retrofit method
        resposeForMovies();
        resposeForShows();
    }

    /** Meow Bottom navigationbar **/
    public void meowNavigation(){
        meow = findViewById(R.id.navigation_bar);

        meow.add(new MeowBottomNavigation.Model(1,R.drawable.ic_tv));
        meow.add(new MeowBottomNavigation.Model(2,R.drawable.ic_home));
        meow.add(new MeowBottomNavigation.Model(3,R.drawable.ic_tv_series));
        meow.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                if (model.getId() == 1){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intentToOpenContainerActivity =  new Intent(MainActivity.this, ContainerActivity.class);
                            intentToOpenContainerActivity.putExtra("key", 1);
                            startActivity(intentToOpenContainerActivity);
//                            Animatoo.animateSlideUp(MainActivity.this);
                        }
                    }, 500);
                }else if(model.getId() == 2){
                    Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                }else if(model.getId() == 3){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intentToOpenContainerActivity = new Intent(MainActivity.this, ContainerActivity.class);
                            intentToOpenContainerActivity.putExtra("key", 2);
                            startActivity(intentToOpenContainerActivity);
//                            Animatoo.animateSlideUp(MainActivity.this);
                        }
                    }, 500);
                }
                return null;
            }
        });
    }

    /** Method to create the drawer menu **/
    private void createTheDrawer(){
        // Craeting the drawer menu
        drawerLayout = findViewById(R.id.drawer_layout);

//        navigationView = findViewById(R.id.drawer_header);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                Toast.makeText(MainActivity.this,"Closed",Toast.LENGTH_SHORT);
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                Toast.makeText(MainActivity.this,"Open",Toast.LENGTH_SHORT);
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /** Method to populate the drawer menu **/
    private void getListViewDrawer(){

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if( childPosition == 0){
                    // Intent for contact --> Linkedin
                    Intent child0Intent = new Intent(Intent.ACTION_VIEW);
                    child0Intent.setData(Uri.parse("https://www.linkedin.com/in/alexandru-rusu-982975203/"));
                    startActivity(child0Intent);
                }else if(childPosition == 1){
                    // Intent for contact --> Github
                    Intent child1Intent = new Intent(Intent.ACTION_VIEW);
                    child1Intent.setData(Uri.parse("https://github.com/kiwibv"));
                    startActivity(child1Intent);
                }else{
                    // Send mail intent -->
                    Intent child2intent = new Intent(Intent.ACTION_SENDTO);
                    child2intent.setData(Uri.parse("mailto:kiwibv@gmail.com"));
                    startActivity(child2intent);
                }
                return false;
            }
        });

        // Move ExpandableListView cursor to right
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.lvExp);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                300, r.getDisplayMetrics());
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expandableListView.setIndicatorBounds(width - px, width);
        } else {
            expandableListView.setIndicatorBoundsRelative(width - px, width);
        }
    }

    // Prepareing the list data
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Contact");

        // Adding child data
        List<String> Movies = new ArrayList<String>();
        Movies.add("Linkedin");
        Movies.add("Github");
        Movies.add("Mail");

        listDataChild.put(listDataHeader.get(0), Movies); // Header, Child data
    }
    /** Settng click listeners **/
    private void setClickListeners(){
        // On click as object
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Mainactivit  ---->
                if(v.getId() == R.id.movies_see_all){
                    Intent intentToOpenContainerActivity =  new Intent(MainActivity.this, ContainerActivity.class);
                    intentToOpenContainerActivity.putExtra("key", 1);
                    startActivity(intentToOpenContainerActivity);
                }else if (v.getId() == R.id.shows_see_all) {
                    Intent intentToOpenContainerActivity = new Intent(MainActivity.this, ContainerActivity.class);
                    intentToOpenContainerActivity.putExtra("key", 2);
                    startActivity(intentToOpenContainerActivity);
                }else if(v.getId() == R.id.exp_movie_textview){
                    Intent intentToOpenContainerActivity =  new Intent(MainActivity.this, ContainerActivity.class);
                    intentToOpenContainerActivity.putExtra("key", 1);
                    startActivity(intentToOpenContainerActivity);
                }else if(v.getId() == R.id.exp_shows_textview){
                    Intent intentToOpenContainerActivity =  new Intent(MainActivity.this, ContainerActivity.class);
                    intentToOpenContainerActivity.putExtra("key", 2);
                    startActivity(intentToOpenContainerActivity);
                }else if(v.getId() == R.id.exp_credits_textview){
                    Intent intentToOpenCredits = new Intent(Intent.ACTION_VIEW);
                    intentToOpenCredits.setData(Uri.parse("https://www.themoviedb.org/"));
                    startActivity(intentToOpenCredits);
                }else if(v.getId() == R.id.exp_favorite_textview){
                    Intent intentToOpenFavoritesActivity = new Intent(MainActivity.this, FavoriteActivity.class);
                    startActivity(intentToOpenFavoritesActivity);
                }else if (v.getId() == R.id.exp_search_textview){
                    Intent intentToOpenDiscoverActivity = new Intent(MainActivity.this, DiscoverActivity.class);
                    startActivity(intentToOpenDiscoverActivity);
                }else if(v.getId() == R.id.exp_home_tetxtview){
                    Toast.makeText(MainActivity.this,"You are home",Toast.LENGTH_SHORT).show();
                }
            }
        };

        // Find views
        TextView movies =  findViewById(R.id.movies_see_all);
        TextView shows = findViewById(R.id.shows_see_all);
        TextView movie_drawer =  findViewById(R.id.exp_movie_textview);
        TextView shows_drawer = findViewById(R.id.exp_shows_textview);
        TextView credits_drawer = findViewById(R.id.exp_credits_textview);
        TextView favorite_drawer = findViewById(R.id.exp_favorite_textview);
        TextView discover_drawer = findViewById(R.id.exp_search_textview);
        TextView home_drawer = findViewById(R.id.exp_home_tetxtview);

        // Set clickListener by views
        movies.setOnClickListener(listener);
        movie_drawer.setOnClickListener(listener);
        shows.setOnClickListener(listener);
        shows_drawer.setOnClickListener(listener);
        credits_drawer.setOnClickListener(listener);
        favorite_drawer.setOnClickListener(listener);
        discover_drawer.setOnClickListener(listener);
        home_drawer.setOnClickListener(listener);
    }

    /** Methods for populateing the recyclerview **/
    private void resposeForShows() {

        recyclerView2 = findViewById(R.id.recyclerViewshowshorizonal);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerAdapter = new RecyclerAdapter(new ArrayList<>());
        recyclerView.setAdapter(recyclerAdapter);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ObjectApi> call = apiInterface.getTrandingSeries(1);
        call.enqueue(new Callback<ObjectApi>() {
            @Override
            public void onResponse(Call<ObjectApi> call, Response<ObjectApi> response) {
                ObjectApi objectApi = response.body();
                List<ArrayApi> arrayApi = objectApi.getResults();
                recyclerAdapter2 = new RecyclerAdapter(arrayApi);
                recyclerView2.setAdapter(recyclerAdapter2);
            }

            @Override
            public void onFailure(Call<ObjectApi> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void resposeForMovies(){

        recyclerView = findViewById(R.id.recyclerViewmoviehorizontal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerAdapter = new RecyclerAdapter(new ArrayList<>());
        recyclerView.setAdapter(recyclerAdapter);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ObjectApi> call = apiInterface.getTrandingMovies(1);
        call.enqueue(new Callback<ObjectApi>() {
            @Override
            public void onResponse(Call<ObjectApi> call, Response<ObjectApi> response) {
                ObjectApi objectApi = response.body();
                List<ArrayApi> arrayApi = objectApi.getResults();
                recyclerAdapter = new RecyclerAdapter(arrayApi);
                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onFailure(Call<ObjectApi> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage() );
            }
        });
    }

    /** Makeing the options menus **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.search_settings) {
            Intent intentToOpenSearchActivity =  new Intent(MainActivity.this, ContainerActivity.class);
            intentToOpenSearchActivity.putExtra("key", 4);
            startActivity(intentToOpenSearchActivity);
            Animatoo.animateSlideUp(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
    }
    @Override
    protected void onResume() {
        super.onResume();
        meow.show(2,true);
    }
}