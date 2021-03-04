package com.example.andoid.filmhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.andoid.filmhub.adapters.RecyclerAdapterDb;
import com.example.andoid.filmhub.roomdb.Items;
import com.example.andoid.filmhub.roomdb.ItemsDatabase;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    private static final String TAG = "FavoriteActivity";
    RecyclerAdapterDb recyclerAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    LottieAnimationView lottieSwipe;
    SwipeRefreshLayout pullToRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vertical_recycler);

        pullToRefresh();

        // Find/Set recylcer, adapate and layoutManager
        recyclerView = findViewById(R.id.verticalRecyclerView);
        recyclerAdapter = new RecyclerAdapterDb(new ArrayList<>());
        recyclerView.setAdapter(recyclerAdapter);
        layoutManager = new LinearLayoutManager(FavoriteActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Display datebase with LiveData
        LiveData<List<Items>> users = ItemsDatabase.getInstance(FavoriteActivity.this).userDao().getAllItems();

        // TouchHelper to delete on swipe left
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                String id = recyclerAdapter.getMovieOrShowId(position);
                // Delete the selected title in AsynkTask
                DeleteAsynkTask deleteAsynkTask = new DeleteAsynkTask();
                deleteAsynkTask.execute(id);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        users.observe(this, new Observer<List<Items>>() {
            @Override
            public void onChanged(List<Items> items) {
                Log.d(TAG, "onChanged: size =  " + items.size());
                recyclerAdapter.setmList(items);

                Log.d(TAG, "onPostExecute: item count " + recyclerAdapter.getItemCount());

                // Condition to display a text message if database is empty
                if (recyclerAdapter.getItemCount() == 0) {
                    TextView textView = (TextView) findViewById(R.id.errorTextView);
                    textView.setText(R.string.favorite_error_text);
                    textView.setVisibility(View.VISIBLE);

                }

            }
        });
    }

    /**
     * Refresh when pull
     **/
    public void pullToRefresh() {
        pullToRefresh = findViewById(R.id.pullToRefreshLayout);
        pullToRefresh.setOnRefreshListener(() -> {
            recyclerAdapter.notifyDataSetChanged();
            pullToRefresh.setRefreshing(false);

        });
    }

    @SuppressLint("StaticFieldLeak")
    public class DeleteAsynkTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            ItemsDatabase.getInstance(FavoriteActivity.this).userDao().deleteItemWhereId(strings[0]);
            return null;
        }
    }

    /**
     * Makeing the options menus
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete, menu);
        return true;
    }

    /**
     * Options menu behavior
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        lottieSwipe = (LottieAnimationView) findViewById(R.id.swipeToDeleteLottieAnimation);
        TextView textView = (TextView) findViewById(R.id.swipeToDeleteText);

        // delayed action with 0.5 secounds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lottieSwipe.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
            }
        }, 500);

        // Animation listener in order to loop only once ( 4 mehots in total )
        lottieSwipe.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // delayed action with 0.5 secounds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lottieSwipe.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                    }
                }, 500);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                lottieSwipe.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                lottieSwipe.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                lottieSwipe.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
            }
        });

        return super.onOptionsItemSelected(item);
    }
}