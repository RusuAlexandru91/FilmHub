package com.example.andoid.filmhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.andoid.filmhub.adapters.RecyclerAdapterMock;
import com.example.andoid.filmhub.util.HelperClass;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    // Mock data
    RecyclerView recyclerViewcast;
    RecyclerView recyclerViewsimilar;
    RecyclerAdapterMock recyclerAdaptercast;
    RecyclerAdapterMock recyclerAdaptersimilar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getSupportActionBar().hide();

        ImageView imageView = (ImageView) findViewById(R.id.back_button);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        ArrayList<HelperClass> cast = new ArrayList<>();
        cast.add(new HelperClass(R.drawable.person_mock, "Nume Real 1", "Nume Film 1"));
        cast.add(new HelperClass(R.drawable.person_mock, "Nume Real 2", "Nume Film 2"));
        cast.add(new HelperClass(R.drawable.person_mock, "Nume Real 3", "Nume Film 3"));
        cast.add(new HelperClass(R.drawable.person_mock, "Nume Real 3", "Nume Film 4"));
        cast.add(new HelperClass(R.drawable.person_mock, "Nume Real 3", "Nume Film 5"));


        ArrayList<HelperClass> similar = new ArrayList<>();
        similar.add(new HelperClass(R.drawable.logo, "Rec 1", "Rec 1 "));
        similar.add(new HelperClass(R.drawable.logo, "Rec 2", "Rec 2 "));
        similar.add(new HelperClass(R.drawable.logo, "Rec 3", "Rec 3 "));
        similar.add(new HelperClass(R.drawable.logo, "Rec 3", "Rec 4 "));
        similar.add(new HelperClass(R.drawable.logo, "Rec 3", "Rec 5 "));



        recyclerViewcast = findViewById(R.id.recyclerView_cast);
        recyclerViewsimilar = findViewById(R.id.recyclerView_similar);

        recyclerAdaptercast = new RecyclerAdapterMock(cast);
        recyclerAdaptersimilar = new RecyclerAdapterMock(similar);

        recyclerViewcast.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewsimilar.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recyclerViewcast.setAdapter(recyclerAdaptercast);
        recyclerViewsimilar.setAdapter(recyclerAdaptersimilar);
    }
}