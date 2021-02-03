package com.example.andoid.filmhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.andoid.filmhub.adapters.RecyclerAdapter;
import com.example.andoid.filmhub.adapters.RecyclerAdapterMock;
import com.example.andoid.filmhub.fragments.MainFragment;
import com.example.andoid.filmhub.fragments.SearchFragment;
import com.example.andoid.filmhub.util.HelperClass;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ContainerActivity extends AppCompatActivity {
    MeowBottomNavigation meow;
    int moviesIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        meow = findViewById(R.id.navigation_bar);

        meow.add(new MeowBottomNavigation.Model(1,R.drawable.ic_tv));
        meow.add(new MeowBottomNavigation.Model(2,R.drawable.ic_home));
        meow.add(new MeowBottomNavigation.Model(3,R.drawable.ic_tv_series));

        moviesIntent = getIntent().getExtras().getInt("key");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        /// Mainactivit  ----> CategoryFragment
        if (moviesIntent == 1 || moviesIntent == 2){
            MainFragment mainFragment = new MainFragment();
            // Bundle is like an intent but for fragments :))
            // Bundle for Categories
            Bundle bundle = new Bundle();
            bundle.putInt("key", moviesIntent);
            mainFragment.setArguments(bundle);
            ft.replace(R.id.container_categories, mainFragment);
            ft.commit();
        }else if(moviesIntent== 4){
            // Bundle for Search
            SearchFragment searchFragment = new SearchFragment();
            Bundle bundle1 = new Bundle();
            bundle1.putInt("key", moviesIntent);
            searchFragment.setArguments(bundle1);
            ft.replace(R.id.container_categories, searchFragment);
            ft.commit();
            meow.setVisibility(View.INVISIBLE);
        }


        if( moviesIntent == 1){
            meow.show(1,true);
            getSupportActionBar().setTitle("Movies");
        }else{
            meow.show(3,true);
            getSupportActionBar().setTitle("Shows");
        }

        meow.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                if (model.getId() == 1){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intentToOpenContainerActivity = new Intent(ContainerActivity.this, ContainerActivity.class);
                                intentToOpenContainerActivity.putExtra("key", 1);
                                startActivity(intentToOpenContainerActivity);
//                                Animatoo.animateSlideUp(ContainerActivity.this);
                            }
                        }, 500);

                }else if(model.getId() == 2){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        Intent intentToGoBackHome = new Intent(ContainerActivity.this, MainActivity.class);
                        startActivity(intentToGoBackHome);
//                        Animatoo.animateSlideUp(ContainerActivity.this);
                        }
                    }, 500);

                }else if(model.getId() == 3){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intentToOpenContainerActivity = new Intent(ContainerActivity.this, ContainerActivity.class);
                                intentToOpenContainerActivity.putExtra("key", 2);
                                startActivity(intentToOpenContainerActivity);
//                                Animatoo.animateSlideUp(ContainerActivity.this);
                            }
                        }, 500);
                    }
                return null;
            }
        });
    }
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
//    /** Makeing the options menus **/
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.search_settings) {
//            Intent intentToOpenSearchActivity =  new Intent(ContainerActivity.this, ContainerActivity.class);
//            intentToOpenSearchActivity.putExtra("key", 4);
//            startActivity(intentToOpenSearchActivity);
//            Animatoo.animateSlideUp(this);
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
