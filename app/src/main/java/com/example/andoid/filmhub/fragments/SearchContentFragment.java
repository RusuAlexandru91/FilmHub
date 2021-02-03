package com.example.andoid.filmhub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andoid.filmhub.R;
import com.example.andoid.filmhub.adapters.RecyclerAdapterMock;
import com.example.andoid.filmhub.util.HelperClass;
import java.util.ArrayList;


public class SearchContentFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerAdapterMock recyclerAdapterMock;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_layout, container, false);



        ArrayList<HelperClass> user = new ArrayList<>();
        user.add(new HelperClass(R.drawable.mock, "Titlu 1"));
        user.add(new HelperClass(R.drawable.mock, "Titlu 2"));
        user.add(new HelperClass(R.drawable.mock, "Titlu 3"));
        user.add(new HelperClass(R.drawable.mock, "Titlu 4"));
        user.add(new HelperClass(R.drawable.mock, "Titlu 5"));
        user.add(new HelperClass(R.drawable.mock, "Titlu 6"));



        recyclerView = view.findViewById(R.id.recyclerView_search);
        recyclerAdapterMock = new RecyclerAdapterMock(user);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(recyclerAdapterMock);
        return view;
    }
}
